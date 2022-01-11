package com.hzfc.management.jsbsb.common.outapi;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。
 * </p>
 *
 * <p>
 * 不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
 * </p>
 *
 * <p>
 * IJPay 交流群: 723992875
 * </p>
 *
 * <p>
 * Node.js 版: https://gitee.com/javen205/TNWX
 * </p>
 *
 * <p>
 * 支付宝支付相关接口
 * </p>
 *
 * @author Javen
 */
public class AliPayApi {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	public static <T extends AlipayResponse> T doExecute(AlipayRequest<T> request) throws AlipayApiException {
		//if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
		//	return certificateExecute(request);
		//} else {
			return execute(request);
		//}
	}
	
	public static <T extends AlipayResponse> T doExecute(AlipayClient alipayClient, Boolean certModel, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		if (certModel) {
			return certificateExecute(alipayClient,request);
		} else {
			return execute(alipayClient,request);
		}
	}
	
	
	public static <T extends AlipayResponse> T doExecute(AlipayClient alipayClient, Boolean certModel, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		if (certModel) {
			return certificateExecute(alipayClient,request,authToken);
		} else {
			return execute(alipayClient,request,authToken);
		}
	}
	

	public static <T extends AlipayResponse> T doExecute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
			return certificateExecute(request, authToken);
		} else {
			return execute(request, authToken);
		}
	}
	
	
	public static <T extends AlipayResponse> T doExecute(AlipayClient alipayClient,AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
			return certificateExecute(alipayClient,request, authToken);
		} else {
			return execute(alipayClient,request, authToken);
		}
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
		AlipayClient aliPayClient = AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient();
		return aliPayClient.execute(request);
	}
	

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request);
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, authToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request, authToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, accessToken, appAuthToken, targetAppId);
	}

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request, accessToken, appAuthToken, targetAppId);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.pageExecute(request);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request, String method) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request, method);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayClient alipayClient, AlipayRequest<T> request, String method) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.pageExecute(request, method);
	}

	public static <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().sdkExecute(request);
	}

	public static <T extends AlipayResponse> T sdkExecute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.sdkExecute(request);
	}


	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, authToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request, authToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, accessToken, appAuthToken, targetAppId);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId)
			throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request, accessToken, appAuthToken, targetAppId);
	}



	/**
	 * 统一收单交易支付接口接口 <br>
	 * 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param model
	 *            {@link AlipayTradePayModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {@link AlipayTradePayResponse}
	 * @throws AlipayApiException
	 *             支付宝 Api 异常
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		// 填充业务参数
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(request);
	}
	
	/**
	 * 统一收单交易支付接口接口 <br>
	 * 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param alipayClient
	 *            {@link AlipayClient}
	 * @param certModel
	 *				是否证书模式	           
	 * @param model
	 *            {@link AlipayTradePayModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {@link AlipayTradePayResponse}
	 * @throws AlipayApiException
	 *             支付宝 Api 异常
	 *//*
	public static AlipayTradePayResponse tradePayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		// 填充业务参数
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(alipayClient,certModel,request);
	}*/


	/**
	 * 统一收单线下交易预创建 <br>
	 * 适用于：扫码支付等 <br>
	 *
	 * @param model
	 *            {@link AlipayTradePrecreateModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {@link AlipayTradePrecreateResponse}
	 * @throws AlipayApiException
	 *             支付宝 Api 异常
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(request);
	}








}
