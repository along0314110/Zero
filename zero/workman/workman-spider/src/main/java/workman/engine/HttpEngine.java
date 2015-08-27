package workman.engine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 引擎<br>
 * 用于启动不同的抓取任务
 *
 * @anthor yebin
 * @data 2015年8月26日
 */
public class HttpEngine {
	private static final Logger LOG = LogManager.getLogger(HttpEngine.class);

	public static final int STATUS_UNREADY = -1;// 未准备
	public static final int STATUS_READY = 0;// 准备
	public static final int STATUS_RUNNING = 1;// 正在运行
	public static final int STATUS_CLOSED = 2;// 已关闭
	private final String CHARSET = "UTF-8";
	private int status = STATUS_UNREADY;

	private HttpAsyncClient client;

	public HttpEngine() {
		CookieStore cookieStore = new BasicCookieStore();
		HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
		client = builder.setDefaultCookieStore(cookieStore).build();
		status = STATUS_READY;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param callback
	 */
	public void get(final String url, CallBack<String> callback) {
		client.execute(new HttpGet(url), new ResponseCallBack(url, callback));
	}

	/**
	 * post请求
	 * 
	 * @param uri
	 * @param params
	 * @param callback
	 */
	public void post(final String uri, final Map<String, String> params,
			final CallBack<String> callback) {
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for (Entry<String, String> e : params.entrySet()) {
			formParams.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formParams, CHARSET);
			post.setEntity(uefEntity);
			client.execute(post, new ResponseCallBack(uri, params, callback));
		} catch (UnsupportedEncodingException e) {
			LOG.error("请求不支持编码：" + CHARSET, e);
		}
	}

	/**
	 * 获得状态
	 * 
	 * @return
	 */
	public int status() {
		return status;
	}

	class ResponseCallBack implements FutureCallback<HttpResponse> {
		private String url;
		private String uri;
		private Map<String, String> params;
		private CallBack<String> callback;

		public ResponseCallBack(String url, CallBack<String> callBack) {
			this.url = url;
			this.callback = callBack;
		}

		public ResponseCallBack(String uri, Map<String, String> params,
				CallBack<String> callBack) {
			this.uri = uri;
			this.params = params;
			this.callback = callBack;
		}

		public void failed(Exception ex) {
			LOG.error("请求失败：" + (url != null ? url : uri + "，" + params), ex);
		}

		public void completed(HttpResponse result) {
			StatusLine statusLine = result.getStatusLine();
			LOG.info("status line：" + statusLine);
			if (statusLine.getStatusCode() == 200) {
				LOG.info("请求成功：status line " + statusLine);
				HttpEntity entity = result.getEntity();
				try {
					String str = EntityUtils.toString(entity, CHARSET);
					LOG.debug("callback：" + str);
					callback.completed(str);
				} catch (ParseException e) {
					LOG.error("请求解析异常", e);
					LOG.debug("charset " + CHARSET + "：" + entity);
				} catch (IOException e) {
					LOG.info("IO异常", e);
					LOG.debug("charset " + CHARSET + "：" + entity);
				}
			} else {
				LOG.info("请求异常：status line " + statusLine);
			}
		}

		public void cancelled() {
			LOG.info("请求取消：" + (url != null ? url : uri + "，" + params));
		}
	}
}
