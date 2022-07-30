package ru.loki7187.microsrv.globalDto.common;

public class TrnResultDto {
    Long id;
    String result;

    public TrnResultDto() {
    }

    public TrnResultDto(Long id, String result) {
        this.id = id;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
