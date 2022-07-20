package ru.loki7187.microsrv.cardtransactions.service;

import org.springframework.stereotype.Service;
import ru.loki7187.microsrv.cardtransactions.dto.Card;

@Service
public class CardService {

    public Card increaseCard(long num, long sum) {
        return new Card(num, sum);
    }
}
