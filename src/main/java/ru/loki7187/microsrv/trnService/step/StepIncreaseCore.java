package ru.loki7187.microsrv.trnService.step;

import org.springframework.stereotype.Component;

import static ru.loki7187.microsrv.globalconfig.Constants.stepIncrease;

@Component
public class StepIncreaseCore implements CommonStepCore {
    @Override
    public String getStepId() {
        return stepIncrease;
    }

    @Override
    public String getStepDirectOperation() {
        return null;
    }

    @Override
    public String getStepRevertOperation() {
        return null;
    }

    @Override
    public Boolean getIsParallelable() {
        return null;
    }
}
