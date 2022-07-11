package com.ute.bookstoreonlinebe.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class Statistic {
    private List<StatisticInDay> days = new ArrayList<>();
}
