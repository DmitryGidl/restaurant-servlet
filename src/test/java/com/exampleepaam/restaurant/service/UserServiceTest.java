package com.exampleepaam.restaurant.service;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.dao.UserDao;
import com.exampleepaam.restaurant.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.testdata.TestData;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Spy
    UserService userService;
    @Mock
    static DaoFactory daoFactory;
    @Mock
    static UserDao userDao;

    static MockedStatic<DaoFactory> daoFactoryDummy;

    @BeforeEach
    void setUp() {
        daoFactoryDummy = Mockito.mockStatic(DaoFactory.class);
        daoFactoryDummy.when(DaoFactory::getInstance).thenReturn(daoFactory);
        when(daoFactory.createUserDao()).thenReturn(userDao);
    }

    @AfterEach
    void close() {
        daoFactoryDummy.close();
    }

    @Test
    void save_success() {
        User user = TestData.getUser1();
        String email = user.getEmail();
        when(userDao.findByEmail(email)).thenReturn(null);
        userService.save(user);
        verify(userDao).save(user);

    }

    @Test
    void findById_success() {
        long id = 3253;
        User expectedUser = TestData.getUser1();
        when(userDao.findById(id)).thenReturn(expectedUser);
        User actualUser = userService.findById(id);
        Assertions.assertEquals(expectedUser, actualUser);

    }

    @Test
    void findByEmail_success() {
        User expectedUser = TestData.getUser1();
        String email = expectedUser.getEmail();
        when(userDao.findByEmail(email)).thenReturn(expectedUser);
        User actualUser = userService.findByEmail(email);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void update_success() {
        User user = TestData.getUser1();
        userService.update(user);
        verify(userDao).update(user);
    }

    @Test
    void addUserBalance_success() {
        User user = TestData.getUser1();
        long id = user.getId();
        when(userDao.findById(id)).thenReturn(user);
        BigDecimal oldBalance = user.getBalanceUAH();
        BigDecimal balanceToAdd = BigDecimal.valueOf(231);
        BigDecimal newBalance = oldBalance.add(balanceToAdd);

        userService.addUserBalance(user, balanceToAdd);

        user.setBalanceUAH(newBalance);
        verify(userDao).update(user);

    }
}
