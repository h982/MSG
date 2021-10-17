package com.web.c101.newStore;

public class NewStoreAdaptor {
    public static NewStoreDto entityToDto(NewStore newStore) {
        return NewStoreDto.builder()
                .name(newStore.getName())
                .area(newStore.getArea())
                .build();
    }

    public static NewStore dtoToEntity(NewStoreDto newStoreDto) {
        return NewStore.builder()
                .name(newStoreDto.getName())
                .area(newStoreDto.getArea())
                .build();
    }
}
