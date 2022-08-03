package ru.loki7187.microsrv.dbservice.master;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
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
import ru.loki7187.microsrv.dbservice.entity.OpHistoryEntityIdClass;
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
    //TODO добавить сохранение результатов шагов в бд

    @Transactional
    public String increaseCardRest(StepData data, String direction) {
        var card = new Gson().fromJson(data.getStepParams().get(cardParam), CardDto.class);
        var id = data.getTrnId();
        var existsOp = checkOpHist(id);

        // повторно операции не делаем
        if (!((direction.equals(directionDirect) && existsOp.getFirst() == true)
            || (direction.equals(directionRevert) && existsOp.getSecond() == true ))) {

            if (direction.equals(directionDirect) && existsOp.getSecond() == false){
                //если всё ок, обратной операции нет, ввыполняем прямую
                switch ((int) (card.getNum() % 2)) {
                    case 0: {
                        cardIncreaseOp(cr, true, card);
                    }
                    break;
                    case 1: {
                        cardIncreaseOp(cr1, true, card);
                    }
                    break;
                }
                // запишем операцию в историю в конце
            } else if (direction.equals(directionDirect) && existsOp.getSecond() == true) {
                // если уже есть обратная операция, значит что - то пошло не так, запишем, что прямая тоже была, чтобы не ползать повторно (в конце)
                // , при этом прямая и обратная ничего не сделают
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == false) {
                // если не было прямой, а уже делаем обратную, то ничего не делаем, но делаем об этом запись (revertOp), чтобы не ползать повторно (в конце)
                //так же, сделаем тут запись о прямой операции
                // тогда при параллельной гонке мы упадём либо в этой транзакции, либо упадёт параллельная транзакция, выполняющая прямую операцию
                opHistory.save(new OpHistoryEntity(id, directionDirect, card.getNum(), err));
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == true) {
                //прямая операция была, обратной ещё не было
                var op = opHistory.findById(new OpHistoryEntityIdClass(id, directionDirect)).get();
                if(op.getOpResult().equals(success)) {
                    switch ((int) (card.getNum() % 2)) {
                        case 0: {
                            cardIncreaseOp(cr, false, card);
                        }
                        break;
                        case 1: {
                            cardIncreaseOp(cr1, false, card);
                        }
                        break;
                    }
                } else {
                    //прямая операция прошла с ошибкой, ничего не делаем
                }
            }
            opHistory.save(new OpHistoryEntity(id, direction, card.getNum(), success));
        }
        return success;
    }

    private <T extends CrudRepository> void cardIncreaseOp (T repo, boolean isIncrease, CardDto card) {

        if (isIncrease) {
            var cardOpt = repo.findById(card.getNum());
            if (cardOpt.isPresent()) {
                var cardEntity = (CardEntity) cardOpt.get();
                cardEntity.setRest(cardEntity.getRest() + card.getSum());
                repo.save(cardEntity);
            } else {
                var cardEntity = new CardEntity(card.getNum(), card.getSum());
                repo.save(cardEntity);
            }
        } else {
            var cardOpt = repo.findById(card.getNum());
            if (cardOpt.isPresent()) {
                var cardEntity = (CardEntity)cardOpt.get();
                cardEntity.setRest(cardEntity.getRest() - card.getSum());
                repo.save(cardEntity);
            }
        }
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
                        res = cardDecreaseOp(cr, true, card);
                    } break;
                    case 1: {
                        res = cardDecreaseOp(cr1, true, card);
                    } break;
                }
            } else if (direction.equals(directionDirect) && existsOp.getSecond() == true) {
                // если уже есть обратная операция, значит что - то пошло не так, запишем, что прямая тоже была, чтобы не ползать повторно (в конце)
                // , при этом прямая и обратная ничего не сделают
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == false) {
                // если не было прямой, а уже делаем обратную, то ничего не делаем, но делаем об этом запись (revertOp), чтобы не ползать повторно (в конце)
                //так же, сделаем тут запись о прямой операции
                // тогда при параллельной гонке мы упадём либо в этой транзакции, либо упадёт параллельная транзакция, выполняющая прямую операцию
                opHistory.save(new OpHistoryEntity(id, directionDirect, card.getNum(), err));
            } else if (direction.equals(directionRevert) && existsOp.getFirst() == true) {
                //прямая операция была, обратной ещё не было
                var op = opHistory.findById(new OpHistoryEntityIdClass(id, directionDirect)).get();
                //результат в data может быть некорректным, т.к. могли прийти из cancel операции, которая не дождалась окончания выполнения какого - то шага
                if (op.getOpResult().equals(success)) {
                    switch ((int) (num % 2)){
                        case 0: {
                            cardDecreaseOp(cr, false, card);
                        } break;
                        case 1: {
                            cardDecreaseOp(cr1, false, card);
                        } break;
                    }
                } else {
                    //прямая операция прошла с ошибкой, ничего не делаем
                }
            }
            opHistory.save(new OpHistoryEntity(id, direction, card.getNum(), success));
        }
        return res;
    }

    private <T extends CrudRepository> String cardDecreaseOp (T repo, boolean isDecrease, CardDto card){
        var res = success;
        if (isDecrease) {
            var cardOpt = repo.findById(card.getNum());
            if (cardOpt.isPresent()) {
                var cardEntity = (CardEntity)cardOpt.get();
                if (cardEntity.getRest() >= card.getSum()) {
                    cardEntity.setRest(cardEntity.getRest() - card.getSum());
                    repo.save(cardEntity);
                } else {
                    res = notEnoughtMoney;
                }
            } else {
                res = cardNotFound;
            }
        } else {
            var cardOpt = repo.findById(card.getNum());
            if (cardOpt.isPresent()) {
                var cardEntity = (CardEntity)cardOpt.get();
                cardEntity.setRest(cardEntity.getRest() + card.getSum());
                repo.save(cardEntity);
            }
        }

        return res;
    }

    @JmsListener(destination = stepIncreaseOp, containerFactory = myFactory)
    public void onStepIncreaseRest (StepData data) {
        logger.debug("onStepIncreaseRest");
        //TODO try-catch вокруг транзакции, эту операцию нельзя повторять, вывалить ошибку шага
        var res = increaseCardRest(data, directionDirect);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepIncreaseOpRevert, containerFactory = myFactory)
    public void onStepIncreaseRestRevert (StepData data) {
        logger.debug("onStepIncreaseRestRevert");
        //TODO try-catch вокруг транзакции, эту операцию можно повторять, пока не отработает без ошибок (вызывать повторный вызов из catch)
        var res = increaseCardRest(data, directionRevert);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepDecreaseOp, containerFactory = myFactory)
    public void onStepDecreaseRest (StepData data) {
        logger.debug("onStepDecreaseRest");
        //TODO try-catch вокруг транзакции, эту операцию нельзя повторять, вывалить ошибку шага
        var res = decreaseCardRest(data, directionDirect);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepDecreaseOpRevert, containerFactory = myFactory)
    public void onStepDecreaseRestRevert (StepData data) {
        logger.debug("onStepDecreaseRestRevert");
        //TODO try-catch вокруг транзакции, эту операцию можно повторять, пока не отработает без ошибок (вызывать повторный вызов из catch)
        var res = decreaseCardRest(data, directionRevert);
        data.setStepResult(res);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    private Pair<Boolean, Boolean> checkOpHist(Long id) {
        var existsDirectOp = opHistory.findById(new OpHistoryEntityIdClass(id, directionDirect)).isPresent();
        var existsRevertOp = opHistory.findById(new OpHistoryEntityIdClass(id, directionRevert)).isPresent();
        return Pair.of(existsDirectOp, existsRevertOp);
    }
}
