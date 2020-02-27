package com.casic.cloud.hyperloop.core.annotation;

public enum OperationEnum {
    ADD("add"), SELECT("update"), INSERT("insert"), UPDATE("uodate"), LOGIN("login");

    private String content;

    OperationEnum(String content) {
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
