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

    public Boolean getStepStatus() {
        return stepStatus;
    }
}
