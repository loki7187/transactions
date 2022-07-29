package ru.loki7187.microsrv.trnService.step;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

public class LastStepTemplate implements ICommonStep{
    @Override
    public String getStepId() {
        return stepLast;
    }

    @Override
    public String getStepDirectOperation() {
        return stepLastOp;
    }

    @Override
    public String getStepRevertOperation() {
        return stepLastOpRevert;
    }

    @Override
    public Boolean getIsParallelable() {
        return false;
    }
}
