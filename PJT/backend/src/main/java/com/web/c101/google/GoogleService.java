package com.web.c101.google;

import com.web.c101.error.CustomException;
import com.web.c101.error.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GoogleService {

    GoogleDao googledao;

    public float getScore(String store) {
        float score = 0f;

        try {
            Google temp = googledao.findTop1ByGoogleKeyword(store);
            if(temp != null){
                score = temp.getGoogleStarAvg();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ERROR);
        }

        return score;
    }

    public List<GoogleDto> getReviews(String store) {

        List<GoogleDto> googleReviews = new ArrayList<>();

        try {
            List<Google> re = googledao.findGoogleByGoogleKeyword(store);
            for(Google R  : re) {
                GoogleDto tmp = GoogleAdaptor.entityToDto(R);
                googleReviews.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ERROR);
        }

        return googleReviews;
    }

}
