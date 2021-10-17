package com.web.c101.file;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImgFileDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    private String file_name;
    private String file_size;

    private long rid;
    MultipartFile[] files;//임시

}