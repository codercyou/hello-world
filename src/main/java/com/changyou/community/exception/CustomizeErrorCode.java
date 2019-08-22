package com.changyou.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找到问题不在了，要不要换个试试？"),
    COMMENT_NOT_EXISIT(2002,"COMMENT不存在"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重新操作"),
    SYSTEM_ERROR(2004,"服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_NOT_EXIST(2005,"评论类型不存在"),
    CONTENT_IS_EMPTY(2006,"评论不能为空" );

    @Override
    public String getMessage() {
        return message;
    }

    private String message;
    private Integer code;

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(int code, String message) {
        this.message = message;
        this.code = code;

    }


}
