package com.web.c101.file.Response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileResponse {
    List<String> fileNameList;
}
