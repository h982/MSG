package com.web.c101.newStore;

import com.web.c101.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
public class NewStoreController {

    NewStoreService newStoreService;

    @PostMapping("/newStore/add")
    @ApiOperation(value = "맛집 추가")
    public Object addNewStore(@RequestBody NewStoreDto newStoreDto){
        System.out.println(newStoreDto);
        log.info("맛집 추가");
        final BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        if(newStoreService.addNewStore(newStoreDto)){
            result.status = true;
            result.data = "success";
        }

        return result;
    }

}
