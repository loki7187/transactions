package ru.loki7187.microsrv.cardtransactions.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.loki7187.microsrv.cardtransactions.dto.Card;
import ru.loki7187.microsrv.cardtransactions.service.CardService;

@RestController
@RequestMapping("/cardTransactions")
@Api("controller")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/increase")
    public Card increaseCard(@RequestBody Card card){
        return cardService.increaseCard(card.getNum(), card.getSum());
    }

    @GetMapping("/tmp")
    public String tmpMethod() {
        return "riegoirjg11";
    }
}
