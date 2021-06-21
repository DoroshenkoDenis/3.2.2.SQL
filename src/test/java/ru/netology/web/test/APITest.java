package ru.netology.web.test;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLSetter;

import static ru.netology.web.data.DataHelper.getCardTransferInfo;
import static org.hamcrest.Matchers.*;

public class APITest {

    public String shouldLoginAndGetToken() {
        DataHelper.login();
        return DataHelper.verify(SQLSetter.getVerificationCode(DataHelper.getAuthInfo()));
    }

    @Test
    void shouldTransferToOwnCardAndRevers() {
        String token = shouldLoginAndGetToken();
        DataHelper.CardTransferInfo cardTransferInfo = getCardTransferInfo("5559 0000 0000 0001", "5559 0000 0000 0002", "5000");
        String jsonCardData = new Gson().toJson(cardTransferInfo);
        DataHelper.transfer(token, jsonCardData);
        DataHelper.showCards(token)
                .body("[0].balance", equalTo(15000))
                .body("[1].balance", equalTo(5000));
        DataHelper.CardTransferInfo cardTransferInfoForRevers = getCardTransferInfo("5559 0000 0000 0002", "5559 0000 0000 0001", "5000");
        String jsonCardDataForRevers = new Gson().toJson(cardTransferInfoForRevers);
        DataHelper.transfer(token, jsonCardDataForRevers);
        DataHelper.showCards(token)
                .body("[0].balance", equalTo(10000))
                .body("[1].balance", equalTo(10000));
    }

    @Test
    void shouldTransferTOthersCardAndRevers() {
        String token = shouldLoginAndGetToken();
        DataHelper.CardTransferInfo cardTransferInfo = getCardTransferInfo("5559 0000 0000 0001", "5559 0000 0000 7777", "5000");
        String jsonCardData = new Gson().toJson(cardTransferInfo);
        DataHelper.transfer(token, jsonCardData);
        DataHelper.showCards(token)
                .body("[1].balance", equalTo(5000));
        DataHelper.CardTransferInfo cardTransferInfoForRevers = getCardTransferInfo("5559 0000 0000 7777", "5559 0000 0000 0001", "5000");
        String jsonCardDataForRevers = new Gson().toJson(cardTransferInfoForRevers);
        DataHelper.transfer(token, jsonCardDataForRevers);
        DataHelper.showCards(token)
                .body("[1].balance", equalTo(10000));
    }

//    //    Этот тест должен упасть - есть возможность перевести суммы превышающую баланс на карте
//    @Test
//    void shouldNotTransferIfAmountExceeded() {
//        String token = shouldLoginAndGetToken();
//        DataHelper.CardTransferInfo cardTransferInfo = getCardTransferInfo("5559 0000 0000 0001", "5559 0000 0000 7777", "50000");
//        String jsonCardData = new Gson().toJson(cardTransferInfo);
//        DataHelper.transfer(token, jsonCardData);
//        DataHelper.showCards(token)
//                .body("[1].balance", equalTo(10000));
//    }

}
