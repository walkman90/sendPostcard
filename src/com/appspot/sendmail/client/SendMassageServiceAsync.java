package com.appspot.sendmail.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SendMassageServiceAsync {

	void sendMsg(String toEmail, String msgBody, String postcard,
			AsyncCallback<Boolean> callback);

}
