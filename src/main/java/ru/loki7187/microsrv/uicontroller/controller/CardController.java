package ru.loki7187.microsrv.uicontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.loki7187.microsrv.globalDto.CardDto;
import ru.loki7187.microsrv.globalDto.TransactionDto;
import ru.loki7187.microsrv.trnService.service.TrnService;

@RestController
@RequestMapping("/cardTransactions")
public class CardController {

    @Autowired
    TrnService trnService;

    @PostMapping("/increase")
    public String increaseCard(@RequestBody CardDto card){
        var res = trnService.increaseCardRest(card.getNum(), card.getSum());
        return res.getFirst() + " " + res.getSecond();
    }

    @PostMapping("/decrease")
    public String decreaseCard(@RequestBody CardDto card){
        var res = trnService.decreaseCardRest(card.getNum(), card.getSum());
        return res.getFirst() + " " + res.getSecond();
    }

    @PostMapping("/makeTransaction")
    public String makeTransaction(@RequestBody TransactionDto trn){
        var res = trnService.makeTransactionCardToCard(trn.getNum1(), trn.getSum(), trn.getNum2());
        return res.getLeft() + " " + res.getMiddle() + " " + res.getRight();
    }

    @GetMapping("/tmp")
    public String tmpMethod() {
        return "riegoirjg1122";
    }
}
