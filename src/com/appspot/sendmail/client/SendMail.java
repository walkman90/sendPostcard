package com.appspot.sendmail.client;


import java.util.ArrayList;

import com.google.appengine.labs.repackaged.com.google.common.io.Files;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SendMail implements EntryPoint {
	
	private SuggestBox toEmail = new SuggestBox();
	private RichTextArea area = new RichTextArea();
	private RichTextToolbar toolbar = new RichTextToolbar(area);
	private Grid grid = new Grid(2, 1);
	private Label greeting = new Label("Greeting: ");
	private Button sendButton = new Button("Send");
	private FlexTable table = new FlexTable();
	private ArrayList<Image> images = new ArrayList<Image> ();
	private String chosenCard = "";
	private FileListAsync remoteService = GWT.create(FileList.class); 
	private SendMassageServiceAsync sendMsgService = GWT.create(SendMassageService.class);
	private DialogBox showImg = new DialogBox();
	private DialogBox showAnim = new DialogBox();
	private VerticalPanel info = new VerticalPanel();
	
	public void onModuleLoad() {
		table.setStyleName("imgTable");
		table.setStyleName("MyApp");
		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
			Window.alert("Something went wrong");
			}

			@Override
			public void onSuccess(String[] result) {
				addImages(result);
			}
		};	
		remoteService.getFileList(callback);
		RootPanel.get("tableContainer").add(table);
		
	    area.ensureDebugId("cwRichText-area");
	    area.setSize("100%", "14em"); 
	    toolbar.ensureDebugId("cwRichText-toolbar");
	    toolbar.setWidth("100%");

	    grid.setStyleName("cw-RichText");
	    grid.setWidget(0, 0, toolbar);
	    grid.setWidget(1, 0, area);
		
	    showAnim.add(new Image("animation/animation.gif"));
	    showAnim.setGlassEnabled(true);
		showAnim.setPopupPosition(Window.getClientWidth()/2 - 245, 200);
	    sendButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				checkInputs();
			}
		});
		toEmail.getElement().setAttribute("placeHolder", "To: Email");
		toEmail.setWidth("300px");
		info.add(toEmail);
		info.add(greeting);
		info.add(grid);
		info.add(sendButton);
		RootPanel.get("inputInfo").add(info);
	
	}
	
	/**
	 * Adding images getting from the server to the page
	 * @param images - Array of images links
	 */
	private void addImages(String[] images) {
		int row = 0;
		int column = 0;
		for (int i = 0; i < images.length; i++) {
			final Image img = new Image("images/" + images[i]);
			img.setSize("180px", "130px");
			img.setStyleName("image");
			img.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					createDialogBox(img.getUrl());
				}
			});
			table.setWidget(row, column++, img);
			if(column == 4) {
				row++;
				column = 0;
			}
		}
	}
	
	/**
	 * Creates dialog box with full-size image and select button
	 * @param url - URL to the selected image
	 */
	private void createDialogBox(String url) {
		final String imgURL = url;
		showImg.setGlassEnabled(true);
		showImg.setPopupPosition(Window.getClientWidth()/2 - 245, 100);	
		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();	
		Button dialogCloseBt = new Button("Close");
		dialogCloseBt.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showImg.hide();
			}
		});
		Button chooseButton = new Button("Select");
		chooseButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				chosenCard = imgURL;
				showImg.hide();
				selectImgInTable(chosenCard);
			}
		});
		hPanel.add(dialogCloseBt);
		hPanel.add(chooseButton);
		vPanel.add(new Image(url));
		vPanel.add(hPanel);
		showImg.setWidget(vPanel);
		showImg.show();
	}
	
	/**
	 * Adding colored border for the chosen image
	 * @param imgURL - URL of the chosen image
	 */
	private void selectImgInTable(String imgURL) {
		final int row = table.getRowCount();
		final int column = table.getCellCount(0);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				if( ((Image)table.getWidget(i, j)).getUrl().equals(imgURL)) {
					table.getWidget(i, j).setStyleName("selectedImage");
				} else {
					table.getWidget(i, j).setStyleName("image");
				}
			}
		}
	}
	
	/**
	 * Checking of email and text area
	 */
	private void checkInputs() {
		if(chosenCard == "") {
			Window.alert("Please, choose the postcard.");
			return;
		} else if (!toEmail.getValue().matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$")) {
			Window.alert("To Email address is invalid.");
			return;
		} else if (area.getText().isEmpty()) {
			Window.alert("Please, input a greeting.");
			return;
		}
		showAnim.show();
		sendMassage();
	}
	
	/**
	 * Sending info to the server using RPC
	 */
	private void sendMassage() {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				showAnim.show();
				Window.alert("Something went wrong");
			}

			@Override
			public void onSuccess(Boolean result) {
				showAnim.hide();
				Window.alert("Your message was succesfully sent! " +
								"Thank you for using our service!");
				toEmail.setText("");
				area.setText("");
			}
			
		};
		String card = chosenCard.substring(chosenCard.lastIndexOf("/")+1);
		sendMsgService.sendMsg(toEmail.getValue(),
				area.getHTML(), card, callback);
	}
}
