package com.web.c101.search;


import com.web.c101.BasicResponse;
import com.web.c101.keyword.KeywordService;
import com.web.c101.util.ElasticUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class SearchController {

    ElasticUtil elastic = ElasticUtil.getInstance();

    @Autowired
    private KeywordService keywordService;

    @GetMapping("/search")
    @ApiOperation(value = "검색하기")
    public Object searchStore(@RequestParam String name) {

        log.info("검색하기");
        BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        Map<String, Object> query = new HashMap<>();
        query.put("name", name);

        Map<String, SortOrder> sort = new HashMap<>();

        List<Map<String, Object>> list = elastic.ESSearch("msg", query, sort);

        if(list.size() > 0){
            result.status = true;
            result.data = "success";
        }
        result.object = list;

        return result;

    }


    @PutMapping("/search/updateCnt")
    @ApiOperation(value = "언급량 최신화")
    public Object updateCnt(@RequestBody StoreDto storeDto) {
        log.info("검색어 언급량 최신화");
        BasicResponse result = new BasicResponse();
        result.status = false;
        result.data = "fail";

        if(elastic.updateCnt("realtime", storeDto)){
            result.status = true;
            result.data = "success";
        }

        return result;

    }

    @GetMapping("/search/keyword")
    @ApiOperation(value = "키워드 분석")
    public Object getKeywords(@RequestParam String name, @RequestParam String area){
        String store = area + name.replace(" ", "_");
        log.info("키워드 분석 :" + store);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = keywordService.getKeywords(store);

        return result;
    }

}
