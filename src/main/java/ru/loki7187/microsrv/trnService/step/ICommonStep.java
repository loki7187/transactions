package ru.loki7187.microsrv.trnService.step;

public interface ICommonStep {
    String getStepId();
    String getStepDirectOperation();
    String getStepRevertOperation();
    Boolean getIsParallelable();
}
