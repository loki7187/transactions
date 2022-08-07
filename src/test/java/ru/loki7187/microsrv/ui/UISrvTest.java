//package ru.loki7187.microsrv.ui;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.web.context.request.async.DeferredResult;
//import ru.loki7187.microsrv.dbservice.dao.CardRepo;
//import ru.loki7187.microsrv.dbservice.dao.CardRepo1;
//import ru.loki7187.microsrv.dbservice.entity.CardEntity;
//import ru.loki7187.microsrv.dbservice.entity.CardEntity1;
//import ru.loki7187.microsrv.globalDto.common.CardDto;
//import ru.loki7187.microsrv.globalDto.common.TransactionDto;
//import ru.loki7187.microsrv.uicontroller.service.UIService;
//
//import static ru.loki7187.microsrv.globalconfig.Constants.*;
//
//@SpringBootTest
//@Sql(scripts = "/dataCard.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//public class UISrvTest {
//
//    @Autowired
//    UIService uiService;
//
//    @Autowired
//    CardRepo1 cardRepo1;
//
//    @Autowired
//    CardRepo cardRepo;
//
//    private final Logger logger = LoggerFactory.getLogger(UISrvTest.class);
//
//    @Test
//    public void testIncrease (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.increaseReq(res, new CardDto(1L, 10L));
//        while (!res.hasResult()){} //dada, logika v teste
//        Assertions.assertEquals(210L ,cardRepo1.findById(1L).orElse(new CardEntity1(0L, 0L)).getRest());
//    }
//
//    @Test
//    public void testDecreasePositive (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.decreaseReq(res, new CardDto(2L, 10L));
//        while (!res.hasResult()){} //dada, logika v kode
//        Assertions.assertEquals(190L ,cardRepo.findById(2L).orElse(new CardEntity(0L, 0L)).getRest());
//    }
//
//    @Test
//    public void testDecreaseNegative1 (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.decreaseReq(res, new CardDto(2L, 1000L));
//        while (!res.hasResult()){} //dada, logika v kode
//        Assertions.assertEquals(200L ,cardRepo.findById(2L).orElse(new CardEntity(0L, 0L)).getRest());
//        Assertions.assertEquals(notEnoughtMoney ,res.getResult().toString());
//    }
//
//    @Test
//    public void testDecreaseNegative2 (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.decreaseReq(res, new CardDto(20L, 1000L));
//        while (!res.hasResult()){} //dada, logika v kode
//        Assertions.assertEquals(cardNotFound ,res.getResult().toString());
//    }
//
//    @Test
//    public void testTrnPositive (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.trnCardToCardReq(res, new TransactionDto(3L, 4L, 10L));
//        while (!res.hasResult()){} //dada, logika v kode
//        Assertions.assertEquals(410L ,cardRepo.findById(4L).orElse(new CardEntity(0L, 0L)).getRest());
//        Assertions.assertEquals(390L ,cardRepo1.findById(3L).orElse(new CardEntity1(0L, 0L)).getRest());
//    }
//
//    @Test
//    public void testTrnNegative1 (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.trnCardToCardReq(res, new TransactionDto(3L, 4L, 1000L));
//        while (!res.hasResult()){} //dada, logika v kode
//        Assertions.assertEquals(notEnoughtMoney ,res.getResult().toString());
//    }
//
//    @Test
//    public void testTrnNegative2 (){
//        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
//        uiService.trnCardToCardReq(res, new TransactionDto(30L, 4L, 10L));
//        while (!res.hasResult()){} //dada, logika v kode
//        Assertions.assertEquals(cardNotFound ,res.getResult().toString());
//    }
//}
