package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void testQuery() {
        Member binco = new Member("Binco", 27);
        memberRepository.save(binco);

        List<Member> result = memberRepository.findUser("Binco", 27);
        Member findUser = result.get(0);

        assertThat(findUser).isEqualTo(binco);
    }

    @Test
    public void findUsernameList() {
        Member binco = new Member("Binco", 27);
        Member tistory = new Member("Tistory", 30);
        memberRepository.save(binco);
        memberRepository.save(tistory);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    /*
    페이징 조건과 정렬 조건 설정 -> 알아서 페이징 계산을 다 해준다.

    Slice는 Count 쿼리를 날리지 않는다. page와 다른 점은 limit에 +1을 해서 데이터를 가져온다. 프론트 단에서 더보기 형식으로 눈속임을 위함.
     */
    @Test
    public void page() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        // 주의! 페이지는 0부터 시작이다.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC,
                "username"));
        Page<Member> page = memberRepository.findByAge(10, (Pageable) pageRequest);

        // 위 page는 Member Entity이기 때문에 API(Controller)에서 외부로 반환하면 안된다.
        // 밑에 방식으로 Dto 형식으로 변환하고 반환해야 한다.
        Page<MemberDto> DtoMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent(); //조회된 데이터
        assertThat(content.size()).isEqualTo(3); //조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); //페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); //전체 페이지 번호
        assertThat(page.isFirst()).isTrue(); //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue(); //다음 페이지가 있는가?
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        /*
         벌크성 수정 쿼리 (@Modifying 어노테이션이 있어야 업데이트가 실행된다.)
         영속성 컨텍스트와 관계없이 바로 DB에 적용하기 때문에 영속성 컨텍스트를 날려야한다. 안그러면 영속성 컨텍스트에는 값이 그대로고 DB값만 바뀌어서 문제가 생긴다.
         테스트는 순수 JPA와 똑같이 작동
        */
        //        em.clear(); 를 하거나 Modifying 어노테이션에 설정을 해야함
        //        사실 영속성 컨텍스트와 벌크 연산을 같이 사용하지 않으면 됨.

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    // Entity Graph
    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush(); // DB에 바로 반영
        em.clear(); // 영속성 컨텍스트 Clear

        //when (Lazy, N+1 문제 -> fetch Join이 필요함)
//        List<Member> members = memberRepository.findAll();

        List<Member> members = memberRepository.findMemberFetchJoin();

        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    public void queryHint() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();
        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");
        em.flush(); //Update Query 실행X
    }

}