package ru.loki7187.microsrv.trnService.service;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import ru.loki7187.microsrv.dbservice.entity.CardEntity;
import ru.loki7187.microsrv.globalDto.CardDto;
import ru.loki7187.microsrv.trnService.data.StepData;
import ru.loki7187.microsrv.trnService.data.TrnData;
import ru.loki7187.microsrv.trnService.step.CommonStepCore;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
@EnableAsync
//@EnableJms
public class TrnService {

    @Autowired
    List<CommonStepCore> allSteps;

    @Autowired
    ApplicationContext ctx;

    public Pair<Boolean, CardDto> increaseCardRest(Long num, Long sum) {
        var trnId = getTrnId();
        var trnData = ctx.getBean(TrnData.class);
        trnData.setTrnId(trnId);
        trnData.getSteps().put(1, Pair.of(allSteps.stream().filter(e -> e.getStepId() == stepIncrease).findFirst().get(), new StepData()));
        var futureRes = runTrn(trnData);
        Pair<Boolean, CardDto> res = null;
        try {
            res = futureRes.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public Pair<String, CardEntity> decreaseCardRest(Long num, Long sum) {
        var trnId = getTrnId();
        var res = success;
        CardEntity card = new CardEntity(0L, 0L);
        return Pair.of(res, card);
    }

    public Triple<String, CardEntity, CardEntity> makeTransactionCardToCard (Long num1, Long sum, Long num2) {
        var trnId = getTrnId();
        var res = success;
        CardEntity card1 = new CardEntity(0L, 0L);
        CardEntity card2 = new CardEntity(0L, 0L);
        return Triple.of(res, card1, card2);
    }

    private Long getTrnId () {
        return new Date().getTime();
    }

    @Async
    public Future<Pair<Boolean, CardDto>> runTrn (TrnData data){
        CardDto card = new CardDto(0L, 0L);
        var res = true;
        // тут надо вызвать шаги транзакции
        return new AsyncResult<>(Pair.of(res, card));
    }

    public void onRunStep (TrnData data) {
        // тут отработать каждый шаг транзакции
    }

    public void onApplyStepResult() {
        // тут отработать ответ на выполнение шага
    }
}
