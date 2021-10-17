package com.web.c101.newStore;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewStoreService {

    NewStoreDao newStoreDao;

    public boolean addNewStore(NewStoreDto newStoreDto){

        NewStore newStore = NewStoreAdaptor.dtoToEntity(newStoreDto);
        System.out.println(newStore);
        try{
            newStoreDao.save(newStore);
        } catch (Exception e){
            return false;
        }

        return true;
    }

}
