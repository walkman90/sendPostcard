package com.appspot.sendmail.server;

import java.io.File;

import com.appspot.sendmail.client.FileList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileListImpl extends RemoteServiceServlet implements FileList {
	
	/**
	 * Getting names of all images on the server
	 */
	@Override
	public String[] getFileList() {
		File folder = new File("images/");
		File[] files = folder.listFiles();
		String[] list = new String[files.length];
		for(int i = 0; i < files.length; i++) {
			list[i] = files[i].getName();
		}
		return list;
	}

	

}
