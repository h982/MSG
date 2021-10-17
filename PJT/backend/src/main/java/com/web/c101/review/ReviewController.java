package com.web.c101.review;

import com.web.c101.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    ReviewService service;

    // 리뷰 작성
    @PostMapping("/addReview")
    @ApiOperation(value = "리뷰등록")
    public Object addReview(ReviewDto req){
        System.out.println(req.getContent());
        log.info("리뷰 등록");
        final BasicResponse result = new BasicResponse();

        if(service.addReview(req)) {
            result.status = true;
            result.data = "success";
        } else {
            result.status = false;
            result.data = "fail";
        }

        return result;
    }

    // 리뷰 삭제
    @PutMapping("/deleteReview/{rid}")
    @ApiOperation(value = "리뷰삭제")
    public Object delReview(@PathVariable(name = "rid") Long rid) {
        log.info("리뷰 삭제");
        final BasicResponse result = new BasicResponse();

        if(service.delReview(rid)){
            result.status = true;
            result.data = "success";
        } else {
            result.status = false;
            result.data = "fail";
        }

        return result;
    }

    // 사용자가 작성한 리뷰 목록
//    @GetMapping("/review/userReviewList")
//    @ApiOperation(value = "사용자 리뷰 목록")
//    public Object getUserReview(@RequestParam Long mid) {
//
//        log.info("사용자 작성 리뷰 목록");
//        final BasicResponse result = new BasicResponse();
//
//        List<ReviewDto> list = service.getUserReview(mid);
//
//        if(list != null) {
//
//            result.status = true;
//            result.data = "success";
//            result.object = list;
//
//        } else {
//
//            result.status = false;
//            result.data = "fail";
//
//        }
//
//        return result;
//    }

    @GetMapping("/userReviewList/{mid}")
    @ApiOperation(value = "사용자 리뷰 목록")
    public Object getUserReview(@PathVariable("mid") String mid, final Pageable pageable) {
        System.out.println(mid);

        log.info("사용자 작성 리뷰 목록");
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";

        Slice<Review> reviewPages = service.getReviewListByPages(Long.parseLong(mid), pageable);

        if (reviewPages.isEmpty()) {
            result.object = false;
        }
        else {
            result.object = reviewPages;
        }

        return result;
    }

    @GetMapping("/getReviewCnt")
    @ApiOperation(value="현재 멤버가 작성한 리뷰의 개수를 반환")
    public Object getReviewCnt(String mid) {
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";

        long reviewCnt = service.getReviewCnt(Long.parseLong(mid));

        if (reviewCnt == 0) {
            result.object = false;
        }
        else {
            result.object = reviewCnt;
        }

        return result;
    }


    // 사용자가 작성한 리뷰 목록
    // dong이 ""라면 해당 맛집에 달린 모든 리뷰를 반환한다.
    // dong이 ""이 아니라면 dong에 해당하는 지역의 맛집 리뷰를 반환한다.
    @GetMapping("/storeReviewList")
    @ApiOperation(value = "지역에 따른 맛집 리뷰 목록")
    public Object getStoreReview(@RequestParam String dong, @RequestParam String store) {

        log.info("지역에 따른 맛집 리뷰 목록");
        final BasicResponse result = new BasicResponse();

        List<Review> list = service.getStoreReview(dong, store);

        if(list != null) {

            result.status = true;
            result.data = "success";
            result.object = list;

        } else {

            result.status = false;
            result.data = "fail";

        }

        return result;
    }
}
