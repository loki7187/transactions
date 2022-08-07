package ru.loki7187.microsrv.globalconfig;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    //global result/return
    public static final String success = "success";
    public static final String notEnoughtMoney = "notEnoughtMoney";
    public static final String cardNotFound = "cardNotFound";
    public static final String err = "err";
    public static final String anotherOpInProcess = "anotherOpInProcess";

    // trnSrvOperations
    public static final String stepIncrease = "stepIncrease";
    public static final String stepIncreaseOpRevert = "stepIncreaseOpRevert";
    public static final String stepIncreaseOp = "stepIncreaseOp";
    public static final String stepDecrease = "stepDecrease";
    public static final String stepDecreaseOpRevert = "stepDecreaseOpRevert";
    public static final String stepDecreaseOp = "stepDecreaseOp";

    public static final String stepFirst = "stepFirst";
//    public static final String stepFirstOpRevert = "stepFirstOpRevert"; вызывается последний шаг с нужными параметрами
    public static final String stepFirstOp = "stepFirstOp";

    public static final String stepLast = "stepLast";
    public static final String stepLastOpRevert = "stepLastOpRevert";
    public static final String stepLastOp = "stepLastOp";
    public static final String cancelOpInProcess = "cancelOpInProcess";
    public static final String cancelOpInDone = "cancelOpInDone";
    public static final String trnResultAddress = "trnResultAddress";
    public static final String cardParam = "cardParam";
    public static final String trnParam = "trnParam";

    //trnStages
    public static final String preparation = "preparation";
    public static final String inProcess = "inProcess";
    public static final String completed = "completed";
    public static final String canceled = "canceled";
    public static final String trnErr = "trnErr";
    public static final String cancelOP = "cancelOP";

    //trn results
    public static final String trnEmptyResult = "trnEmptyResult";

    //step directions
    public static final String directionDirect = "directionDirect";
    public static final String directionRevert = "directionRevert";

    //step statuses
    public static final String emptyStepStatus = "emptyStepStatus";
    public static final String directOpDone = "directOpDone";
    public static final String revertOpDone = "revertOpDone";

    //step results
    public static final String emptyStepResult = "emptyStepResult";

    // uiSrv operations
    public static final String increaseOpFromUi = "increaseOpFromUi";
    public static final String increaseOpFromUiRevert = "increaseOpFromUiRevert";

    public static final String decreaseOpFromUi = "decreaseOpFromUi";
    public static final String decreaseOpFromUiRevert = "decreaseOpFromUiRevert";

    public static final String trnOpFromUi = "trnOpFromUi";
    public static final String trnOpFromUiRevert = "trnOpFromUiRevert";

    public static final String uiResultAddress = "uiResultAddress";

    //repo operations
    public static final String repoOpIncrease = "repoOpIncrease";
    public static final String repoOpDecrease = "repoOpDecrease";
    // jms
    public static final String myFactory = "myFactory";

//    @Value("app.global.constants.globalTimeout")
    public static Long globalTimeout; //5000L

}
