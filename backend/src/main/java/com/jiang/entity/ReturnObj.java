package com.jiang.entity;

import java.util.Map;

public class ReturnObj {
private Integer code;
private String msg;
private Map<String, String> result;

public static ReturnObj success(int code, String msg){
	ReturnObj obj = new ReturnObj();
	obj.setCode(code);
	obj.setMsg(msg);
	return obj;
}

public Integer getCode() {
	return code;
}

public void setCode(Integer code) {
	this.code = code;
}

public String getMsg() {
	return msg;
}

public void setMsg(String msg) {
	this.msg = msg;
}

public Map<String, String> getResult() {
	return result;
}

public void setResult(Map<String, String> result) {
	this.result = result;
}
}

