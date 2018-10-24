package com.mailian.core.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	
	private static final String DEFAULT_ENCODE = "utf-8";

	public static String get(String uri) {
		return get(uri, false);
	}

	public static String get(String uri, boolean isTimeOutControl) {
		log.debug("HttpClientUtil getting uri:" + uri);
		HttpGet httpGet = new HttpGet(uri);
		return execute(httpGet, isTimeOutControl);
	}

	public static String get(String uri, Map<String, Object> params) {
		return get(uri, params, false);
	}

	public static String get(String uri, Map<String, Object> params,
			boolean isTimeOutControl) {
		try {
			return get(toGetParams(uri, params), isTimeOutControl);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String post(String uri, Map<String, Object> params) {
		return post(uri, params, false);
	}

	public static String postBody(String uri, Map<String, Object> params,
			Map<String, String> heads) {
		return postBody(uri, params, heads, false);
	}

	public static String postBody(String uri, String content,
			Map<String, String> heads, boolean isTimeOutControl) {
		HttpPost httppost = new HttpPost(uri);
		if (heads != null) {
			for (Entry<String, String> head : heads.entrySet()) {
				httppost.addHeader(head.getKey(), head.getValue());
			}
		}

		String jsonStr = content;
		log.info("HttpClientUtil postBody URL:" + uri + " body:" + jsonStr);
		StringEntity entity = new StringEntity(jsonStr, HTTP.UTF_8);
		entity.setContentType("application/json");
		httppost.setEntity(entity);
		return execute(httppost, isTimeOutControl);
	}

	public static String postBody(String uri, Object obj,
			Map<String, String> heads, boolean isTimeOutControl) {
		return postBody(uri, JSON.toJSONString(obj), heads, isTimeOutControl);
	}

	public static String post(String uri, Map<String, Object> params,
			boolean isTimeOutControl) {
		HttpPost httppost = new HttpPost(uri);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Entry<String, Object> entry : params.entrySet()) {
			Object value = entry.getValue();
			nvps.add(new BasicNameValuePair(entry.getKey(),
					value != null ? value.toString() : null));
		}
		httppost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		return execute(httppost, isTimeOutControl);
	}
	
	//2018-04-02 
	public static String multipartPost(String uri, Map<String, ContentBody> params,
			boolean isTimeOutControl) {
		HttpPost httppost = new HttpPost(uri);
		/*setConnectTimeout：设置连接超时时间，单位毫秒。setConnectionRequestTimeout：
		设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。setSocketTimeout：
		请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。 */ 
		RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setSocketTimeout(15000).build();
		
		httppost.setConfig(defaultRequestConfig);  
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();  
		 
		for (Entry<String, ContentBody> entry : params.entrySet()) {
			  multipartEntityBuilder.addPart(entry.getKey(), entry.getValue());
		}
		 
		HttpEntity reqEntity = multipartEntityBuilder.build();  
		httppost.setEntity(reqEntity);  
		return execute(httppost, isTimeOutControl);
	}
	

	public static String execute(HttpUriRequest request,
			boolean isTimeOutControl) {
		String out = null;
		HttpClient client = getHttpClient(requestConfig);
//		HttpClient client = new DefaultHttpClient();
//		client.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
//		client.getParams().setParameter(
//				CoreProtocolPNames.HTTP_CONTENT_CHARSET, Consts.UTF_8);
//		// if(isTimeOutControl){
//		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);// 数据传输时间60s
//		client.getParams().setParameter(
//				CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);// 连接时间60s
//		// }
		HttpResponse rsp;
		int status = 0;
		try {
			rsp = client.execute(request);
			status = rsp.getStatusLine().getStatusCode();
			log.info("HttpClientUtil got status:" + status + " URL:"
					+ request.getURI().toString());
			if (status == 200) {
				HttpEntity entity = rsp.getEntity();
				out = EntityUtils.toString(entity);
				//log.info("HttpClientUtil got result:" + out);
			}
		} catch (ClientProtocolException e) {
			log.error("HttpClientUtil found ClientProtocolException:"
					+ e.getMessage());
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error("HttpClientUtil found IOException:" + e.getMessage());
			throw new RuntimeException(e);
		} finally {
//			client.getConnectionManager().shutdown();
		}
		return out;
	}

	/**
	 * url转码
	 * 
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String urlEncode(String value)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(value, DEFAULT_ENCODE);
	}

	/**
	 * 设置get请求的参数
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String toGetParams(String uri, Map<String, Object> params)
			throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(uri)) {
			return null;
		}
		if (params == null || params.size() == 0) {
			return uri;
		}
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (params.get(key) == null)
				continue;
			if (i == 0)
				param.append("?");
			else
				param.append("&");
			param.append(key).append("=")
					.append(urlEncode(String.valueOf(params.get(key))));
			i++;
		}
		return uri + param.toString();
	}
	
	public static CloseableHttpClient createSSLClientDefault(){
		try {
		             @SuppressWarnings("deprecation")
					SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
		                 //信任所有
		                 public boolean isTrusted(X509Certificate[] chain,
		                                 String authType) throws CertificateException {
		                     return true;
		                 }
		             }).build();
		             SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		             return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		         } catch (KeyManagementException e) {
		             e.printStackTrace();
		         } catch (NoSuchAlgorithmException e) {
		             e.printStackTrace();
		         } catch (KeyStoreException e) {
		             e.printStackTrace();
		         }
		         return  HttpClients.createDefault();
		}
	
	/**
     * 最大连接数400
     */
    private static int MAX_CONNECTION_NUM = 400;


    /**
     * 单路由最大连接数80
     */
    private static int MAX_PER_ROUTE = 80;


    /**
     * 向服务端请求超时时间设置(单位:毫秒)
     */
    private static int SERVER_REQUEST_TIME_OUT = 60000;


    /**
     * 服务端响应超时时间设置(单位:毫秒)
     */
    private static int SERVER_RESPONSE_TIME_OUT = 60000;
	
	private static Object LOCAL_LOCK = new Object();
	
	/**
     * 连接池管理对象
     */
	static PoolingHttpClientConnectionManager cm = null;
	
	private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(SERVER_REQUEST_TIME_OUT).setConnectTimeout(SERVER_RESPONSE_TIME_OUT).build();
	
	public static CloseableHttpClient getHttpClient(RequestConfig config) {
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
                .setConnectionManager(getPoolManager())
                .build();
        return httpClient;
    }
	
	/**
     * 
     * 功能描述: <br>
     * 初始化连接池管理对象
     *
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private static PoolingHttpClientConnectionManager getPoolManager() {
        final String methodName = "getPoolManager";
        if (null == cm) {
            synchronized (LOCAL_LOCK) {
                if (null == cm) {
                    SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
                    try {
                        sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                                sslContextBuilder.build());
                        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                                .register("https", socketFactory)
                                .register("http", new PlainConnectionSocketFactory())
                                .build();
                        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                        cm.setMaxTotal(MAX_CONNECTION_NUM);
                        cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }

                }
            }
        }
        return cm;
    }


	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		try {
			String urlNameString = url + "?" + param;
			log.info("sendPost - {}", urlNameString);
			URL realUrl = new URL(urlNameString);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null)
			{
				result.append(line);
			}
			log.info("recv - {}", result);
		} catch (ConnectException e) {
			log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e.getMessage());
		} catch (SocketTimeoutException e) {
			log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e.getMessage());
		} catch (IOException e) {
			log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e.getMessage());
		} catch (Exception e) {
			log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error("调用in.close Exception, url=" + url + ",param=" + param, ex.getMessage());
			}
		}
		return result.toString();
	}
}
