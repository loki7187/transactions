package ru.loki7187.microsrv.trnService.data;

import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.loki7187.microsrv.trnService.step.CommonStepCore;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = "prototype")
public class TrnData {
    ConcurrentHashMap<Integer, Pair<CommonStepCore, StepData>> steps;
    Long trnId;

    public ConcurrentHashMap<Integer, Pair<CommonStepCore, StepData>> getSteps() {
        return steps;
    }

    public void setSteps(ConcurrentHashMap<Integer, Pair<CommonStepCore, StepData>> steps) {
        this.steps = steps;
    }

    public Long getTrnId() {
        return trnId;
    }

    public void setTrnId(Long trnId) {
        this.trnId = trnId;
    }
}
