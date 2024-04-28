package practice.communityservice.repository;

import practice.communityservice.domain.Member;

public interface MemberRepository {
    // 회원 저장
    public Member save(Member member);
}
