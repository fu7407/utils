package com.zzf.common.net.testDomain.req;

public class CheckDomainResp extends BaseRespArg{

	/**
	 * registered=false:表示此域名可以注册 registered=true:表示此域名不能注册
	 */
	public boolean isRegistered;
	
}
