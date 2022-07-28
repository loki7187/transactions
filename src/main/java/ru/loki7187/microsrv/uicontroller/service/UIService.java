package ru.loki7187.microsrv.uicontroller.service;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import ru.loki7187.microsrv.globalDto.CardDto;
import ru.loki7187.microsrv.globalDto.TransactionDto;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UIService {

    private ConcurrentHashMap<Long, Pair<DeferredResult, Object>> requests;

    public Long getId () {
        return new Date().getTime();
    }

    public void increaseReq (DeferredResult res, CardDto card) {
        var id = getId();
        requests.put(id, Pair.of(res, card));
        // TODO call trnService
    }

    public void decreaseReq (DeferredResult res, CardDto card) {
        var id = getId();
        requests.put(id, Pair.of(res, card));
        // TODO call trnService
    }

    public void trnCardToCardReq (DeferredResult res, TransactionDto trn) {
        var id = getId();
        requests.put(id, Pair.of(res, trn));
        // TODO call trnService
    }
}
