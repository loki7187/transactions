package ru.loki7187.microsrv.globalDto.trnservice;

import org.springframework.data.util.Pair;
import ru.loki7187.microsrv.trnService.step.ICommonStep;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

public class TrnData {

    // шаги транзакции (номер, шаблон, данные)
    private final ConcurrentHashMap<Integer, Pair<ICommonStep, StepData>> steps = new ConcurrentHashMap<>();

    // ид транзакции
    private Long trnId = 0L;

    private String trnStage = preparation;

    private String trnResult = trnEmptyResult;

    private String resultAddress = "";

    private Integer currStepNum = 1;

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
    }

    public TrnData(String resultAddress_, Long id) {
        this.resultAddress = resultAddress_;
        this.trnId = id;
    }

    public Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> getNextStep() {
        Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> res;
        if (trnStage.equals(preparation) || trnStage.equals(inProcess)) {
            res = steps.entrySet().stream().sorted(this::getIncreaseComparator)
                    .filter(e -> e.getValue().getSecond().getStepStatus().equals(emptyStepStatus) && e.getValue().getSecond().getStepDirection().equals(directionDirect))
                    .findFirst();
        }
        else {
            res = steps.entrySet().stream().sorted(this::getDecreaseComparator)
                    .filter(e -> e.getValue().getSecond().getStepDirection().equals(directionRevert) && e.getValue().getSecond().getStepStatus().equals(directOpDone))
                    .findFirst();
        }
        return res;
    }

    public Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> getNextStepForCancelOp() {
        Optional<Map.Entry<Integer, Pair<ICommonStep, StepData>>> res;
        res = steps.entrySet().stream().sorted(this::getDecreaseComparator)
                .filter(e -> e.getValue().getSecond().getStepDirection().equals(directionRevert) && e.getValue().getSecond().getStepStatus().equals(directOpDone))
                .findFirst();
        return res;
    }

    public int getIncreaseComparator(Map.Entry<Integer, Pair<ICommonStep, StepData>> e1, Map.Entry<Integer, Pair<ICommonStep, StepData>> e2) {
        return e1.getKey() <= e2.getKey() ? -1 : 1;
    }

    public int getDecreaseComparator(Map.Entry<Integer, Pair<ICommonStep, StepData>> e1, Map.Entry<Integer, Pair<ICommonStep, StepData>> e2) {
        return e1.getKey() <= e2.getKey() ? 1 : -1;
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

    public void putdata (Pair<ICommonStep, StepData> data) {
        data.getSecond().setStepNum(currStepNum);
        this.getSteps().put(currStepNum++, data);
    }

    public Pair<ICommonStep, StepData> getStepByNum(Integer stepNum) {
        return this.getSteps().entrySet().stream()
                .filter(e -> e.getValue().getSecond().getStepNum().equals(stepNum))
                .findFirst().get().getValue();
    }

}
