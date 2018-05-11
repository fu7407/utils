package com.zzf.common.net.testDomain.req;

public class BaseRespArg {
	
	public int errorCode = 0;	
	public String errorMsg = "Success";
	
	
	public void setErrorCode(int errCode,String errMsg)
	{
		errorCode = errCode;
		errorMsg = errMsg;
	}


}
