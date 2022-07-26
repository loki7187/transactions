package ru.loki7187.microsrv.dbservice.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import ru.loki7187.microsrv.dbservice.dao.CardRepo;
import ru.loki7187.microsrv.dbservice.dao.CardRepo1;
import ru.loki7187.microsrv.globalDto.CardDto;


public class RepoEngine {

    @Autowired
    CardRepo cr;

    @Autowired
    CardRepo1 cr1;

    @Transactional
    public Pair<Boolean, CardDto> increaseCardRest(Long num, Long sum) {
        CardDto cardDto = new CardDto(0L, 0L);
        var res = false;
        switch ((int) (num % 2)){
            case 0: {
                //code for increase cr
            } break;
            case 1: {
                //code for increase cr1
            } break;
        }
        return Pair.of(res, cardDto);
    }

    @Transactional
    public Pair<Boolean, CardDto> decreaseCardRest(Long num, Long sum) {
        CardDto cardDto = new CardDto(0L, 0L);
        var res = false;
        switch ((int) (num % 2)){
            case 0: {
                //code for decrease cr
            } break;
            case 1: {
                //code for decrease cr1
            } break;
        }
        return Pair.of(res, cardDto);
    }

}
