package ru.loki7187.microsrv.cardtransactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.loki7187.microsrv.cardtransactions.dao.CardRepo;
import ru.loki7187.microsrv.cardtransactions.entity.CardEntity;


@Service
public class CardService {

    private final String success = "success";
    private final String notEnoughtMoney = "notEnoughtMoney";
    private final String cardNotFound = "cardNotFound";
    private final String err = "err";


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
}
