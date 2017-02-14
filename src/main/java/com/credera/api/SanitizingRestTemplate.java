package com.credera.api;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

public class SanitizingRestTemplate extends RestTemplate {
	static final String[] headersToSanitize = {
		HttpHeaders.CONTENT_TYPE,
		HttpHeaders.CONTENT_TYPE.toLowerCase(),
		HttpHeaders.CONTENT_TYPE.toUpperCase(),
		HttpHeaders.CONTENT_LENGTH,
		HttpHeaders.CONTENT_LENGTH.toLowerCase(),
		HttpHeaders.CONTENT_LENGTH.toUpperCase(),
	};

	public SanitizingRestTemplate() {
		super();
	}

	public SanitizingRestTemplate(ClientHttpRequestFactory requestFactory) {
		super(requestFactory);
	}

	public SanitizingRestTemplate(
			List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}
	
	protected Object sanitize(Object requestBody) {
		if (requestBody instanceof HttpEntity) {
			HttpEntity<?> httpEntity = (HttpEntity<?>)requestBody;
			
			HttpHeaders sanitizedHttpHeaders = new HttpHeaders();
			sanitizedHttpHeaders.putAll(httpEntity.getHeaders());
			
			for (String headerToSanitize : headersToSanitize) {
				sanitizedHttpHeaders.remove(headerToSanitize);
			}
			
			requestBody = new HttpEntity<Object>(httpEntity.getBody(), sanitizedHttpHeaders);
		}
		
		return requestBody;
	}
	
	@Override
	protected <T> RequestCallback httpEntityCallback(Object requestBody) {		
		return super.httpEntityCallback(sanitize(requestBody));
	}

	@Override
	protected <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
		return super.httpEntityCallback(sanitize(requestBody), responseType);
	}
}
