package ru.loki7187.microsrv.uicontroller.service;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.TrnResultDto;
import ru.loki7187.microsrv.globalDto.ui.CardTrnDto;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class UIService {

    private final String direct = "direct";
    private final String revert = "revert";
    private final String cardReqType = "cardReqType";
    private final String trnReqType = "trnReqType";

    @Autowired
    JmsTemplate jmsTemplate;
//    @Autowired
//    ApplicationContext ctx;


    private final ConcurrentHashMap<Long, Triple<DeferredResult<String>, Object, String>> requests;

    public UIService () {
        requests = new ConcurrentHashMap<>();
    }

    public Long getId () {
        return new Date().getTime();
    }

    public void increaseReq (DeferredResult<String> res, CardDto card) {
        var id = getId();
        res.onTimeout(() -> {
            cancelChangeReq(card, direct);
            res.setErrorResult(err);
        });
        requests.put(id, Triple.of(res, card, cardReqType));
        jmsTemplate.convertAndSend(increaseOpFromUi, new CardTrnDto(card, id));
    }

    public void decreaseReq (DeferredResult<String> res, CardDto card) {
        var id = getId();
        res.onTimeout(() -> {
            cancelChangeReq(card, revert);
            res.setErrorResult(err);
        });
        requests.put(id, Triple.of(res, card, cardReqType));
        // TODO call trnService async
    }

    public void trnCardToCardReq (DeferredResult<String> res, TransactionDto trn) {
        var id = getId();
        res.onTimeout(() -> {
            cancelTrn(trn);
            res.setErrorResult(err);
        });
        requests.put(id, Triple.of(res, trn, trnReqType));
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

    @JmsListener(destination = uiResultAddress, containerFactory = myFactory)
    public void onRequestResult (TrnResultDto res) {
        System.out.println("setting result to deferredResult");
        requests.get(res.getId()).getLeft().setResult(res.getResult());
    }
}
