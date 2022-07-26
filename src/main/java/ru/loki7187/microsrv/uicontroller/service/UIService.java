package ru.loki7187.microsrv.uicontroller.service;

import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.common.CardDto;
import ru.loki7187.microsrv.globalDto.common.TrnResultDto;
import ru.loki7187.microsrv.globalDto.ui.CancelTrnDto;
import ru.loki7187.microsrv.globalDto.ui.CardTrnDto;
import ru.loki7187.microsrv.globalDto.common.TransactionDto;
import ru.loki7187.microsrv.globalDto.ui.TransactionTrnDto;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static ru.loki7187.microsrv.globalconfig.Constants.*;

@Service
public class UIService {
    private final String cardReqType = "cardReqType";
    private final String trnReqType = "trnReqType";

    private final String noneReqType = "noneReqType";

    @Autowired
    JmsTemplate jmsTemplate;
//    @Autowired
//    ApplicationContext ctx;

    private final Logger logger = LoggerFactory.getLogger(UIService.class);

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
            cancelReq(id, res);
            res.setErrorResult(err);
        });
        requests.put(id, Triple.of(res, card, cardReqType));
        jmsTemplate.convertAndSend(increaseOpFromUi, new CardTrnDto(card, id));
    }

    public void decreaseReq (DeferredResult<String> res, CardDto card) {
        var id = getId();
        res.onTimeout(() -> {
            cancelReq(id, res);
            res.setErrorResult(err);
        });
        requests.put(id, Triple.of(res, card, cardReqType));
        jmsTemplate.convertAndSend(decreaseOpFromUi, new CardTrnDto(card, id));
    }

    public void trnCardToCardReq (DeferredResult<String> res, TransactionDto trn) {
        var id = getId();
        res.onTimeout(() -> {
            cancelReq(id, res);
            res.setErrorResult(err);
        });
        requests.put(id, Triple.of(res, trn, trnReqType));
        jmsTemplate.convertAndSend(trnOpFromUi, new TransactionTrnDto(id, trn));
    }

    public void cancelReq (Long id, DeferredResult res) {
        var cancelOpId = getId();
        requests.put(cancelOpId, Triple.of(res, null, noneReqType));
        jmsTemplate.convertAndSend(cancelOpInProcess, new CancelTrnDto(id, cancelOpId));
    }

    @JmsListener(destination = uiResultAddress, containerFactory = myFactory)
    public void onRequestResult (TrnResultDto res) {
        logger.debug("setting result to deferredResult id = " + res.getId());
        logger.info("setting result to deferredResult id = " + res.getId());
        var req = requests.get(res.getId());
        if (!req.getLeft().hasResult()){
            // тут можно получить исходный объект - CardTrnDto или TransactionTrnDto из req.getMiddle, с приведением типа в зависимости от req.getRight
            req.getLeft().setResult(res.getResult());
            //requests.remove(res.getId());
        }
    }
}
