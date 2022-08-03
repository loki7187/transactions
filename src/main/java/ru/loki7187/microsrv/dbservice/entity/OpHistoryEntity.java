package ru.loki7187.microsrv.dbservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "Ophistory", schema = "mainschema", indexes = {
        @Index(name = "trnIdAndDirection", columnList = "trnId, direction")
})
public class OpHistoryEntity {

    @Id
    @Column(name = "trnId")
    Long trnId;

    @Id
    @Column(name = "direction")
    String direction;

    @Column(name = "card")
    Long num;

    @Column(name = "result")
    String opResult;

    public OpHistoryEntity() {
    }

    public OpHistoryEntity(Long trnId, String direction, Long num, String opResult) {
        this.trnId = trnId;
        this.direction = direction;
        this.num = num;
        this.opResult = opResult;
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

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getOpResult() {
        return opResult;
    }

    public void setOpResult(String opResult) {
        this.opResult = opResult;
    }
}
