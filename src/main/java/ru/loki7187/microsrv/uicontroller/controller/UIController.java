package ru.loki7187.microsrv.uicontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;
import ru.loki7187.microsrv.uicontroller.service.UIService;

@RestController
@RequestMapping("/cardTransactions")
public class UIController {

    @Autowired
    UIService uiService;

    private final Long timeout = 100000000L; //10 sec

    @PostMapping("/increase")
    public DeferredResult<String> increaseCard(@RequestBody CardDto card){
        DeferredResult<String> res = new DeferredResult<>(timeout);
        uiService.increaseReq(res, card);
        return res;
    }

    @PostMapping("/decrease")
    public DeferredResult<String> decreaseCard(@RequestBody CardDto card){
        DeferredResult<String> res = new DeferredResult<>(timeout);
        uiService.decreaseReq(res, card);
        return res;
    }

    @PostMapping("/makeTransaction")
    public DeferredResult<String> makeTransaction(@RequestBody TransactionDto trn){
        DeferredResult<String> res = new DeferredResult<>(timeout);
        uiService.trnCardToCardReq(res, trn);
        return res;
    }

    @GetMapping("/tmp")
    public DeferredResult<String> tmpMethod() {
        DeferredResult<String> output = new DeferredResult<>(500L);
        output.onTimeout( () -> {
            System.out.println("err result");
            output.setResult("321");});
        output.onCompletion( () -> {
            try {
                Thread.sleep(1000);
                System.out.println(output.isSetOrExpired());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread( () -> {
            try {
                Thread.sleep(1000);
                System.out.println("good res");
                output.setResult("11234");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        System.out.println("rijeoirejgiuehrg");
        return output;
    }
}
