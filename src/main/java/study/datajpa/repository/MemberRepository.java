package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 조건이 3개 이상 될 시 복잡해지고 너무 길어지는 단점.
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);


    // 실무에서 많이 사용한다. (복잡한 정적 쿼리) -> 동적 쿼리는 QueryDSL을 사용
    // 쿼리문 안에 프로퍼티 오타가 있을 때 애플리케이션 실행 시 오류로 잡아주는 장점이 있다
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();
}
