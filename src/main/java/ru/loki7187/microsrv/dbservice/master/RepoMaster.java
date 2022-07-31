package ru.loki7187.microsrv.dbservice.master;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.loki7187.microsrv.uicontroller.service.UIService;

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

    private final Logger logger = LoggerFactory.getLogger(RepoMaster.class);

    @Transactional
    public String increaseCardRest(CardDto card) {
        var num = card.getNum();
        var sum = card.getSum();
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
        return success;
    }

    @Transactional
    public String decreaseCardRest(CardDto card) {
        var num = card.getNum();
        var sum = card.getSum();
        var res = success;
        switch ((int) (num % 2)){
            case 0: {
                var cardOpt = cr.findById(num);
                if (cardOpt.isPresent()) {
                    var cardEntity = cardOpt.get();
                    if (cardEntity.getRest() >= sum){
                        cardEntity.setRest(cardEntity.getRest() - sum);
                        cr.save(cardEntity);
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
                    var cardEntity = cardOpt.get();
                    if (cardEntity.getRest() >= sum){
                        cardEntity.setRest(cardEntity.getRest() - sum);
                        cr1.save(cardEntity);
                    }
                    else {
                        res = notEnoughtMoney;
                    }
                } else {
                    res = cardNotFound;
                }
            } break;
        }
        return res;
    }

    @JmsListener(destination = stepIncreaseOp, containerFactory = myFactory)
    public void onStepIncreaseRest (StepData data) {
        logger.debug("onStepIncreaseRest");
        var res = increaseCardRest(new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class));
        data.setStepResult(res);
        data.setStepStatus(directOpDone);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepIncreaseOpRevert, containerFactory = myFactory)
    public void onStepIncreaseRestRevert (StepData data) {
        logger.debug("onStepIncreaseRestRevert");
        var res = decreaseCardRest(new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class));
        data.setStepResult(res);
        data.setStepStatus(revertOpDone);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepDecreaseOp, containerFactory = myFactory)
    public void onStepDecreaseRest (StepData data) {
        logger.debug("onStepDecreaseRest");
        var res = decreaseCardRest(new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class));
        data.setStepResult(res);
        data.setStepStatus(directOpDone);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepDecreaseOpRevert, containerFactory = myFactory)
    public void onStepDecreaseRestRevert (StepData data) {
        logger.debug("onStepDecreaseRestRevert");
        var res = increaseCardRest(new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class));
        data.setStepResult(res);
        data.setStepStatus(revertOpDone);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

}
