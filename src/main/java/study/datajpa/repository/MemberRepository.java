package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 메소드 이름으로 쿼리 생성
    List<Member> findByUsername(String username);
    List<Member> findByTop3();

    // 조건이 3개 이상 될 시 복잡해지고 너무 길어지는 단점.
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);


    // 실무에서 많이 사용한다. (복잡한 정적 쿼리) -> 동적 쿼리는 QueryDSL을 사용
    // 쿼리문 안에 프로퍼티 오타가 있을 때 애플리케이션 실행 시 오류로 잡아주는 장점이 있다
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // Collection In절 처리
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO로 조회하기 - new Operation을 사용하여 Dto 추출
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /*
    반환타입이 유연하다
     */
    List<Member> findListByUsername(String username); // 컬렉션 (데이터가 없을 경우에는 Empty 컬렉션을 반환함. Null이 아님)
    Optional<Member> findOptionalByUsername(String username); // 단건 (null 일수도 아닐수도 있음, 만약 결과가 2개 이상 나오면 예외 발생)
    Member findMemberByUsername(String username); // 단건 (데이터가 없을 경우에 Null을 반환)

    // Paging (페이지는 0부터 시작한다)
    // TotalCount가 데이터가 많을 시에 조회하는데 시간이 많이 걸릴 수 있다. JPA에서는 카운트 쿼리를 분리할 수 있게 제공한다
    // 카운트 쿼리 분리 안할꺼면 @Query 빼도 된다.
    // 정렬도 복잡한 정렬이 필요한 경우 카운트 쿼리처럼 따로 빼야 한다.
    @Query(value = "select m from Member m",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    /*
     벌크성 수정 쿼리 (@Modifying 어노테이션이 있어야 업데이트가 실행된다.)
     영속성 컨텍스트와 관계없이 바로 DB에 적용하기 때문에 영속성 컨텍스트를 날려야한다. 안그러면 영속성 컨텍스트에는 값이 그대로고 DB값만 바뀌어서 문제가 생긴다.
     테스트는 순수 JPA와 똑같이 작동
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    /*
      fetch join
     */
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    /*
     Entity Graph(사실상 fetch join의 간편 버전) - 밑에 3개 다 똑같은 기능이긴 하다.
     */
    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // 오직 Read만 수행할 경우에 사용한다.
    // 사용하는 곳에서 set을 통해 더티 체킹을 안하더라도 jpa에서는 db에서 데이터를 가져오는 즉시
    // 변경감지를 위해 원본과 스냅샷을 1차캐시에 생성하기 때문에
    // 비용이 발생한다. 성능 최적화를 위해 스냅샷을 안만드는것이 좋다
    // 복잡한 쿼리에서 사용할때(트래픽이 많고 중요한 로직) 좋다. 막 다 넣어도 그렇게 성능 최적화 안된다. 성능 테스트해보고 결정
    @QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly",
            value = "true")},
            forCounting = true)
    Member findReadOnlyByUsername(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findByUsernameLock(String name);
}
