package ru.loki7187.microsrv.uicontroller.service;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UIService {

    public Long getId () {
        return new Date().getTime();
    }

    //TODO need to save requests with id in some storage
}
