package ru.loki7187.microsrv.trnService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.ui.CardTrnDto;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;
import ru.loki7187.microsrv.globalDto.trnservice.StepData;
import ru.loki7187.microsrv.globalDto.trnservice.TrnData;
import ru.loki7187.microsrv.trnService.step.ICommonStep;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class TrnService {

    @Autowired
    List<ICommonStep> allSteps;

    @Autowired
    JmsTemplate jmsTemplate;

    private ConcurrentHashMap<Long, TrnData> queries;

    public TrnService () {
        queries = new ConcurrentHashMap<>();
    }

    public void increaseCardRest(CardDto card, Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress);
        trnData.setTrnId(id);
        var stepData = new StepData(id);
        //TODO надо сконвертировать card в жсон
        stepData.getStepParams().put(cardParam, card);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepFirst).findFirst().get(), new StepData(id)));
        trnData.getSteps().put(2, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), stepData));
        trnData.getSteps().put(3, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepLast).findFirst().get(), new StepData(id)));
        queries.put(id, trnData);
        runNextStep(trnData);
    }

    //TODO доделать
    public Pair<String, CardDto> decreaseCardRest(CardDto card, Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress);
        trnData.setTrnId(id);
        var stepData = new StepData(id);
        stepData.getStepParams().put(cardParam, card);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), stepData));
        Pair<String, CardDto> res = null;
        return res;
    }

    //TODO доделать
    public Pair<String, TransactionDto> makeTransactionCardToCard (TransactionDto trn , Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress);
        trnData.setTrnId(id);
        var stepData = new StepData(id);
        stepData.getStepParams().put("TrnDto", trn);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), stepData));
        Pair<String, TransactionDto> res = null;
        return res;
    }

    //TODO доделать
    public void runNextStep (TrnData data) {
        var currStep = data.getNextStep();
        if (currStep.isPresent()) {
            jmsTemplate.convertAndSend(currStep.get().getFirst().getStepDirectOperation(), currStep.get().getSecond());
        }
    }

    //TODO доделать
    //TODO не забыть, что окончание транзакции вынесено в отдельный шаг, тут делать ничего не надо
    // зато надо обработать ошибки и revert операции
    @JmsListener(destination = trnResultAddress, containerFactory = myFactory)
    public void onStepResult(StepData data) {
        if (data.getStepStatus().equals(success)){
            runNextStep(queries.get(data.getTrnId()));
        }
    }

    @JmsListener(destination = increaseOpFromUi, containerFactory = myFactory)
    public void onIncreaseOpFromUi (CardTrnDto cardTrn) {
        System.out.println(cardTrn.getCard() + " " + cardTrn.getId());
        increaseCardRest(cardTrn.getCard(), cardTrn.getId(), cardTrn.getResultAddress());
    }

    @JmsListener(destination = stepFirstOp, containerFactory = myFactory)
    public void onStepFirst(StepData data) {
        System.out.println("first step");
        queries.get(data.getTrnId()).setTrnStage(inProcess);
        data.setStepStatus(success);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepLastOp, containerFactory = myFactory)
    public void onStepLast(StepData data) {
        System.out.println("last step");
        data.setStepStatus(success);
        var trnData = queries.get(data.getTrnId());
        trnData.setTrnStage(completed);
        jmsTemplate.convertAndSend(trnData.getResultAddress(), Pair.of(trnData.getTrnId(), completed));
    }
}
