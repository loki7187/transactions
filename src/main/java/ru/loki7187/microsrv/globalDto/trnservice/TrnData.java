package ru.loki7187.microsrv.globalDto.trnservice;

import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.loki7187.microsrv.globalDto.trnservice.StepData;
import ru.loki7187.microsrv.trnService.step.ICommonStep;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.preparation;

public class TrnData {

    // шаги транзакции (номер, шаблон, данные)
    private ConcurrentHashMap<Integer, Pair<ICommonStep, StepData>> steps;

    // ид транзакции
    private Long trnId;

    private String trnStage;

    private Integer currentStep;

    private String resultAddress;

    public String getResultAddress() {
        return resultAddress;
    }

    public void setResultAddress(String resultAddress) {
        this.resultAddress = resultAddress;
    }

    public ConcurrentHashMap<Integer, Pair<ICommonStep, StepData>> getSteps() {
        return steps;
    }

    public Long getTrnId() {
        return trnId;
    }

    public void setTrnId(Long trnId) {
        this.trnId = trnId;
    }

    public TrnData() {

        this.steps = new ConcurrentHashMap<>();
        this.trnStage = preparation;
        this.currentStep = 1;
        this.resultAddress = "";
    }

    public TrnData(String resultAddress_) {

        this.steps = new ConcurrentHashMap<>();
        this.trnStage = preparation;
        this.currentStep = 1;
        this.resultAddress = resultAddress_;
    }

    public Optional<Pair<ICommonStep, StepData>> getNextStep() {
        Optional<Pair<ICommonStep, StepData>> res = Optional.empty();
        if (steps.containsKey(currentStep)) {
            res = Optional.of(steps.get(currentStep));
            currentStep++;
        }
        return res;
    }

    public String getTrnStage() {
        return trnStage;
    }

    public void setTrnStage(String trnStage) {
        this.trnStage = trnStage;
    }
}
