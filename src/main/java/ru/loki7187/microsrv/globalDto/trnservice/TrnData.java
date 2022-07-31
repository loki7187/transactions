package ru.loki7187.microsrv.globalDto.trnservice;

import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.loki7187.microsrv.globalDto.trnservice.StepData;
import ru.loki7187.microsrv.trnService.step.ICommonStep;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

public class TrnData {

    // шаги транзакции (номер, шаблон, данные)
    private ConcurrentHashMap<Integer, Pair<ICommonStep, StepData>> steps;

    // ид транзакции
    private Long trnId;

    private String trnStage;

    private String trnResult;

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
        this.resultAddress = "";
        this.trnResult = trnEmptyResult;
    }

    public TrnData(String resultAddress_) {

        this.steps = new ConcurrentHashMap<>();
        this.trnStage = preparation;
        this.resultAddress = resultAddress_;
        this.trnResult = trnEmptyResult;
    }

    public Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> getNextStep() {
        Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> res;
        if (trnStage.equals(preparation) || trnStage.equals(inProcess)) {
            res = steps.entrySet().stream().sorted()
                    .filter(e -> e.getValue().getSecond().getStepStatus().equals(emptyStepStatus))
                    .findFirst();
        }
        else {
            res = steps.entrySet().stream().sorted( (e1, e2) -> e1.getKey() <= e2.getKey() ? -1 : 1)
                    .filter(e -> e.getValue().getSecond().getStepDirection().equals(directionRevert))
                    .findFirst();
        }
        return res;
    }

    public String getTrnStage() {
        return trnStage;
    }

    public void setTrnStage(String trnStage) {
        this.trnStage = trnStage;
    }

    public String getTrnResult() {
        return trnResult;
    }

    public void setTrnResult(String trnResult) {
        this.trnResult = trnResult;
    }
}
