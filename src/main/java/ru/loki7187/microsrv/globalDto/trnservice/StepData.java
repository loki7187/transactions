package ru.loki7187.microsrv.globalDto.trnservice;

import ru.loki7187.microsrv.globalDto.common.IResultable;

import java.util.HashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

public class StepData implements IResultable {

    private final HashMap<String, String> stepParams = new HashMap<>();
    private String stepStatus = emptyStepStatus;

    private String stepResult = emptyStepResult;

    private Long trnId = 0L;

    private String stepDirection = directionDirect;

    private Integer repeatLimit = 5;

    public StepData() {
    }

    public StepData(Long id) {
        this.trnId = id;
    }

    public HashMap<String, String> getStepParams() {
        return stepParams;
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
        this.stepStatus = stepStatus;
    }

    public Long getTrnId() {
        return trnId;
    }

    public void setTrnId(Long trnId) {
        this.trnId = trnId;
    }

    @Override
    public String getResultAddress() {
        return trnResultAddress;
    }

    public String getStepDirection() {
        return stepDirection;
    }

    public void setStepDirection(String stepDirection) {
        this.stepDirection = stepDirection;
    }

    public String getStepResult() {
        return stepResult;
    }

    public void setStepResult(String stepResult) {
        this.stepResult = stepResult;
    }

    public Boolean canRepeat() {
        return repeatLimit-- > 0;
    }
}
