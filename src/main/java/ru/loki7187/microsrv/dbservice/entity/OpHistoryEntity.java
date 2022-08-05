package ru.loki7187.microsrv.dbservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Ophistory", schema = "mainschema"
,indexes = {
        @Index(name = "trnIdx", columnList = "trnId, direction, repoOp", unique = true)
})
public class OpHistoryEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "trnId")
    Long trnId;

    @Column(name = "direction")
    String direction;

    @Column(name = "repoOp")
    String repoOp;

    @Column(name = "card")
    Long num;

    @Column(name = "result")
    String opResult;

    public OpHistoryEntity() {
    }

    public OpHistoryEntity(Long trnId, String direction, String repoOp, Long num, String opResult) {
        this.trnId = trnId;
        this.direction = direction;
        this.repoOp = repoOp;
        this.num = num;
        this.opResult = opResult;
    }

    public Long getTrnId() {
        return this.trnId;
    }

    public void setTrnId(Long trnId) {
        this.trnId = trnId;
    }

    public String getDirection() {
        return this.direction;
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


