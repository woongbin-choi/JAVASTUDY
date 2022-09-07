package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// readOnly = true  -> 성능 최적화 / 조회 할 때 자주 사용
@Service
@Transactional(readOnly = true)
// final 필드만 생성자를 만들어줌. 그래서 밑에 주석처리
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 생성자 인잭션 (유용하게 커스터마이징 할 수 있다)
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 1건 조회
    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }


}
