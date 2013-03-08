package com.appspot.sendmail.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("fileList")
public interface FileList extends RemoteService {
	public String[] getFileList();
}
