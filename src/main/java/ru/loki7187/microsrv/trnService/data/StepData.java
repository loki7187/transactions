package ru.loki7187.microsrv.trnService.data;

import java.util.ArrayList;

public class StepData {

    ArrayList stepParams;
    Boolean stepStatus;

    public ArrayList getStepParams() {
        return stepParams;
    }

    public void setStepParams(ArrayList stepParams) {
        this.stepParams = stepParams;
    }

    public Boolean getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(Boolean stepStatus) {
        this.stepStatus = stepStatus;
    }
}
