package ru.loki7187.microsrv.uicontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;
import ru.loki7187.microsrv.uicontroller.service.UIService;

import static ru.loki7187.microsrv.globalconfig.Constants.globalTimeout;

@RestController
@RequestMapping("/cardTransactions")
public class UIController {

    @Autowired
    UIService uiService;


    @PostMapping("/increase")
    public DeferredResult<String> increaseCard(@RequestBody CardDto card){
        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
        uiService.increaseReq(res, card);
        return res;
    }

    @PostMapping("/decrease")
    public DeferredResult<String> decreaseCard(@RequestBody CardDto card){
        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
        uiService.decreaseReq(res, card);
        return res;
    }

    @PostMapping("/makeTransaction")
    public DeferredResult<String> makeTransaction(@RequestBody TransactionDto trn){
        DeferredResult<String> res = new DeferredResult<>(globalTimeout);
        uiService.trnCardToCardReq(res, trn);
        return res;
    }

    @PostMapping("/cancelOp")
    public DeferredResult<String> cancelOp(@RequestParam Long opId){
        DeferredResult<String> res = new DeferredResult<>(50000000L);
        uiService.cancelReq(opId, res);
        return res;
    }

    @GetMapping("/tmp")
    public DeferredResult<String> tmpMethod() {
        DeferredResult<String> output = new DeferredResult<>(globalTimeout);
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
