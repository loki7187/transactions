package ru.loki7187.microsrv.trnService.data;

import java.util.ArrayList;
import java.util.HashMap;

public class StepData {

    HashMap stepParams;
    Boolean stepStatus;

    public StepData() {
        this.stepParams = new HashMap();
    }

    public HashMap getStepParams() {
        return stepParams;
    }

    public void setStepParams(HashMap stepParams) {
        this.stepParams = stepParams;
    }

    public Boolean getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(Boolean stepStatus) {
        this.stepStatus = stepStatus;
    }
}
