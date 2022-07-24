package ru.loki7187.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.loki7187.app.service.CardService;

import static ru.loki7187.app.globalconfig.Constants.*;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CardServiceTest {

    @Autowired
    CardService cardService;

    @Test
    public void testIncreaseCardRestPositive () {
        Assertions.assertEquals(200L, cardService.increaseCardRest(1L, 100L).getSecond().getRest());
    }

    @Test
    public void testDecreaseCardRestPositive () {
        Assertions.assertEquals(200L, cardService.decreaseCardRest(3L, 100L).getSecond().getRest());
    }

    @Test
    public void testDecreaseCardRestNegative1 () {
        Assertions.assertEquals(notEnoughtMoney, cardService.decreaseCardRest(1L, 200L).getFirst());
    }

    @Test
    public void testDecreaseCardRestNegative2 () {
        Assertions.assertEquals(cardNotFound, cardService.decreaseCardRest(6L, 200L).getFirst());
    }

    @Test
    public void testTransactionPositive () {
        Assertions.assertEquals(success, cardService.makeTransactionCardToCard(2L, 200L, 4L).getLeft());
    }

    @Test
    public void testTransactionNegative1 () {
        Assertions.assertEquals(notEnoughtMoney, cardService.makeTransactionCardToCard(1L, 2000L, 4L).getLeft());
    }

    @Test
    public void testTransactionNegative2 () {
        Assertions.assertEquals(cardNotFound, cardService.makeTransactionCardToCard(7L, 2000L, 4L).getLeft());
    }
}
