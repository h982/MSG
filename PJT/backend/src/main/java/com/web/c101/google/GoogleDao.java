package com.web.c101.google;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoogleDao extends JpaRepository<Google, Long> {
    Google findTop1ByGoogleKeyword(String googleKeyword);
    List<Google> findGoogleByGoogleKeyword(String googleKeyword);
}
