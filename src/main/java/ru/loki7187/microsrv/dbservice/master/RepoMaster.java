package ru.loki7187.microsrv.dbservice.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.loki7187.microsrv.dbservice.dao.CardRepo;
import ru.loki7187.microsrv.dbservice.dao.CardRepo1;
import ru.loki7187.microsrv.dbservice.entity.CardEntity;
import ru.loki7187.microsrv.dbservice.entity.CardEntity1;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.trnservice.StepData;

import java.util.LinkedHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class RepoMaster {

    @Autowired
    CardRepo cr;

    @Autowired
    CardRepo1 cr1;

    @Autowired
    JmsTemplate jmsTemplate;

    @Transactional
    public String increaseCardRest(CardDto card) {
        var num = card.getNum();
        var sum = card.getSum();
        var res = success;
        switch ((int) (num % 2)){
            case 0: {
                var cardOpt = cr.findById(num);
                if (cardOpt.isPresent()) {
                    var cardEntity = cardOpt.get();
                    cardEntity.setRest(cardEntity.getRest() + sum);
                    cr.save(cardEntity);
                } else {
                    var cardEntity = new CardEntity(num, sum);
                    cr.save(cardEntity);
                }
            } break;
            case 1: {
                var cardOpt = cr1.findById(num);
                if (cardOpt.isPresent()) {
                    var cardEntity = cardOpt.get();
                    cardEntity.setRest(cardEntity.getRest() + sum);
                    cr1.save(cardEntity);
                } else {
                    var cardEntity = new CardEntity1(num, sum);
                    cr1.save(cardEntity);
                }
            } break;
        }
        return res;
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

    @JmsListener(destination = stepIncreaseOp, containerFactory = myFactory)
    public void onStepIncreaseRest (StepData data) {
        System.out.println("onStepIncreaseRest");
        var crd = (LinkedHashMap)data.getStepParams().get(cardParam);
        var card = new CardDto((Long)crd.get("num"), (Long)crd.get("sum"));
        var res = increaseCardRest((CardDto) data.getStepParams().get(cardParam));
        data.setStepStatus(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

}
