package com.credera.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.client.RestClientException;

public class ErrorMessageConverter implements HttpMessageConverter<Object> {
	private final List<MediaType> readableMediaTypes = new ArrayList<MediaType>();
	
	public ErrorMessageConverter() {
		this.readableMediaTypes.add(MediaType.TEXT_HTML);
	}
	
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		if (mediaType != null) {
			for (MediaType readableMediaType : this.readableMediaTypes) {
				if (readableMediaType.includes(mediaType)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	public List<MediaType> getSupportedMediaTypes() {
		return Collections.unmodifiableList(this.readableMediaTypes);
	}

	public Object read(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {		
		Scanner scanner = new Scanner(inputMessage.getBody());
		scanner.useDelimiter("\\A");
	    String message = scanner.hasNext() ? scanner.next() : "";
	    
	    scanner.close();
	    
		throw new RestClientException(message);
	}

	public void write(Object t, MediaType contentType,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		// Never called since "canWrite()" returns false.
		throw new HttpMessageNotWritableException(this.getClass().getName() + " does not support writing");
	}
	
}
