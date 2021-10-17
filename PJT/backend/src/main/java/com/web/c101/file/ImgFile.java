package com.web.c101.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImgFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fid;

    private String file_name;
    private String file_size;

    private long rid;

}