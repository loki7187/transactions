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
import ru.loki7187.microsrv.dbservice.dao.OpHistoryRepo;
import ru.loki7187.microsrv.dbservice.entity.CardEntity;
import ru.loki7187.microsrv.dbservice.entity.CardEntity1;
import ru.loki7187.microsrv.dbservice.entity.OpHistoryEntity;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.trnservice.StepData;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class RepoMaster {

    @Autowired
    CardRepo cr;

    @Autowired
    CardRepo1 cr1;

    @Autowired
    OpHistoryRepo opHistory;

    @Autowired
    JmsTemplate jmsTemplate;

    private final Logger logger = LoggerFactory.getLogger(RepoMaster.class);

    @Transactional
    public String increaseCardRest(StepData data, String direction) {
        var card = new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class);
        var num = card.getNum();
        var sum = card.getSum();
        var id = data.getTrnId();
        var existsOp = checkOpHist(id);

        // повторно операции не делаем
        if (!((direction.equals(directionDirect) && existsOp.getFirst() == true)
            || (direction.equals(directionRevert) && existsOp.getSecond() == true ))) {

            if (direction.equals(directionDirect) && existsOp.getSecond() == false){
                //если всё ок, обратной операции нет, ввыполняем прямую
                switch ((int) (num % 2)) {
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
                    }
                    break;
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
                    }
                    break;
                }
                // запишем операцию в историю в конце
            } else if (direction.equals(directionDirect) && existsOp.getSecond() == true) {
                // если уже есть обратная операция, значит что - то пошло не так, запишем, что прямая тоже была, чтобы не ползать повторно (в конце)
                // , при этом прямая и обратная ничего не сделают
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == false) {
                // если не было прямой, а уже делаем обратную, то ничего не делаем, но делаем об этом запись, чтобы не ползать повторно (в конце)
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == true) {
                //прямая операция была, обратной ещё не было
                switch ((int) (num % 2)) {
                    case 0: {
                        var cardOpt = cr.findById(num);
                        if (cardOpt.isPresent()) {
                            var cardEntity = cardOpt.get();
                            cardEntity.setRest(cardEntity.getRest() - sum);
                            cr.save(cardEntity);
                        }
                    }
                    break;
                    case 1: {
                        var cardOpt = cr1.findById(num);
                        if (cardOpt.isPresent()) {
                            var cardEntity = cardOpt.get();
                            cardEntity.setRest(cardEntity.getRest() - sum);
                            cr1.save(cardEntity);
                        }
                    }
                    break;
                }
            }
            var hist = new OpHistoryEntity(id, direction, num);
            opHistory.save(hist);
        }
        return success;
    }

    @Transactional
    public String decreaseCardRest(StepData data, String direction) {
        var card = new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class);
        var num = card.getNum();
        var sum = card.getSum();
        var id = data.getTrnId();
        var existsOp = checkOpHist(id);
        var res = success;

        // повторно операции не делаем
        if (!((direction.equals(directionDirect) && existsOp.getFirst() == true)
                || (direction.equals(directionRevert) && existsOp.getSecond() == true ))){

            if (direction.equals(directionDirect) && existsOp.getSecond() == false){
                //если всё ок, обратной операции нет, ввыполняем прямую
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
            } else if (direction.equals(directionDirect) && existsOp.getSecond() == true) {
                // если уже есть обратная операция, значит что - то пошло не так, запишем, что прямая тоже была, чтобы не ползать повторно (в конце)
                // , при этом прямая и обратная ничего не сделают
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == false) {
                // если не было прямой, а уже делаем обратную, то ничего не делаем, но делаем об этом запись, чтобы не ползать повторно (в конце)
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == true) {
                //прямая операция была, обратной ещё не было
                if (data.getStepResult().equals(success)) {
                    switch ((int) (num % 2)){
                        case 0: {
                            var cardOpt = cr.findById(num);
                            if (cardOpt.isPresent()) {
                                var cardEntity = cardOpt.get();
                                cardEntity.setRest(cardEntity.getRest() + sum);
                                cr.save(cardEntity);
                            }
                        } break;
                        case 1: {
                            var cardOpt = cr1.findById(num);
                            if (cardOpt.isPresent()) {
                                var cardEntity = cardOpt.get();
                                cardEntity.setRest(cardEntity.getRest() - sum);
                                cr1.save(cardEntity);
                            }
                        } break;
                    }
                } else {
                    //прямая операция прошла с ошибкой, ничего не делаем
                }
            }

            var hist = new OpHistoryEntity(id, direction, num);
            opHistory.save(hist);
        }
        return res;
    }

    @JmsListener(destination = stepIncreaseOp, containerFactory = myFactory)
    public void onStepIncreaseRest (StepData data) {
        logger.debug("onStepIncreaseRest");
        var res = increaseCardRest(data, directionDirect);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepIncreaseOpRevert, containerFactory = myFactory)
    public void onStepIncreaseRestRevert (StepData data) {
        logger.debug("onStepIncreaseRestRevert");
        var res = increaseCardRest(data, directionRevert);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepDecreaseOp, containerFactory = myFactory)
    public void onStepDecreaseRest (StepData data) {
        logger.debug("onStepDecreaseRest");
        var res = decreaseCardRest(data, directionDirect);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepDecreaseOpRevert, containerFactory = myFactory)
    public void onStepDecreaseRestRevert (StepData data) {
        logger.debug("onStepDecreaseRestRevert");
        var res = decreaseCardRest(data, directionRevert);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    private Pair<Boolean, Boolean> checkOpHist(Long id) {
        var existsDirectOp = opHistory.findOpHistoryEntityByTrnIdAndDirection(id, directionDirect).isPresent();
        var existsRevertOp = opHistory.findOpHistoryEntityByTrnIdAndDirection(id, directionRevert).isPresent();
        return Pair.of(existsDirectOp, existsRevertOp);
    }
}
