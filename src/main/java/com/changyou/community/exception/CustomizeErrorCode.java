package com.changyou.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找到问题不在了，要不要换个试试？"),
    COMMENT_NOT_EXISIT(2002,"COMMENT不存在"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重新操作"),
    SYSTEM_ERROR(2004,"服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_NOT_EXIST(2005,"评论类型不存在"),
    CONTENT_IS_EMPTY(2006,"评论不能为空" ),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败")
    ;

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
