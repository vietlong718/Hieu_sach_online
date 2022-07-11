package com.ute.bookstoreonlinebe.config;

import com.ute.bookstoreonlinebe.dtos.user.UserDto;
import com.ute.bookstoreonlinebe.entities.Book;
import com.ute.bookstoreonlinebe.entities.Category;
import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedCategory;
import com.ute.bookstoreonlinebe.models.Statistic;
import com.ute.bookstoreonlinebe.repositories.UserRepository;
import com.ute.bookstoreonlinebe.services.book.BookService;
import com.ute.bookstoreonlinebe.services.category.CategoryService;
import com.ute.bookstoreonlinebe.services.mailsender.MailSenderService;
import com.ute.bookstoreonlinebe.services.order.OrderService;
import com.ute.bookstoreonlinebe.services.statistic.StatisticService;
import com.ute.bookstoreonlinebe.services.user.UserService;
import com.ute.bookstoreonlinebe.utils.enums.EnumGender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Profile("dev")
public class DataSeedingListerner implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Value("${default.password}")
    private String defaultPassword;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            UserDto userDto = new UserDto("zerodev247@gmail.com", passwordEncoder.encode(defaultPassword),
                    "admin",
                    new SimpleDateFormat("MM/dd/yyyy").parse(("4/20/1997")),
                    EnumGender.Male, "thu duc, tp.HCM",
                    "0989542812");
            userService.createAdmin(userDto);

        }
    }
}
