package com.web.c101.naverAPI;

import com.web.c101.BasicResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
public class NaverAPIController {
    private NaverAPIService naverAPIService;

    @GetMapping ("/naverAPI")
    public ResponseEntity<BasicResponse> SearchKeyword(String keyword){
        log.info("Flask Request: " + keyword);
        String query = null;
        try {
            query = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<Integer> list = naverAPIService.getSearchAmount(query);

        final BasicResponse result = new BasicResponse();
        if( list.size() == 0){
            for(int i =0; i<30; i++)
                list.add(0);
        }
        result.status = true;
        result.data = "Success";
        result.object = list;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
