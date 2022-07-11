package com.ute.bookstoreonlinebe.services.order;

import com.ute.bookstoreonlinebe.dtos.card.CartDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedBookInOrder;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCardListBook;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedPrice;
import com.ute.bookstoreonlinebe.exceptions.InvalidException;
import com.ute.bookstoreonlinebe.exceptions.NotFoundException;
import com.ute.bookstoreonlinebe.repositories.OrderRepository;
import com.ute.bookstoreonlinebe.services.book.BookService;
import com.ute.bookstoreonlinebe.services.mailsender.MailSenderService;
import com.ute.bookstoreonlinebe.services.user.UserService;
import com.ute.bookstoreonlinebe.utils.PageUtils;
import com.ute.bookstoreonlinebe.utils.enums.EnumCurrency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    private UserService userService;

    private BookService bookService;

    private MailSenderService mailSenderService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, BookService bookService, MailSenderService mailSenderService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getOrderPaging(String search, int page, int size, String sort, String column) {
        Pageable pageable = PageUtils.createPageable(page, size, sort, column);
        return orderRepository.getOrderPaging(search, pageable);
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Order có id %s không tồn tại", id)));
    }

    @Override
    public List<Order> getOrderByUserId(String id) {
        return orderRepository.getOrderByUserId(id)
                .orElseThrow(() -> new NotFoundException(String.format("Order có id %s không tồn tại", id)));
    }

    @Override
    public Order createNewOrder(String userID, Principal principal, CartDto dto) {
        User user = userService.checkUserWithIDAndPrincipal(userID, principal);
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        List<EmbeddedBookInOrder> listBookInOrder = new ArrayList<>();
        dto.getCardDetail().forEach(
                e -> {
                    Book book = bookService.getBookById(e.getBookID());
                    System.out.println("Book Id :" + book.toString());
                    EmbeddedBookInOrder embeddedBookInOrder = new EmbeddedBookInOrder();
                    embeddedBookInOrder.setBook(book);
                    embeddedBookInOrder.setQuantity(e.getQuantity());
                    embeddedBookInOrder.setTotal(book.getPrice().getPrice() * e.getQuantity());
                    listBookInOrder.add(embeddedBookInOrder);
                });
        order.setBooksInOrder(listBookInOrder);
        float subTotal = 0.0F;
        for (EmbeddedBookInOrder embeddedBookInOrder : listBookInOrder) {
            subTotal += embeddedBookInOrder.getTotal();
        }
        order.setSubtotal(new EmbeddedPrice(subTotal, EnumCurrency.vnd));
        order.setAddress(user.getAddress());
        order.setPhone(user.getPhone());
        order.setNote("Giao hang nhanh");
        order.setStatus(true);
        order.setPay(false);
        order.setShipping(false);
        order.setDelivered(false);
        orderRepository.save(order);
        mailSenderService.sendMailNewOrder(user, order);
        return order;
    }

    @Override
    public Order disableOrder(String userID, Principal principal) {
        return null;
    }

    @Override
    public Order callOffOrder(String userID, String orderID, Principal principal) {
        User user = userService.checkUserWithIDAndPrincipal(userID, principal);
        Order order = getOrderById(orderID);
        if (!order.getUser().getId().equals(user.getId())) {
            throw new InvalidException(
                    String.format("Order có id %s không thuộc về user có id %s", orderID, userID));
        }
        if (order.isStatus() && order.isShipping()) {
            throw new InvalidException(
                    String.format("Không thể huy đơn hàng có id %s, vì đơn hàng đã được giao.", orderID));
        }
        order.setStatus(false);
        orderRepository.save(order);
        mailSenderService.sendMailCallOffOrder(user, order);
        return order;
    }

    @Override
    public Order changeStatusOrder(String id) {
        Order order = getOrderById(id);
        order.setStatus(!order.isStatus());
        return orderRepository.save(order);
    }

    @Override
    public Order changeStatusPayOfOrder(String id) {
        return null;
    }

    @Override
    public Order changeShippingOrder(String id) {
        Order order = getOrderById(id);
        order.setShipping(!order.isShipping());
        return orderRepository.save(order);
    }

    @Override
    public Order changeDeliveredOrder(String id) {
        Order order = getOrderById(id);
        order.setDelivered(!order.isDelivered());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getListOrderByUserIdWithIf(String id, int status, Principal principal) {
        User user = userService.getUser(principal);
        if (!user.getRoles().stream().anyMatch(a -> a.equalsIgnoreCase("ROLE_ADMIN"))) {

            if (!id.equals(user.getId())) {
                throw new InvalidException(String.format("Token không đến từ người dùng có id %s", id));
            }

        }

        switch (status) {
            case 1:
                // status = true, shipping = false
                return orderRepository.getListOrderByUserIdWithIf(id, true, false, false);
            case 2:
                // status = true, shipping = true, delivered = false
                return orderRepository.getListOrderByUserIdWithIf(id, true, true, false);
            case 3:
                // hoan thanh
                // status = true, shipping = true, delivered = true
                return orderRepository.getListOrderByUserIdWithIf(id, true, true, true);
            case 4:
                // status = false
                return orderRepository.getListOrderByUserIdWithStatusFalse(id, false);
            default:
                throw new InvalidException(String.format("Status không hợp lệ."));
        }
    }

    @Override
    public List<Order> getOrderSuccessByUserId(String id) {
        return orderRepository.getListOrderByUserIdWithIf(id, true, true, true);
    }

    @Override
    public Order getOrderByIdForPayment(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
