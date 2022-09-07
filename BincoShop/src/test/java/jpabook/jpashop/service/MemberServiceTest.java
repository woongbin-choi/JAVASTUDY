package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원가입 테스트")
    @Rollback(false) // DB로 직접 보고 싶으면 추가
    public void joinTest() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush();// Transactional 로 인해서 insert 안하는걸 쿼리문 보기 위해 추가
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    @DisplayName("회원가입 중복 예외")
    public void duplicateTest() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("choi");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }

        // then
        fail("예외가 발생해야 한다.");
    }

}