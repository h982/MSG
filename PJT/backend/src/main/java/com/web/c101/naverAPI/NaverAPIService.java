package com.web.c101.naverAPI;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NaverAPIService {
    public List<Integer> getSearchAmount(String keyword) {
        List<Integer> result = new ArrayList<>();
        String flaskUrl = "http://j5c101.p.ssafy.io:5000/searchAPI?keyword=" + keyword;
//        String flaskUrl = "http://localhost:5000/searchAPI?keyword=" + keyword;
        try{
            HttpURLConnection conn = (HttpURLConnection) new URL(flaskUrl).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String res = br.readLine();
            res = res.substring(1,res.length()-1);
            String[] str = res.split(",");
            if(str.length < 30)
                return result; //빈 거 리턴
            for(int i = 0; i< str.length; i++)
                result.add(Integer.parseInt(str[i]));
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
