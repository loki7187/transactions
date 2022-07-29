package ru.loki7187.microsrv.dbservice.entity;

import ru.loki7187.microsrv.globalDto.common.CardDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Card", schema = "mainschema")
public class CardEntity implements BaseCardEntity{

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

    @Override
    public CardDto getCardDtoView() {
        return new CardDto(getCardNum(), getRest());
    }
}
