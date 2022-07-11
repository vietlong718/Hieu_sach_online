package com.ute.bookstoreonlinebe;

import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookStoreOnlineBeApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }
}
