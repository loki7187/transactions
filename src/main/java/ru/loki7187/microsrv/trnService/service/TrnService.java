package ru.loki7187.microsrv.trnService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.loki7187.microsrv.globalDto.CardDto;
import ru.loki7187.microsrv.globalDto.CardTrnDto;
import ru.loki7187.microsrv.globalDto.TransactionDto;
import ru.loki7187.microsrv.trnService.data.StepData;
import ru.loki7187.microsrv.trnService.data.TrnData;
import ru.loki7187.microsrv.trnService.step.CommonStepCore;

import java.util.List;

import static ru.loki7187.microsrv.globalconfig.Constants.increaseOpFromUi;
import static ru.loki7187.microsrv.globalconfig.Constants.stepIncrease;

@Service
public class TrnService {

    @Autowired
    List<CommonStepCore> allSteps;

    @Autowired
    ApplicationContext ctx;

    public Pair<Boolean, CardDto> increaseCardRest(CardDto card, Long id) {
        var trnData = ctx.getBean(TrnData.class);
        trnData.setTrnId(id);
        var stepData = new StepData();
        stepData.getStepParams().put("CardDto", card);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), stepData));
        Pair<Boolean, CardDto> res = null;
        return res;
    }

    //TODO доделать
    public Pair<String, CardDto> decreaseCardRest(CardDto card, Long id) {
        var trnData = ctx.getBean(TrnData.class);
        trnData.setTrnId(id);
        var stepData = new StepData();
        stepData.getStepParams().put("CardDto", card);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), stepData));
        Pair<String, CardDto> res = null;
        return res;
    }

    //TODO доделать
    public Pair<String, TransactionDto> makeTransactionCardToCard (TransactionDto trn , Long id) {
        var trnData = ctx.getBean(TrnData.class);
        trnData.setTrnId(id);
        var stepData = new StepData();
        stepData.getStepParams().put("TrnDto", trn);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), stepData));
        Pair<String, TransactionDto> res = null;
        return res;
    }

    //TODO доделать
    public void onRunStep (TrnData data) {
        // тут отработать каждый шаг транзакции
    }

    //TODO доделать
    public void onResponceStepResult() {
        // тут отработать ответ на выполнение шага
    }

    @JmsListener(destination = increaseOpFromUi, containerFactory = "myFactory")
    public void onIncreaseOpFromUi (CardTrnDto cardTrn) {
        System.out.println(cardTrn.getCard() + " " + cardTrn.getId());
    }
}