package com.guz.spring.files.upload.db.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.guz.spring.files.upload.db.message.ResponseFile;

public class AsyncRequestUtils {
	private static final Logger logger = LoggerFactory.getLogger(AsyncRequestUtils.class);

	public static DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config().setConnectTimeout(500);
	public static final AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

	private AsyncRequestUtils() {
	}

	public static Response get(String url) {
		Response response = null;
		Future<Response> whenResponse = client.prepareGet(url).execute();
		try {
			response = whenResponse.get();
		} catch (InterruptedException e) {
			logger.error("error en el proceso asincrono", e);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e1) {
			logger.error("se ha interrumpido el proceso asincrono", e1);
		}
		return response;
	}

	public static ResponseFile getResponsedFile(String url) {
		ResponseFile responseFile = null;
		String fileTitle = "file";
		try {
			URL urlObj = new URL(url);
			fileTitle = urlObj.getHost();
		} catch (MalformedURLException e) {
			logger.error("url invalida ===> "+url, e);
			return responseFile;
		}
		Response response = get(url);
		if (response == null)
			return responseFile;
		if (HttpStatus.OK.value() == (response.getStatusCode())) {
			responseFile = new ResponseFile();
			responseFile.setName(String.valueOf(System.currentTimeMillis()));
			responseFile.setSize(response.getResponseBodyAsBytes().length);
			responseFile.setFile(response.getResponseBodyAsBytes());
			responseFile.setType(response.getContentType());
			responseFile.setTitle(fileTitle);
		}
		return responseFile;
	}

	public static <T> boolean post(String url, T object) {
		boolean result = false;

		return result;
	}
}
