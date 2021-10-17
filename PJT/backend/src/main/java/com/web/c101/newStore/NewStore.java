package com.web.c101.newStore;


import lombok.*;

import javax.persistence.*;

@Entity(name = "store")
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;

    private String name;
    private String area;
}
