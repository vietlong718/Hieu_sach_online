package com.ute.bookstoreonlinebe.services.statistic;

import com.ute.bookstoreonlinebe.models.Statistic;
import com.ute.bookstoreonlinebe.models.StatisticInDay;

import javax.xml.crypto.Data;
import java.util.Date;

public interface StatisticService {

    Statistic getTurnoverMonPresent();

    Statistic getTurnoverWeekPresent();

    StatisticInDay getTurnoverToDay();

    StatisticInDay getTurnoverAnyDay(int day, int month, int year);

    Statistic getTurnoverAnyMonth(int month, int year);

    Statistic getTurnoverInSevenDay();
}
