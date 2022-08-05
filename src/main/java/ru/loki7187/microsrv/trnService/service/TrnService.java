package ru.loki7187.microsrv.trnService.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.TrnResultDto;
import ru.loki7187.microsrv.globalDto.ui.CancelTrnDto;
import ru.loki7187.microsrv.globalDto.ui.CardTrnDto;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;
import ru.loki7187.microsrv.globalDto.trnservice.StepData;
import ru.loki7187.microsrv.globalDto.trnservice.TrnData;
import ru.loki7187.microsrv.globalDto.ui.TransactionTrnDto;
import ru.loki7187.microsrv.trnService.step.ICommonStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class TrnService {

    @Autowired
    List<ICommonStep> allSteps;

    @Autowired
    JmsTemplate jmsTemplate;

    //TODO переделать на хранение в бд (если успею)
    //TODO вынести хранилище в отдельный класс
    private final ConcurrentHashMap<Long, Pair<TrnData, ArrayList<Long>>> queries;

    private final Logger logger = LoggerFactory.getLogger(TrnService.class);

    public TrnService () {
        queries = new ConcurrentHashMap<>();
    }

    @Transactional
    public void increaseCardRest(CardDto card, Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress, id);
        var stepData = new StepData(id);
        var cardList = new ArrayList<>(List.of(card.getNum()));
        stepData.getStepParams().put(cardParam, new Gson().toJson(card, CardDto.class));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepFirst)).findFirst().get(), new StepData(id)));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepIncrease)).findFirst().get(), stepData));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepLast)).findFirst().get(), new StepData(id)));
        queries.put(id, Pair.of(trnData, cardList));
        runNextStep(trnData);
    }

    public void decreaseCardRest(CardDto card, Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress, id);
        var stepData = new StepData(id);
        var cardList = new ArrayList<>(List.of(card.getNum()));
        stepData.getStepParams().put(cardParam, new Gson().toJson(card, CardDto.class));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepFirst)).findFirst().get(), new StepData(id)));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepDecrease)).findFirst().get(), stepData));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepLast)).findFirst().get(), new StepData(id)));
        queries.put(id, Pair.of(trnData, cardList));
        runNextStep(trnData);
    }

    public void makeTransactionCardToCard (TransactionDto trn , Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress, id);
        var stepData1 = new StepData(id);
        var stepData2 = new StepData(id);
        var cardList = new ArrayList<>(List.of(trn.getNum1(), trn.getNum2()));

        stepData1.getStepParams().put(cardParam, new Gson().toJson(new CardDto(trn.getNum1(),trn.getSum()), CardDto.class));
        stepData2.getStepParams().put(cardParam, new Gson().toJson(new CardDto(trn.getNum2(),trn.getSum()), CardDto.class));

        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepFirst)).findFirst().get(), new StepData(id)));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepDecrease)).findFirst().get(), stepData1));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepIncrease)).findFirst().get(), stepData2));
        trnData.putdata(Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepLast)).findFirst().get(), new StepData(id)));
        queries.put(id, Pair.of(trnData, cardList));
        runNextStep(trnData);
    }

    //если
    @Transactional
    public void runNextStep (TrnData data) {
        Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> stepOp;
        if (data.getTrnStage().equals(cancelOpInProcess)){
            stepOp = data.getNextStepForCancelOp();
        } else {
            stepOp = data.getNextStep();
        }
        stepOp.ifPresent(
                (step) -> {
                    if (step.getValue().getSecond().getStepDirection().equals(directionDirect)){step.getValue().getSecond().setStepStatus(directOpDone);}
                    else {step.getValue().getSecond().setStepStatus(revertOpDone);}
                    jmsTemplate.convertAndSend(step.getValue().getSecond().getStepDirection().equals(directionDirect)
                                    ? step.getValue().getFirst().getStepDirectOperation()
                                    : step.getValue().getFirst().getStepRevertOperation()
                            , step.getValue().getSecond());
                });
    }

    //TODO не забыть посмотреть, где нужна @Transactional
    //TODO проверить корректность/нужность использования параллельных коллекций
    @JmsListener(destination = trnResultAddress, containerFactory = myFactory)
    public void onStepResult(StepData data) {
        var trnData = data.getCancelOpId().equals(0L) ? queries.get(data.getTrnId()).getFirst() : queries.get(data.getCancelOpId()).getFirst();
        trnData.getStepByNum(data.getStepNum()).getSecond().setStepResult(data.getStepResult());
        if (!(trnData.getTrnStage().equals(canceled) || trnData.getTrnStage().equals(cancelOpInProcess))) {
            if (data.getStepResult().equals(success)) {
                runNextStep(queries.get(data.getTrnId()).getFirst());
            } else {
                trnData.setTrnStage(trnErr);
                trnData.setTrnResult(data.getStepResult());
                trnData.getSteps().entrySet().stream()
                        .filter(e -> e.getValue().getSecond().getStepStatus().equals(directOpDone))
                        .forEach(e -> e.getValue().getSecond().setStepDirection(directionRevert));
                runNextStep(trnData);
            }
        } else {
            // отработает отменяющая операция
            runNextStep(trnData);
        }
    }

    @JmsListener(destination = increaseOpFromUi, containerFactory = myFactory)
    public void onIncreaseOpFromUi (CardTrnDto cardTrn) {
        logger.debug("onIncreaseOpFromUi");
        if (checkAnotherOpForCardInProcess(cardTrn.getId())){
            increaseCardRest(cardTrn.getCard(), cardTrn.getId(), cardTrn.getResultAddress());
        } else {
            jmsTemplate.convertAndSend(cardTrn.getResultAddress(), new TrnResultDto(cardTrn.getId(), anotherOpInProcess));
        }
    }

    @JmsListener(destination = decreaseOpFromUi, containerFactory = myFactory)
    public void onDecreaseOpFromUi (CardTrnDto cardTrn) {
        logger.debug("onDecreaseOpFromUi");
        if (checkAnotherOpForCardInProcess(cardTrn.getId())){
            decreaseCardRest(cardTrn.getCard(), cardTrn.getId(), cardTrn.getResultAddress());
        } else {
            jmsTemplate.convertAndSend(cardTrn.getResultAddress(), new TrnResultDto(cardTrn.getId(), anotherOpInProcess));
        }
    }

    @JmsListener(destination = trnOpFromUi, containerFactory = myFactory)
    public void onTrnOpFromUi (TransactionTrnDto transactionTrnDto) {
        logger.debug("onTrnOpFromUi");
        if (checkAnotherOpForCardInProcess(transactionTrnDto.getTrn().getNum1()) && checkAnotherOpForCardInProcess(transactionTrnDto.getTrn().getNum2())){
            makeTransactionCardToCard(transactionTrnDto.getTrn(), transactionTrnDto.getId(), transactionTrnDto.getResultAddress());
        } else {
            jmsTemplate.convertAndSend(transactionTrnDto.getResultAddress(), new TrnResultDto(transactionTrnDto.getId(), anotherOpInProcess));
        }
    }

    @JmsListener(destination = cancelOpInProcess, containerFactory = myFactory)
    public void cancelOp (CancelTrnDto cancelTrnDto) {
        // TODO проверить, не выставлен ли уже статус cancelled для прямой операции
        cancelOperation(cancelTrnDto);
    }

    @JmsListener(destination = stepFirstOp, containerFactory = myFactory)
    public void onStepFirst(StepData data) {
        logger.debug("first step");
        queries.get(data.getTrnId()).getFirst().setTrnStage(inProcess);
        data.setStepResult(success);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    //либо это последний шаг, либо обратный для первого
    @Transactional
    @JmsListener(destination = stepLastOp, containerFactory = myFactory)
    public void onStepLast(StepData data) {
        logger.debug("last step");
        var trnData = data.getCancelOpId().equals(0L) ? queries.get(data.getTrnId()).getFirst() : queries.get(data.getCancelOpId()).getFirst();
        var thisStepData = trnData.getStepByNum(data.getStepNum()).getSecond();
        if (trnData.getTrnStage().equals(inProcess) && trnData.getTrnResult().equals(trnEmptyResult)){
            thisStepData.setStepResult(success);
            trnData.setTrnStage(completed);
            trnData.setTrnResult(success);
        }else if ((trnData.getTrnStage().equals(cancelOpInProcess))) {
            thisStepData.setStepResult(success);
            String res = trnData.getSteps().entrySet().stream()
                    .map(e -> e.getValue().getSecond())
                    .filter(e -> !e.getStepResult().equals(success))
                    .count() > 0 ? err : success;
            trnData.setTrnResult(res);
            trnData.setTrnStage(cancelOpInDone);
        }else {
            // это обратный для первого шага
            trnData.setTrnStage(completed);
        }
        //queries.remove(data.getTrnId());
        jmsTemplate.convertAndSend(trnData.getResultAddress(), new TrnResultDto(trnData.getTrnId(), trnData.getTrnResult()));
    }

    @JmsListener(destination = stepLastOpRevert, containerFactory = myFactory)
    public void stepLastOpRevert (StepData data) {
        data.setStepResult(success);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    private Boolean checkAnotherOpForCardInProcess (Long cardId) {
        return queries.values().stream().
                filter(e -> !(e.getFirst().getTrnStage().equals(completed) || e.getFirst().getTrnStage().equals(canceled) || e.getFirst().getTrnStage().equals(cancelOpInDone)))
                .flatMap(e -> e.getSecond().stream())
                .filter(e -> e.equals(cardId))
                .count() == 0;
    }

    private void cancelOperation (CancelTrnDto cancelTrnDto) {
        if (!queries.containsKey(cancelTrnDto.getDirectOpId())){
            return;
        }
        var op = queries.get(cancelTrnDto.getDirectOpId());
        // операция либо в процессе, либо выполнена, либо с еррором
        // от этого зависит выборка откатываемых шагов
        op.getFirst().setTrnStage(canceled);
        //выбрать все шаги со статусом не emptyStepStatus, выполнить новую операцию с шагами старой в обратном порядке +-
        //шаги со статусом revertOpDone тоже выберем, запуск шага должен быть идемпотентным

        var newOp = new TrnData(cancelTrnDto.getResultAddress(), cancelTrnDto.getRevertOpId());
        newOp.setTrnStage(cancelOpInProcess);
        op.getFirst().getSteps().entrySet().stream()
                .filter(e -> !e.getValue().getSecond().getStepStatus().equals(emptyStepStatus))
                .collect(Collectors.toList())
                .stream()
                .forEach( e-> newOp.putdata(Pair.of(e.getValue().getFirst(), new StepData(cancelTrnDto.getDirectOpId(), e.getValue().getSecond()))));
        newOp.getSteps().entrySet().stream().forEach((e) -> {
            e.getValue().getSecond().setStepDirection(directionRevert);
            e.getValue().getSecond().setStepStatus(directOpDone);
            e.getValue().getSecond().setCancelOpId(cancelTrnDto.getRevertOpId());
        });
        var newCardList = new ArrayList<Long>();
        newCardList.addAll( op.getSecond());
        queries.put(cancelTrnDto.getRevertOpId(), Pair.of(newOp, newCardList));
        runNextStep(newOp);
    }
}
