package com.bjd.smartanalysis.common;

import lombok.Data;

@Data
public class ResponseData {
    private Integer code;
    private Boolean success;
    private Object data;
    private String msg;

    public ResponseData() {
        this.code = 200;
        this.msg = "";
        this.success = true;
        this.data =  null;
    }

    public static ResponseData OK(Object data) {
        ResponseData rd = new ResponseData();
        rd.setData(data);
        return rd;
    }

    public static ResponseData FAIL(String msg) {
        ResponseData rd = new ResponseData();
        rd.setSuccess(false);
        rd.setMsg(msg);
        return rd;
    }

    public static ResponseData NOAUTH() {
        ResponseData rd = new ResponseData();
        rd.setCode(401);
        rd.setSuccess(false);
        return rd;
    }
}
