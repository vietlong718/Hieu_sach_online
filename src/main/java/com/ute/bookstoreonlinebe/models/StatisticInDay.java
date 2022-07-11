package com.ute.bookstoreonlinebe.models;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class StatisticInDay {
    private String day;
    private long sumbills = 0;
    private float summonny = 0;
}
