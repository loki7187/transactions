package ru.loki7187.microsrv.trnService.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.TrnResultDto;
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

    //TODO переделать на хранение в бд
    private final ConcurrentHashMap<Long, TrnData> queries;

    private final Logger logger = LoggerFactory.getLogger(TrnService.class);

    public TrnService () {
        queries = new ConcurrentHashMap<>();
    }

    public void increaseCardRest(CardDto card, Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress);
        trnData.setTrnId(id);
        var stepData = new StepData(id);
        stepData.getStepParams().put(cardParam, new Gson().toJson(card, CardDto.class));
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepFirst)).findFirst().get(), new StepData(id)));
        trnData.getSteps().put(2, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepIncrease)).findFirst().get(), stepData));
        trnData.getSteps().put(3, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepLast)).findFirst().get(), new StepData(id)));
        queries.put(id, trnData);
        runNextStep(trnData);
    }

    //TODO доделать
    public void decreaseCardRest(CardDto card, Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress);
        trnData.setTrnId(id);
        var stepData = new StepData(id);
        stepData.getStepParams().put(cardParam, new Gson().toJson(card, CardDto.class));
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepFirst)).findFirst().get(), new StepData(id)));
        trnData.getSteps().put(2, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepDecrease)).findFirst().get(), stepData));
        trnData.getSteps().put(3, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepLast)).findFirst().get(), new StepData(id)));
        queries.put(id, trnData);
        runNextStep(trnData);
    }

    //TODO доделать
    public Pair<String, TransactionDto> makeTransactionCardToCard (TransactionDto trn , Long id, String resultAddress) {
        var trnData = new TrnData(resultAddress);
        trnData.setTrnId(id);
        var stepData = new StepData(id);
        stepData.getStepParams().put(trnParam, new Gson().toJson(trn, TransactionDto.class));
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId().equals(stepIncrease)).findFirst().get(), stepData));
        Pair<String, TransactionDto> res = null;
        return res;
    }

    //TODO доделать
    public void runNextStep (TrnData data) {
        data.getNextStep().ifPresent(
                step -> jmsTemplate.convertAndSend(step.getValue().getFirst().getStepDirectOperation()
                        , step.getValue().getSecond())
        );
    }

    //TODO не забыть посмотреть, где нужна @Transactionsl

    //TODO доделать
    //TODO не забыть, что окончание транзакции вынесено в отдельный шаг, тут делать ничего не надо
    // зато надо обработать ошибки и revert операции
    @JmsListener(destination = trnResultAddress, containerFactory = myFactory)
    public void onStepResult(StepData data) {
        if (data.getStepStatus().equals(success)){
            runNextStep(queries.get(data.getTrnId()));
        } else {
            var trnData = queries.get(data.getTrnId());
            trnData.setTrnStage(trnErr);
            trnData.getSteps().entrySet().stream()
                    .filter(e -> !e.getValue().getSecond().getStepStatus().equals(emptyStepStatus))
                            .forEach(e -> e.getValue().getSecond().setStepDirection(directionRevert));
            runNextStep(trnData);
        }
    }

    @JmsListener(destination = increaseOpFromUi, containerFactory = myFactory)
    public void onIncreaseOpFromUi (CardTrnDto cardTrn) {
        logger.debug("onIncreaseOpFromUi");
        increaseCardRest(cardTrn.getCard(), cardTrn.getId(), cardTrn.getResultAddress());
    }

    @JmsListener(destination = decreaseOpFromUi, containerFactory = myFactory)
    public void onDecreaseOpFromUi (CardTrnDto cardTrn) {
        logger.debug("onDecreaseOpFromUi");
        decreaseCardRest(cardTrn.getCard(), cardTrn.getId(), cardTrn.getResultAddress());
    }

    @JmsListener(destination = stepFirstOp, containerFactory = myFactory)
    public void onStepFirst(StepData data) {
        logger.debug("first step");
        queries.get(data.getTrnId()).setTrnStage(inProcess);
        data.setStepStatus(success);
        jmsTemplate.convertAndSend(data.getResultAddress(), data);
    }

    @JmsListener(destination = stepLastOp, containerFactory = myFactory)
    public void onStepLast(StepData data) {
        logger.debug("last step");
        data.setStepStatus(success);
        var trnData = queries.get(data.getTrnId());
        trnData.setTrnStage(completed);
        jmsTemplate.convertAndSend(trnData.getResultAddress(), new TrnResultDto(trnData.getTrnId(), completed));
    }
}
