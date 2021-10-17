package com.web.c101.member;

import com.web.c101.member.security.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @Column(name = "mid")
    private long id;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "flag")
    private boolean flag;

    @Enumerated(EnumType.STRING)
    private Authority authority;
}
