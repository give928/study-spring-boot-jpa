package com.give928.springboot.jpa.service.member;

import com.give928.springboot.jpa.domain.common.Address;
import com.give928.springboot.jpa.domain.member.Member;
import com.give928.springboot.jpa.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(String name, String city, String street, String zipcode) {
        Address address = Address.builder()
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .build();
        Member member = Member.builder()
                .name(name)
                .address(address)
                .build();

        return join(member);
    }

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);

        memberRepository.save(member);

        return member.getId();
    }

    // 멀티 스레드 환경에서 동시에 유효성을 통과할 수 있다. 컬럼에 UK 제약 조건을 권장한다.
    private void validateDuplicateMember(Member member) {
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    @Transactional
    public void update(Long id, String name, String city, String street, String zipcode) {
        Member member = memberRepository.findOne(id);
        member.update(name, city, street, zipcode);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
