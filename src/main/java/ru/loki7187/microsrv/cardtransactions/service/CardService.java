package ru.loki7187.microsrv.cardtransactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.loki7187.microsrv.cardtransactions.dao.CardRepo;
import ru.loki7187.microsrv.cardtransactions.entity.CardEntity;

@Service
public class CardService {

    @Autowired
    CardRepo cardRepo;

    public Pair<Boolean, CardEntity> increaseCard(Long num, Long sum) {
        var ce = cardRepo.findById(num);
        if (ce.isPresent()){
            var cardEntity = ce.get();
            cardEntity.setRest(cardEntity.getRest() + sum);
            cardRepo.save(cardEntity);
            return Pair.of(true, cardEntity);
        }
        return Pair.of(false, new CardEntity());
    }
}
