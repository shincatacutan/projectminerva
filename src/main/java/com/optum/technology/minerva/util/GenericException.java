package com.optum.technology.minerva.util;

public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errCode;
	private String errMsg;

	//getter and setter methods

	public GenericException(String errCode, String errMsg) {
		this.setErrCode(errCode);
		this.setErrMsg(errMsg);
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
