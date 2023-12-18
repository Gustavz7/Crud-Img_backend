package com.guz.spring.files.upload.db.message;

//este modelo se usa para llevar el mensaje al usuario en caso de error
public class ResponseMessage {
	private String message;

	public ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
