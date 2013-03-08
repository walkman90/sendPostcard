package com.appspot.sendmail.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FileListAsync {

	void getFileList(AsyncCallback<String[]> callback);

}
