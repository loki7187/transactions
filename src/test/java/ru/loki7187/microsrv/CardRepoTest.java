package ru.loki7187.microsrv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.loki7187.microsrv.cardtransactions.dao.CardRepo;
import ru.loki7187.microsrv.cardtransactions.entity.CardEntity;

@SpringBootTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CardRepoTest {

    @Autowired
    CardRepo cardRepo;

    @Test
    public void testSelectAll (){
        Assertions.assertEquals(5 ,cardRepo.count());
    }

    @Test
    public void testSelectById (){
        Assertions.assertEquals(100L, cardRepo.findById(1L).orElse(new CardEntity(0L, 0L)).getRest());
    }

    @Test
    public void testSelectByIdNotFound (){
        Assertions.assertFalse(cardRepo.findById(6L).isPresent());
    }
}
