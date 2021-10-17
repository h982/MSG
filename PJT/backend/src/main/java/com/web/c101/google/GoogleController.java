package com.web.c101.google;

import com.web.c101.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/google")
public class GoogleController {

    GoogleService service;

    // 리뷰 작성
    @GetMapping("/score")
    @ApiOperation(value = "구글 평균 평점")
    public Object getScore(@RequestParam String store){

        log.info("구글 평점");
        final BasicResponse result = new BasicResponse();

        result.object = service.getScore(store);
        result.status = true;
        result.data = "success";

        return result;
    }

    @GetMapping("/review")
    @ApiOperation(value = "구글 리뷰 가져오기")
    public Object getReviews(@RequestParam String store){

        log.info("구글 상점");
        final BasicResponse result = new BasicResponse();

        result.object = service.getReviews(store);
        result.status = true;
        result.data = "success";

        return result;
    }
}
