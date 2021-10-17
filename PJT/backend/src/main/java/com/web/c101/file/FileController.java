package com.web.c101.file;


import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.web.c101.BasicResponse;
import com.web.c101.file.Response.FileResponse;
import com.web.c101.review.ReviewDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
public class FileController {

    FileService fileService;

    @GetMapping("/img/{fileName}")
    public Object getFile(@PathVariable final String fileName, HttpServletRequest request) throws MalformedURLException{
        Resource resource =  fileService.getFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/file/getImgName")
    public Object getImg(final String rid) {
        final BasicResponse result = new BasicResponse();

        FileResponse fileResponse = fileService.getFileNameList(Long.parseLong(rid));

        if(fileResponse != null) {
            result.status = true;
            result.data = "success";
            result.object = fileResponse;

        }
        else {
            result.status = false;
            result.data = "fail";
        }

        return result;
    }

}