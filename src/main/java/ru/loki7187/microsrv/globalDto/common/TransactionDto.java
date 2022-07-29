package ru.loki7187.microsrv.globalDto.common;

import static ru.loki7187.microsrv.globalconfig.Constants.uiResultAddress;

public class TransactionDto implements IResultable {
    private Long num1;
    private Long num2;
    private Long sum;

    public TransactionDto() {
    }

    public TransactionDto(Long num1, Long num2, Long sum) {
        this.num1 = num1;
        this.num2 = num2;
        this.sum = sum;
    }

    public Long getNum1() {
        return num1;
    }

    public void setNum1(Long num1) {
        this.num1 = num1;
    }

    public Long getNum2() {
        return num2;
    }

    public void setNum2(Long num2) {
        this.num2 = num2;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    @Override
    public String getResultAddress() {
        return uiResultAddress;
    }
}
