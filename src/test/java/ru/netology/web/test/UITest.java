package ru.netology.web.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLSetter;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.VerificationPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UITest {
    LoginPage loginPage = new LoginPage();

    DataHelper.AuthInfo authInfo1 = DataHelper.getAuthInfo("vasya", "qwerty123");
    DataHelper.AuthInfo authInfo2 = DataHelper.getAuthInfo("petya", "123qwerty");

    DataHelper.BadPassword badPassword = DataHelper.getBadPassword("en");
    String dashBoardHeaderText = "  Личный кабинет";


    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void clean() {
        SQLSetter.dropDataBase();
    }

    @Test
    void shouldLogin() {
        loginPage
                .validLogin(authInfo2)
                .verify(authInfo2)
                .shouldBeVisible(dashBoardHeaderText);
    }

}
