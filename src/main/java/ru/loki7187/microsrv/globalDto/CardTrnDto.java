package ru.loki7187.microsrv.globalDto;

public class CardTrnDto {
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
}
