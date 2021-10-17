package com.web.c101.realtime;

import com.web.c101.BasicResponse;
import com.web.c101.util.ElasticUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class realtimeController {

    ElasticUtil elasticUtil = ElasticUtil.getInstance();

    @GetMapping("/realtime")
    @ApiOperation(value = "실시간 순위")
    public Object getRealtime(){

        log.info("실시간 검색어 순위");
        BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        Map<String,Object> query = new HashMap<>();

        Map<String, SortOrder> sort = new HashMap<>();
        sort.put("cnt", SortOrder.DESC);

        List<Map<String, Object>> list = elasticUtil.ESSearch("realtime", query, sort);

        if(list.size() > 0){
            result.status = true;
            result.data = "success";
        }
        result.object = list;

        return result;



    }

}
