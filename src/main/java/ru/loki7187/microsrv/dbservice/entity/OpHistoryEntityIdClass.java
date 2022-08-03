package ru.loki7187.microsrv.dbservice.entity;

import java.io.Serializable;

public class OpHistoryEntityIdClass implements Serializable {
    private Long trnId;
    private String direction;

    public OpHistoryEntityIdClass(Long trnId, String direction) {
        this.trnId = trnId;
        this.direction = direction;
    }

    public OpHistoryEntityIdClass() {
    }

    public Long getTrnId() {
        return trnId;
    }

    public void setTrnId(Long trnId) {
        this.trnId = trnId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
