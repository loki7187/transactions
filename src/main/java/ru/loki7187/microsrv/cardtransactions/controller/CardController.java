package ru.loki7187.microsrv.cardtransactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.loki7187.microsrv.cardtransactions.dto.Card;
import ru.loki7187.microsrv.cardtransactions.dto.Transaction;
import ru.loki7187.microsrv.cardtransactions.service.CardService;

@RestController
@RequestMapping("/cardTransactions")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping("/increase")
    public String increaseCard(@RequestBody Card card){
        var res = cardService.increaseCardRest(card.getNum(), card.getSum());
        return res.getFirst() + " " + res.getSecond();
    }

    @PostMapping("/decrease")
    public String decreaseCard(@RequestBody Card card){
        var res = cardService.decreaseCardRest(card.getNum(), card.getSum());
        return res.getFirst() + " " + res.getSecond();
    }

    @PostMapping("/makeTransaction")
    public String makeTransaction(@RequestBody Transaction trn){
        var res = cardService.makeTransactionCardToCard(trn.getNum1(), trn.getSum(), trn.getNum2());
        return res.getLeft() + " " + res.getMiddle() + " " + res.getRight();
    }

    @GetMapping("/tmp")
    public String tmpMethod() {
        return "riegoirjg1122";
    }
}
