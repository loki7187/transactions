package ru.loki7187.microsrv.globalDto;

import java.io.Serializable;

public class CardDto implements Serializable {
    private Long num;
    private Long sum;

    public CardDto(Long num, Long sum) {
        this.num = num;
        this.sum = sum;
    }

    public CardDto() {
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "CardDto{" +
                "num=" + num +
                ", sum=" + sum +
                '}';
    }
}
