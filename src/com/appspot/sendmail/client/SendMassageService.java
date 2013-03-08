package com.appspot.sendmail.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sendMassage")
public interface SendMassageService extends RemoteService {
	public boolean sendMsg(String toEmail, String msgBody, String postcard) throws UnsupportedEncodingException;
}
