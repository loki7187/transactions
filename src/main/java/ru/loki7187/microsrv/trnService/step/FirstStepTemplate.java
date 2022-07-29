package ru.loki7187.microsrv.trnService.step;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

public class FirstStepTemplate implements ICommonStep{
    @Override
    public String getStepId() {
        return stepFirst;
    }

    @Override
    public String getStepDirectOperation() {
        return stepFirstOp;
    }

    @Override
    public String getStepRevertOperation() {
        return stepFirstOpRevert;
    }

    @Override
    public Boolean getIsParallelable() {
        return false;
    }
}
