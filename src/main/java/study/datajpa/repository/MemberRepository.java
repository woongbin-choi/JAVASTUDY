package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

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
}
