package com.cus.utils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class HttpHelper {

	private HttpHelper() {
	}

	private static class HttpHelperInner {
		private static HttpHelper instance = new HttpHelper();
	}

	public static HttpHelper shareHelper() {
		return HttpHelperInner.instance;
	}

	// 获取源代码
	public String Get(String url, String encoding, String... requestParamAndValues) {
		return Get(url, encoding, null, requestParamAndValues);
	}

	public String Get(String url, String encoding, SSLContext sslContext, String... requestParamAndValues) {

		return Get(url, encoding, sslContext, null, requestParamAndValues);
	}

	// 获取源代码
	public String Get(String url, String encoding, SSLContext sslContext, HttpHelperRequestHandler requestHandler,
			String... requestParamAndValues) {
		if (encoding == null  || encoding.equals("")) {
			encoding = "utf-8";
		}
		BufferedReader reader = null;

		String requestParam = "";
		if (requestParamAndValues != null && requestParamAndValues.length % 2 == 0) {
			for (int i = 0; i < requestParamAndValues.length; i += 2) {
				String name = requestParamAndValues[i];
				String val = requestParamAndValues[i + 1];
				requestParam += name + "=" + val + "&";
			}
			if (requestParam != "") {
				requestParam = requestParam.substring(0, requestParam.length() - 1);
				url += "?" + requestParam;
			}
		}

		try {
			URL uri = new URL(url);
			HttpURLConnection request = (HttpURLConnection) uri.openConnection();
			request.setRequestProperty("Accept-Charset", encoding);

//			// 使用SSL认证
			if (sslContext != null && request instanceof HttpsURLConnection) {
				((HttpsURLConnection) request).setSSLSocketFactory(sslContext.getSocketFactory());
			}

			if (requestHandler != null) {
				requestHandler.OnPreSend(request);
				// request.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
			}

			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), encoding));

			System.out.println("getResponseCode:" + request.getResponseCode());
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				contentBuf.append(line);
			}
			return contentBuf.toString();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error:" + e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {

			}
		}
		return "";

	}

	public String PostData(String url, String... postParamAndValues) {
		return PostData(url, "utf-8", null, postParamAndValues);

	}

	public String PostData(String url, String encoding, String... postParamAndValues) {
		return PostData(url, encoding, null, postParamAndValues);
	}

	public String PostData(String url, SSLContext sslContext, String... postParamAndValues) {
		return PostData(url, "utf-8", sslContext, postParamAndValues);

	}

	public String PostData(String url, String encoding, SSLContext sslContext, String... postParamAndValues) {
		String postData = "";
		if (postParamAndValues != null && postParamAndValues.length % 2 == 0) {
			for (int i = 0; i < postParamAndValues.length; i += 2) {
				String name = postParamAndValues[i];
				String val = postParamAndValues[i + 1];
				postData += name + "=" + val + "&";
			}
			if (postData != "") {
				postData = postData.substring(0, postData.length() - 1);
			}
		}
		return Post(url, postData, encoding, sslContext);
	}

	public String Post(String url, String postData) {
		return Post(url, postData, "utf-8", null);
	}

	public String Post(String url, String postData, String encoding) {
		return Post(url, postData, encoding, null);
	}

	public String Post(String url, String postData, SSLContext sslContext) {
		return Post(url, postData, "utf-8", sslContext);
	}

	public String Post(String url, String postData, String encoding, SSLContext sslContext) {
		return Post(url, postData, encoding,sslContext, null);
	}

	public String Post(String url, String postData, String encoding, SSLContext sslContext,
                       HttpHelperRequestHandler requestHandler) {
		if (encoding == null || encoding.equals("")) {
			encoding = "utf-8";
		}

		PrintWriter write = null;
		BufferedReader reader = null;

		try {
			URL uri = new URL(url);
			// 打开和URL之间的连接
			URLConnection request = uri.openConnection();
			HttpURLConnection connection = (HttpURLConnection)request;
			// 使用SSL认证
			if (sslContext != null && connection instanceof HttpsURLConnection) {
				((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
			}
			if (requestHandler != null) {
				requestHandler.OnPreSend(connection);
				// request.addRequestProperty("Content-type",
				// "application/x-www-form-urlencoded");
			}

			// 发送POST请求必须设置如下两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(2000);
			connection.setReadTimeout(2000);
			// 获取URLConnection对象对应的输出流
			write = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), encoding));
			write.print(postData); // 发送post数据
			write.flush();
			System.out.println("http返回码："+connection.getResponseCode());
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			String line = "";
			StringBuilder contentBuf = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				contentBuf.append(line);
			}
			return contentBuf.toString();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("http请求异常："+e.getMessage());
			System.out.println("http请求异常："+e.toString());
		} finally {
			try {
				if (write != null) {
					write.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {

			}
		}
		return "";
	}

}