package com.graduation.submission.utils;

/**
 * @项目名称： submission
 * @类名称： IStatusMessage
 * @类描述：响应状态信息
 * @version:
 */
public interface IStatusMessage {
	
	String getCode();

	String getMessage();
	
	public enum SystemStatus implements IStatusMessage{

		SUCCESS("1000","SUCCESS"), //请求成功
		ERROR("1001","ERROR"),	   //请求失败
		PARAM_ERROR("1002","PARAM_ERROR"), //请求参数有误
		SUCCESS_MATCH("1003","SUCCESS_MATCH"), //表示成功匹配
		NO_LOGIN("1100","NO_LOGIN"), //未登录
		MANY_LOGINS("1101","MANY_LOGINS"), //多用户在线（踢出用户）
		UPDATE("1102","UPDATE"), //用户信息或权限已更新（退出重新登录）
		LOCK("1111","LOCK"), //用户已锁定
		EXIT_MOBILE("1200","该手机号已注册过，请换一个手机号"),
		EXIT_USERNAME("1300","用户名已存在，请换一个用户名");
		private String code;
		private String message;

		private SystemStatus(String code,String message){
			this.code = code;
			this.message = message;
		}

		public String getCode(){
			return this.code;
		}

		public String getMessage(){
			return this.message;
		}
	}
}