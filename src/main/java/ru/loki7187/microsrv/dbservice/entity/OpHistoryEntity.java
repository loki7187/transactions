package ru.loki7187.microsrv.dbservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Ophistory", schema = "mainschema")
public class OpHistoryEntity implements Serializable {

    @Id
    @Column(name = "id")
    OpHistoryEntityIdClass id = new OpHistoryEntityIdClass();;

    @Column(name = "card")
    Long num;

    @Column(name = "result")
    String opResult;

    public OpHistoryEntity() {
    }

    public OpHistoryEntity(Long trnId, String direction, Long num, String opResult) {
        this.id.setTrnId(trnId);
        this.id.setDirection(direction);
        this.num = num;
        this.opResult = opResult;
    }

    public Long getTrnId() {
        return this.id.getTrnId();
    }

    public void setTrnId(Long trnId) {
        this.id.setTrnId(trnId);
    }

    public String getDirection() {
        return this.id.getDirection();
    }

    public void setDirection(String direction) {
        this.id.setDirection(direction);
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


