package com.web.c101.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberDao extends JpaRepository<Member, Long> {
    Optional<Member> findMemberById(long memberId);
    boolean existsByNickname(String nickname);
}
