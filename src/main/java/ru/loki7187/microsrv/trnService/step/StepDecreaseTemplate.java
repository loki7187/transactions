package ru.loki7187.microsrv.trnService.step;

import org.springframework.stereotype.Component;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Component
public class StepDecreaseTemplate implements ICommonStep {
    @Override
    public String getStepId() {
        return stepDecrease;
    }

    @Override
    public String getStepDirectOperation() {
        return stepDecreaseOp;
    }

    @Override
    public String getStepRevertOperation() {
        return stepDecreaseOpRevert;
    }

    @Override
    public Boolean getIsParallelable() {
        return true;
    }
}
