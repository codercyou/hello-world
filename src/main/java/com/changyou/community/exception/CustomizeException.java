package com.changyou.community.exception;

public class CustomizeException extends RuntimeException {

        private String message;
        private Integer code;

        public CustomizeException(ICustomizeErrorCode errorCode) {
            this.message = errorCode.getMessage();
            this.code = errorCode.getCode();
        }

        public CustomizeException(String message) {
            this.message = message;
        }


        public Integer getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }

}
