package ru.loki7187.microsrv.dbservice.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import ru.loki7187.microsrv.dbservice.dao.CardRepo;
import ru.loki7187.microsrv.dbservice.dao.CardRepo1;
import ru.loki7187.microsrv.dbservice.entity.CardEntity;
import ru.loki7187.microsrv.dbservice.entity.CardEntity1;
import ru.loki7187.microsrv.globalDto.CardDto;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

public class RepoMaster {

    @Autowired
    CardRepo cr;

    @Autowired
    CardRepo1 cr1;

    @Transactional
    public Pair<String, CardDto> increaseCardRest(Long num, Long sum) {
        CardDto cardDto = new CardDto(0L, 0L);
        var res = success;
        switch ((int) (num % 2)){
            case 0: {
                var cardOpt = cr.findById(num);
                if (cardOpt.isPresent()) {
                    var card = cardOpt.get();
                    card.setRest(card.getRest() + sum);
                    cr.save(card);
                    cardDto = card.getCardDtoView();
                } else {
                    var card = new CardEntity(num, sum);
                    cr.save(card);
                    cardDto = card.getCardDtoView();
                }
            } break;
            case 1: {
                var cardOpt = cr1.findById(num);
                if (cardOpt.isPresent()) {
                    var card = cardOpt.get();
                    card.setRest(card.getRest() + sum);
                    cr1.save(card);
                    cardDto = card.getCardDtoView();
                } else {
                    var card = new CardEntity1(num, sum);
                    cr1.save(card);
                    cardDto = card.getCardDtoView();
                }
            } break;
        }
        return Pair.of(res, cardDto);
    }

    @Transactional
    public Pair<String, CardDto> decreaseCardRest(Long num, Long sum) {
        CardDto cardDto = new CardDto(0L, 0L);
        var res = success;
        switch ((int) (num % 2)){
            case 0: {
                var cardOpt = cr.findById(num);
                if (cardOpt.isPresent()) {
                    var card = cardOpt.get();
                    if (card.getRest() >= sum){
                        card.setRest(card.getRest() - sum);
                        cr.save(card);
                        cardDto = card.getCardDtoView();
                    }
                    else {
                        res = notEnoughtMoney;
                    }
                } else {
                    res = cardNotFound;
                }
            } break;
            case 1: {
                var cardOpt = cr1.findById(num);
                if (cardOpt.isPresent()) {
                    var card = cardOpt.get();
                    if (card.getRest() >= sum){
                        card.setRest(card.getRest() - sum);
                        cr1.save(card);
                        cardDto = card.getCardDtoView();
                    }
                    else {
                        res = notEnoughtMoney;
                    }
                } else {
                    res = cardNotFound;
                }
            } break;
        }
        return Pair.of(res, cardDto);
    }

}
