package ru.loki7187.app.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "Card", schema = "mainschema")
public class CardEntity {

    @Id
    @Column(name = "id")
    Long cardNum;

    @Column(name = "rest")
    Long rest;

    public CardEntity(Long cardNum, Long rest) {
        this.cardNum = cardNum;
        this.rest = rest;
    }

    public CardEntity() {
    }

    public Long getCardNum() {
        return cardNum;
    }

    public void setCardNum(Long cardNum) {
        this.cardNum = cardNum;
    }

    public Long getRest() {
        return rest;
    }

    public void setRest(Long rest) {
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "CardEntity{" +
                "cardNum=" + cardNum +
                ", rest=" + rest +
                '}';
    }
}