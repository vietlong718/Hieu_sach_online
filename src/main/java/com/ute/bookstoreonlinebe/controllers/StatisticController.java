package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.models.Statistic;
import com.ute.bookstoreonlinebe.models.StatisticInDay;
import com.ute.bookstoreonlinebe.services.book.BookService;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import com.ute.bookstoreonlinebe.services.statistic.StatisticService;
import com.ute.bookstoreonlinebe.services.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/statistic")
public class StatisticController {
    private UserService userService;
    private BookService bookService;
    private OrderService orderService;

    private StatisticService statisticService;

    public StatisticController(UserService userService, BookService bookService,
                               OrderService orderService, StatisticService statisticService) {
        this.userService = userService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.statisticService = statisticService;
    }

    @ApiOperation(value = "Admin xem danh thu trong tháng hiện tại.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/turnover/present/mon")
    public ResponseEntity<Statistic> turnoverByMon(){
        return new ResponseEntity<>(statisticService.getTurnoverMonPresent() , HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xem danh thu trong tuần hiện tại.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/turnover/present/week")
    public ResponseEntity<Statistic> turnoverByWeek(){
        return new ResponseEntity<>(statisticService.getTurnoverWeekPresent() , HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xem danh thu trong ngày hiện tại.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/turnover/present/today")
    public ResponseEntity<StatisticInDay> turnoverTOday(){
        return new ResponseEntity<>(statisticService.getTurnoverToDay() , HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xem danh thu trong ngày bất kỳ.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/turnover/present/anyday")
    public ResponseEntity<StatisticInDay> turnoverAnyday(
            @RequestParam(value = "day", required = true, defaultValue = "1") int day,
            @RequestParam(value = "month", required = true, defaultValue = "6") int month,
            @RequestParam(value = "year", required = true, defaultValue = "2022") int year
    ){
        return new ResponseEntity<>(statisticService.getTurnoverAnyDay(day, month, year) , HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xem danh thu trong tháng bất kỳ.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/turnover/present/anymonth")
    public ResponseEntity<Statistic> turnoverAnyMonth(
            @RequestParam(value = "month", required = true, defaultValue = "6") int month,
            @RequestParam(value = "year", required = true, defaultValue = "2022") int year
    ){
        return new ResponseEntity<>(statisticService.getTurnoverAnyMonth(month, year) , HttpStatus.OK);
    }

    @ApiOperation(value = "Admin xem danh thu trong 7 ngay gan nhat.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/turnover/present/sevenday")
    public ResponseEntity<Statistic> turnoverInSevenDay(
    ){
        return new ResponseEntity<>(statisticService.getTurnoverInSevenDay() , HttpStatus.OK);
    }
}
