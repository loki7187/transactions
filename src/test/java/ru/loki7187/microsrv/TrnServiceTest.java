//package ru.loki7187.microsrv;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//import ru.loki7187.microsrv.trnService.service.TrnService;
//
//import static ru.loki7187.microsrv.globalconfig.Constants.*;
//
//@SpringBootTest
//@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//public class TrnServiceTest {
//
//    @Autowired
//    TrnService trnService;
//
//    @Test
//    public void testIncreaseCardRestPositive () {
//        Assertions.assertEquals(200L, trnService.increaseCardRest(1L, 100L).getSecond().getRest());
//    }
//
//    @Test
//    public void testDecreaseCardRestPositive () {
//        Assertions.assertEquals(200L, trnService.decreaseCardRest(3L, 100L).getSecond().getRest());
//    }
//
//    @Test
//    public void testDecreaseCardRestNegative1 () {
//        Assertions.assertEquals(notEnoughtMoney, trnService.decreaseCardRest(1L, 200L).getFirst());
//    }
//
//    @Test
//    public void testDecreaseCardRestNegative2 () {
//        Assertions.assertEquals(cardNotFound, trnService.decreaseCardRest(6L, 200L).getFirst());
//    }
//
//    @Test
//    public void testTransactionPositive () {
//        Assertions.assertEquals(success, trnService.makeTransactionCardToCard(2L, 200L, 4L).getLeft());
//    }
//
//    @Test
//    public void testTransactionNegative1 () {
//        Assertions.assertEquals(notEnoughtMoney, trnService.makeTransactionCardToCard(1L, 2000L, 4L).getLeft());
//    }
//
//    @Test
//    public void testTransactionNegative2 () {
//        Assertions.assertEquals(cardNotFound, trnService.makeTransactionCardToCard(7L, 2000L, 4L).getLeft());
//    }
//}
