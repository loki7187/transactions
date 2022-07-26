package ru.loki7187.microsrv.trnService.step;

import java.util.ArrayList;

public interface CommonStepCore {
    String getStepId();
    String getStepDirectOperation();
    String getStepRevertOperation();
    Boolean getIsParallelable();
}
