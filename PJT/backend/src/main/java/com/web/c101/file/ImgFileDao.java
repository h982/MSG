package com.web.c101.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgFileDao extends JpaRepository<ImgFile, String> {
    List<ImgFile> findFileByRid(long rid);
}
