package ru.loki7187.microsrv.globalDto.trnservice;

import ru.loki7187.microsrv.globalDto.common.IResultable;

import java.util.HashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.emptyStepStatus;
import static ru.loki7187.microsrv.globalconfig.Constants.trnResultAddress;

public class StepData implements IResultable {

    private HashMap stepParams;
    private String stepStatus;

    private Long trnId;

    public StepData() {
        this.stepParams = new HashMap();
        this.stepStatus = emptyStepStatus;
    }

    public StepData(Long id) {
        this.stepParams = new HashMap();
        this.stepStatus = emptyStepStatus;
        this.trnId = id;
    }

    public HashMap getStepParams() {
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
}
