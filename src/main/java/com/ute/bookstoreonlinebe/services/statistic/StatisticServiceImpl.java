package com.ute.bookstoreonlinebe.services.statistic;

import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.exceptions.InvalidException;
import com.ute.bookstoreonlinebe.models.Statistic;
import com.ute.bookstoreonlinebe.models.StatisticInDay;
import com.ute.bookstoreonlinebe.repositories.OrderRepository;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Slf4j
@Service
public class StatisticServiceImpl implements StatisticService{
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
    @Autowired
    private MongoOperations mongoOperations;

    private OrderService orderService;

    private OrderRepository orderRepository;

    public StatisticServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Statistic getTurnoverMonPresent() {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate")
                .gte(startOfMonth())
                .lt(endOfMonth())
        );

        List<Order> orders = mongoOperations.find( query, Order.class);
        Map<String, List<Order>> mapOrder = mapOrder(orders);
        Statistic statistic = new Statistic();
        for (Map.Entry<String, List<Order>> entry : mapOrder.entrySet()){
            StatisticInDay statisticInDay = new StatisticInDay();
            statisticInDay.setDay(entry.getKey());
            statisticInDay.setSumbills(entry.getValue().stream().count());
            entry.getValue().stream().forEach(e ->{
                 statisticInDay.setSummonny(
                         statisticInDay.getSummonny() + e.getSubtotal().getPrice()
                 );
            });

            List<StatisticInDay> days = statistic.getDays();
            days.add(statisticInDay);
            statistic.setDays(days);
        }
        return statistic;
    }

    @Override
    public Statistic getTurnoverWeekPresent() {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate")
                .gt(startOfWeek())
                .lt(endOfWeek())
        );
        List<Order> orders = mongoOperations.find(query, Order.class);

        Map<String, List<Order>> mapOrder = mapOrder(orders);
        Statistic statistic = new Statistic();
        for (Map.Entry<String, List<Order>> entry : mapOrder.entrySet()){
            StatisticInDay statisticInDay = new StatisticInDay();
            statisticInDay.setDay(entry.getKey());
            statisticInDay.setSumbills(entry.getValue().stream().count());
            entry.getValue().stream().forEach(e ->{
                statisticInDay.setSummonny(
                        statisticInDay.getSummonny() + e.getSubtotal().getPrice()
                );
            });

            List<StatisticInDay> days = statistic.getDays();
            days.add(statisticInDay);
            statistic.setDays(days);
        }
        return statistic;
    }

    @Override
    public StatisticInDay getTurnoverToDay() {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate")
                .gt(startOfDay())
                .lt(endOfDay())
        );
        List<Order> orders = mongoOperations.find(query, Order.class);
        System.out.println(startOfDay());
        StatisticInDay statisticInDay = new StatisticInDay();
        statisticInDay.setDay(
                startOfDay().toString().replace(
                        startOfDay().toString().substring(startOfDay().toString().lastIndexOf("T"))
                        , "")
                );
        statisticInDay.setSumbills(orders.size());
        orders.forEach(e -> statisticInDay.setSummonny(
                statisticInDay.getSummonny() + e.getSubtotal().getPrice()
                )
        );
        return statisticInDay;
    }

    @Override
    public StatisticInDay getTurnoverAnyDay(int day, int month, int year) {
        if (String.valueOf(day).replace(" ", "").isEmpty()
                || String.valueOf(month).replace(" ", "").isEmpty()
                || String.valueOf(year).replace(" ", "").isEmpty()){
            throw new InvalidException("Thông tin truy vấn không được null.");
        }
        LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, month, day, 23, 59, 59);
        System.out.println(start);
        System.out.println(end);
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate")
                .gt(start)
                .lt(end)
        );

        List<Order> orders = mongoOperations.find(query, Order.class);
        orders.forEach(e -> System.out.println(e.toString()));
        StatisticInDay statisticInDay = new StatisticInDay();
        statisticInDay.setDay(year+"/"+month+"/"+day);
        statisticInDay.setSumbills(orders.size());
        orders.forEach(e -> statisticInDay.setSummonny(
                        statisticInDay.getSummonny() + e.getSubtotal().getPrice()
                )
        );
        return statisticInDay;
    }

    @Override
    public Statistic getTurnoverAnyMonth(int month, int year) {
        if (String.valueOf(month).replace(" ", "").isEmpty()
                || String.valueOf(year).replace(" ", "").isEmpty()){
            throw new InvalidException("Thông tin truy vấn không được null.");
        }
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0, 0);
        int day = 1;
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                day = 31;
                break;
            case 2:
                day = year % 2 == 0 ? 28:29;
                break;
            case 4: case 6: case 9: case 11:
                day = 29;
                break;
            default:
                System.out.println("Error");
                break;
        }

        LocalDateTime end = LocalDateTime.of(year, month, day, 23, 59, 59);
        System.out.println(start);
        System.out.println(end);
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate")
                .gt(start)
                .lt(end)
        );

        List<Order> orders = mongoOperations.find( query, Order.class);
        Map<String, List<Order>> mapOrder = mapOrder(orders);
        Statistic statistic = new Statistic();
        for (Map.Entry<String, List<Order>> entry : mapOrder.entrySet()){
            StatisticInDay statisticInDay = new StatisticInDay();
            statisticInDay.setDay(entry.getKey());
            statisticInDay.setSumbills(entry.getValue().stream().count());
            entry.getValue().stream().forEach(e ->{
                statisticInDay.setSummonny(
                        statisticInDay.getSummonny() + e.getSubtotal().getPrice()
                );
            });

            List<StatisticInDay> days = statistic.getDays();
            days.add(statisticInDay);
            statistic.setDays(days);
        }
        return statistic;
    }

    @Override
    public Statistic getTurnoverInSevenDay() {
        System.out.println("day: " + toString(endOfDay()));
        System.out.println("seven days ago: " + toString(startOfDay().minusDays(7)));
        Query query = new Query();
        query.addCriteria(Criteria.where("orderDate")
                .gt(startOfDay().minusDays(7))
                .lt(endOfDay())
        );
        List<Order> orders = mongoOperations.find(query, Order.class);

        Map<String, List<Order>> mapOrder = mapOrder(orders);
        Statistic statistic = new Statistic();
        for (Map.Entry<String, List<Order>> entry : mapOrder.entrySet()){
            StatisticInDay statisticInDay = new StatisticInDay();
            statisticInDay.setDay(entry.getKey());
            statisticInDay.setSumbills(entry.getValue().stream().count());
            entry.getValue().stream().forEach(e ->{
                statisticInDay.setSummonny(
                        statisticInDay.getSummonny() + e.getSubtotal().getPrice()
                );
            });

            List<StatisticInDay> days = statistic.getDays();
            days.add(statisticInDay);
            statistic.setDays(days);
        }
        return statistic;
    }

    public Map<String, List<Order>> mapOrder(List<Order> orders){
        SimpleDateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<Order>> map = new HashMap<String, List<Order>>();
        orders.forEach(e -> {
            try {
                Date date = format.parse(e.getOrderDate().toString());
                String dateFormat = DateFormat.getInstance().format(date);
                dateFormat = dateFormat.replace(
                        dateFormat.substring(dateFormat.lastIndexOf(":") - 2), "");
                boolean flag = false;
                for (Map.Entry<String, List<Order>> entry : map.entrySet()){
                    if (entry.getKey().equals(dateFormat)){
                        entry.setValue(entry.getValue()).add(e);
                        flag = true;
                    }
                }
                if (!flag){
                    List<Order> ord = new ArrayList<>();
                    ord.add(e);
                    map.put(dateFormat, ord);
                }
            } catch (Exception exception){
                exception.printStackTrace();
            }
        });
        return map;
    }

    public Date convertFrom(LocalDateTime localDate){
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            date = format.parse(localDate.toString());

        }catch (ParseException e){
        }
        return date;
    }

    public static LocalDateTime startOfDay() {
        return LocalDateTime.now(DEFAULT_ZONE_ID).with(LocalTime.MIN);
    }

    public static LocalDateTime endOfDay() {
        return LocalDateTime.now(DEFAULT_ZONE_ID).with(LocalTime.MAX);
    }
    public static LocalDateTime startOfWeek() {
        return LocalDateTime.now(DEFAULT_ZONE_ID)
                .with(LocalTime.MIN)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    //note that week ends with Sunday
    public static LocalDateTime endOfWeek() {
        return LocalDateTime.now(DEFAULT_ZONE_ID)
                .with(LocalTime.MAX)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }
    public static LocalDateTime startOfMonth() {
        return LocalDateTime.now(DEFAULT_ZONE_ID)
                .with(LocalTime.MIN)
                .with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime endOfMonth() {
        return LocalDateTime.now(DEFAULT_ZONE_ID)
                .with(LocalTime.MAX)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    public static String toString(final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
