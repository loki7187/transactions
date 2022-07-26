package ru.loki7187.microsrv.cardtransactions.service;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.loki7187.microsrv.cardtransactions.dao.CardRepo;
import ru.loki7187.microsrv.cardtransactions.entity.CardEntity;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class CardService {

    @Autowired
    CardRepo cardRepo;

    @Transactional
    public Pair<Boolean, CardEntity> increaseCardRest(Long num, Long sum) {
        var ce = cardRepo.findById(num);
        CardEntity card = new CardEntity(0L, 0L);
        var res = true;
        try {
            if (ce.isPresent()) {
                card = ce.get();
                card.setRest(card.getRest() + sum);
                cardRepo.save(card);

            } else {
                card = new CardEntity(num, sum);
                cardRepo.save(card);
            }
        } catch (Exception e) {
            res = false;
        }
        return Pair.of(res, card);
    }

    @Transactional
    public Pair<String, CardEntity> decreaseCardRest(Long num, Long sum) {
        var res = success;
        var ce = cardRepo.findById(num);
        CardEntity card = new CardEntity(0L, 0L);
        try {
            if (ce.isPresent()) {
                card = ce.get();
                if (card.getRest() >= sum){
                    card.setRest(card.getRest() - sum);
                    cardRepo.save(card);
                }else {
                    res = notEnoughtMoney;
                }
            } else {
                res = cardNotFound;
            }
        } catch (Exception e) {
            res = err;
        }
        return Pair.of(res, card);
    }

    @Transactional
    public Triple<String, CardEntity, CardEntity> makeTransactionCardToCard (Long num1, Long sum, Long num2) {
        var res = success;
        var ce1 = cardRepo.findById(num1);
        var ce2 = cardRepo.findById(num2);
        CardEntity card1 = new CardEntity(0L, 0L);
        CardEntity card2 = new CardEntity(0L, 0L);
        try{
            if (ce1.isPresent() && ce2.isPresent()) {
                card1 = ce1.get();
                card2 = ce2.get();
                if (card1.getRest() >= sum) {
                    card1.setRest(card1.getRest() - sum);
                    card2.setRest(card2.getRest() + sum);
                } else {
                    res = notEnoughtMoney;
                }
            } else {
                res = cardNotFound;
            }
        } catch (Exception e) {
            res = err;
        }
        return Triple.of(res, card1, card2);
    }
}
