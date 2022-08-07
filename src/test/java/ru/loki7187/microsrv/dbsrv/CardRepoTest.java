package ru.loki7187.microsrv.dbsrv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.loki7187.microsrv.dbservice.dao.CardRepo;
import ru.loki7187.microsrv.dbservice.dao.CardRepo1;
import ru.loki7187.microsrv.dbservice.entity.CardEntity;
import ru.loki7187.microsrv.dbservice.entity.CardEntity1;

@SpringBootTest
@Sql(scripts = "/dataCard.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CardRepoTest {

    @Autowired
    CardRepo cardRepo;

    @Autowired
    CardRepo1 cardRepo1;

    @Test
    public void testSelectAll (){
        Assertions.assertEquals(5 ,cardRepo.count());
    }

    @Test
    public void testSelectAll1 (){
        Assertions.assertEquals(5 ,cardRepo1.count());
    }

    @Test
    public void testSelectByIdPositive (){
        Assertions.assertEquals(200L, cardRepo.findById(2L).orElse(new CardEntity(0L, 0L)).getRest());
    }

    @Test
    public void testSelectByIdPositive1 (){
        Assertions.assertEquals(200L, cardRepo1.findById(1L).orElse(new CardEntity1(0L, 0L)).getRest());
    }

    @Test
    public void testSelectByIdNegative (){
        Assertions.assertFalse(cardRepo.findById(1L).isPresent());
    }

    @Test
    public void testSelectByIdNegative1 (){
        Assertions.assertFalse(cardRepo1.findById(2L).isPresent());
    }
}
