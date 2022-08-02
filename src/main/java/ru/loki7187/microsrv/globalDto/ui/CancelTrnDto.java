package ru.loki7187.microsrv.globalDto.ui;

import ru.loki7187.microsrv.globalDto.common.IResultable;

import static ru.loki7187.microsrv.globalconfig.Constants.uiResultAddress;

public class CancelTrnDto implements IResultable {
    private Long directOpId;
    private Long revertOpId;

    public CancelTrnDto() {
    }

    public CancelTrnDto(Long directOpId, Long revertOpId) {
        this.directOpId = directOpId;
        this.revertOpId = revertOpId;
    }

    public Long getDirectOpId() {
        return directOpId;
    }

    public void setDirectOpId(Long directOpId) {
        this.directOpId = directOpId;
    }

    public Long getRevertOpId() {
        return revertOpId;
    }

    public void setRevertOpId(Long revertOpId) {
        this.revertOpId = revertOpId;
    }

    @Override
    public String getResultAddress() {
        return uiResultAddress;
    }
}
