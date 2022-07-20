package ru.loki7187.microsrv.cardtransactions.dto;

public class Card {
    private long num;
    private long sum;

    public Card(long num, long sum) {
        this.num = num;
        this.sum = sum;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
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
