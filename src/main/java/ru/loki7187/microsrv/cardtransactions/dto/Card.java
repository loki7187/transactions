package ru.loki7187.microsrv.cardtransactions.dto;

import java.io.Serializable;

public class Card implements Serializable {
    private Long num;
    private Long sum;

    public Card(Long num, Long sum) {
        this.num = num;
        this.sum = sum;
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
        return "Card{" +
                "num=" + num +
                ", sum=" + sum +
                '}';
    }
}
