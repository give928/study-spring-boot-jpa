package com.give928.springboot.jpa.service;

import com.give928.springboot.jpa.domain.member.Member;
import com.give928.springboot.jpa.repository.member.MemberRepository;
import com.give928.springboot.jpa.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
//    @Rollback(false) // 테스트에서 롤백하지 않으려면
    void 회원가입() {
        // given
        Member member = Member.builder().name("kim").build();

        // when
        Long savedId = memberService.join(member);
//        em.flush(); // 영속성 컨텍스트 flush해서 SQL은 실행되지만 롤백된다.
        Member findMember = memberService.findOne(savedId);

        // then
        assertEquals(member, findMember);
    }

    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = Member.builder().name("kim").build();
        Member member2 = Member.builder().name("kim").build();

        // when
        memberService.join(member1);
        Executable executable = () -> memberService.join(member2);

        // then
        IllegalStateException thrown = assertThrows(IllegalStateException.class, executable);
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }
}
