package ru.loki7187.microsrv.uicontroller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.CardDto;
import ru.loki7187.microsrv.globalDto.CardTrnDto;
import ru.loki7187.microsrv.globalDto.TransactionDto;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.err;
import static ru.loki7187.microsrv.globalconfig.Constants.increaseOpFromUi;

@Service
public class UIService {

    private final String direct = "direct";
    private final String revert = "revert";

    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    ApplicationContext ctx;


    private ConcurrentHashMap<Long, Pair<DeferredResult, Object>> requests;

    public UIService () {
        requests = new ConcurrentHashMap<>();
    }

    public Long getId () {
        return new Date().getTime();
    }

    public void increaseReq (DeferredResult res, CardDto card) {
        var id = getId();
        res.onTimeout(() -> {
            cancelChangeReq(card, direct);
            res.setErrorResult(err);
        });
        requests.put(id, Pair.of(res, card));
        jmsTemplate.convertAndSend(increaseOpFromUi, new CardTrnDto(card, id));
    }

    public void decreaseReq (DeferredResult res, CardDto card) {
        var id = getId();
        res.onTimeout(() -> {
            cancelChangeReq(card, revert);
            res.setErrorResult(err);
        });
        requests.put(id, Pair.of(res, card));
        // TODO call trnService async
    }

    public void trnCardToCardReq (DeferredResult res, TransactionDto trn) {
        var id = getId();
        res.onTimeout(() -> {
            cancelTrn(trn);
            res.setErrorResult(err);
        });
        requests.put(id, Pair.of(res, trn));
        // TODO call trnService async
    }

    public void cancelChangeReq (CardDto card, String directParentOp) {
        if (directParentOp.equals(direct)) {
            //TODO call trnService async
        } else {
            //TODO call trnService async
        }
    }

    public void cancelTrn (TransactionDto trn){
        //TODO call trnService async
    }
}
