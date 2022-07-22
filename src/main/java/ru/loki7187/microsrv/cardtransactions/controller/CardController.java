package ru.loki7187.microsrv.cardtransactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.loki7187.microsrv.cardtransactions.dto.Card;
import ru.loki7187.microsrv.cardtransactions.entity.CardEntity;
import ru.loki7187.microsrv.cardtransactions.service.CardService;

@RestController
@RequestMapping("/cardTransactions")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/increase")
    public String increaseCard(@RequestBody Card card){
        var res = cardService.increaseCard(card.getNum(), card.getSum());
        return res.toString();
    }

    @GetMapping("/tmp")
    public String tmpMethod() {
        return "riegoirjg1122";
    }
}
