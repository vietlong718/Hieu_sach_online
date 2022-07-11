package com.ute.bookstoreonlinebe.controllers;

import com.ute.bookstoreonlinebe.dtos.card.CartDto;
import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "test")
    @GetMapping("/test")
    public ResponseEntity<String> testOrder(){
        return new ResponseEntity<>("test order", HttpStatus.OK);
    }
    @ApiOperation(value = "Get tất cả Order không phân trang cho admin")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrder(){
        return new ResponseEntity<>(orderService.getAllOrder(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list Order có phân trang cho admin")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paging")
    public ResponseEntity<Page<Order>> getAllOrder(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "${paging.default.page}") int page,
            @RequestParam(name = "size", required = false, defaultValue = "${paging.default.size}") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(name = "column", required = false, defaultValue = "orderDate") String column){
        return new ResponseEntity<>(
                orderService.getOrderPaging(search,page,size,sort,column), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list order by userId")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping("/{id}")
    public ResponseEntity<List<Order>> getOrderByUserID(
            @PathVariable(value = "id") String id){
        return new ResponseEntity<>(orderService.getOrderByUserId(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list order success by userId")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping("/success/{id}")
    public ResponseEntity<List<Order>> getOrderSuccessByUserID(
            @PathVariable(value = "id") String id){
        return new ResponseEntity<>(orderService.getOrderSuccessByUserId(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Get list order by userId và statusId" +
            "\n(Với statusId là 1 2 3 4 tương đương: \n" +
            "1 = Chưa xác nhận ;\n" +
            "2 = Đã xác nhận ;\n" +
            "3 = Hoàn thành ; \n" +
            "4 = Hủy)")
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping
    public ResponseEntity<List<Order>> getOrderByUserIDAndStatus(
            @RequestParam(value = "userId", required = true) String id,
            @RequestParam(value = "statusId", required = true) int status,
            Principal principal){
        return new ResponseEntity<>(orderService.getListOrderByUserIdWithIf(id, status, principal), HttpStatus.OK);
    }

    @ApiOperation(value = "User tạo đơn đặt hàng")
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{id}")
    public ResponseEntity<Order> createNewOrder(
            @PathVariable(value = "id") String id, Principal principal,@RequestBody CartDto dto){
        System.out.println(id);
        return new ResponseEntity<>(orderService.createNewOrder(id, principal, dto), HttpStatus.OK);
    }

    @ApiOperation(value = "User hủy đơn hàng(nếu đơn hàng chưa được xử lý)")
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{userID}/{orderID}")
    public ResponseEntity<Order> callOffOrder(
            @PathVariable(value = "userID") String userID,
            @PathVariable(value = "orderID") String orderID,
            Principal principal){
        return new ResponseEntity<>(orderService.callOffOrder(userID, orderID, principal), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trang thái đơn hàng")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Order> changeStatusOrder(@PathVariable(value = "id") String id){
        return new ResponseEntity<>(orderService.changeStatusOrder(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trang thái thanh toán")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<Order> changeStatusPayOfOrder(@PathVariable String id){
        return new ResponseEntity<>(orderService.changeStatusPayOfOrder(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trang thái vẫn chuyển hàng(shipping)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/shipping/{id}")
    public ResponseEntity<Order> changeShippingOrder(@PathVariable String id){
        return new ResponseEntity<>(orderService.changeShippingOrder(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Admin thay đổi trang thái đã giao hàng(delivered)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/delivered/{id}")
    public ResponseEntity<Order> changeDeliveredOrder(@PathVariable String id){
        return new ResponseEntity<>(orderService.changeDeliveredOrder(id), HttpStatus.OK);
    }

}
