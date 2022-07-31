package ru.loki7187.microsrv.globalDto.ui;

import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.IResultable;

import java.util.Objects;

import static ru.loki7187.microsrv.globalconfig.Constants.uiResultAddress;

public class CardTrnDto implements IResultable {
    private CardDto card;
    private Long id;

    public CardTrnDto(CardDto card, Long id) {
        this.card = card;
        this.id = id;
    }

    public CardTrnDto() {
    }

    public CardDto getCard() {
        return card;
    }

    public void setCard(CardDto card) {
        this.card = card;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getResultAddress() {
        return uiResultAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardTrnDto that = (CardTrnDto) o;
        return card.equals(that.card) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, id);
    }
}
