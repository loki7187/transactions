package ru.loki7187.microsrv.trnService.step;

import org.springframework.stereotype.Component;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Component
public class StepIncreaseTemplate implements ICommonStep {
    @Override
    public String getStepId() {
        return stepIncrease;
    }

    @Override
    public String getStepDirectOperation() {
        return stepIncreaseOp;
    }

    @Override
    public String getStepRevertOperation() {
        return stepIncreaseOpRevert;
    }

    @Override
    public Boolean getIsParallelable() {
        return true;
    }
}
