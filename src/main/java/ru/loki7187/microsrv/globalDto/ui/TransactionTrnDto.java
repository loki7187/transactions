package ru.loki7187.microsrv.globalDto.ui;

import ru.loki7187.microsrv.globalDto.common.IResultable;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;

import static ru.loki7187.microsrv.globalconfig.Constants.uiResultAddress;

public class TransactionTrnDto implements IResultable {

    Long id;

    TransactionDto trn;

    public TransactionTrnDto(Long id, TransactionDto trn) {
        this.id = id;
        this.trn = trn;
    }

    public TransactionTrnDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionDto getTrn() {
        return trn;
    }

    public void setTrn(TransactionDto trn) {
        this.trn = trn;
    }

    @Override
    public String getResultAddress() {
        return uiResultAddress;
    }
}
