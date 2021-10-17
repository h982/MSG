package com.web.c101.search;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    String name;
    String area;
    String address;
    float latitude;
    float longitude;
}
