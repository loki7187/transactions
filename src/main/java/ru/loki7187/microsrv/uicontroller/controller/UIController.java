package ru.loki7187.microsrv.uicontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.CardDto;
import ru.loki7187.microsrv.globalDto.TransactionDto;
import ru.loki7187.microsrv.trnService.service.TrnService;
import ru.loki7187.microsrv.uicontroller.service.UIService;

@RestController
@RequestMapping("/cardTransactions")
public class UIController {

    @Autowired
    TrnService trnService;

    @Autowired
    UIService uiService;

    @PostMapping("/increase")
    public String increaseCard(@RequestBody CardDto card){
        var res = trnService.increaseCardRest(card, uiService.getId());
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
    public DeferredResult<String> tmpMethod() {
        DeferredResult<String> output = new DeferredResult<>();
        output.onCompletion( () -> {
            try {
                Thread.sleep(1000);
                System.out.println("hfuiewhfiegfuierguerg");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread( () -> {
            try {
                Thread.sleep(1000);
                output.setResult("123");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).run();
        return output;
    }
}
