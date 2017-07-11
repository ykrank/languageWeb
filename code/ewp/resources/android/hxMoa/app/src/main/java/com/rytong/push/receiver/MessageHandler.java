package com.rytong.push.receiver;

import org.androidpn.client.ServiceManager;
import org.androidpn.client.ServiceManager.OnPullSingleMessageListener;
import org.androidpn.client.base.model.BaseMessage;
import org.androidpn.client.base.model.MessageAction;
import org.androidpn.client.listener.ServerMessageListener;


public class MessageHandler implements ServerMessageListener.MessageListener {

	private ViewListener mListener;
	private ServiceManager mServiceManager;
	
	public MessageHandler(ViewListener listener, ServiceManager serviceManager) {
		this.mListener = listener;
		this.mServiceManager = serviceManager;
		ServiceManager.addMessageListener(this);
	}
	
	@Override
	public void onMessage(String messageId, MessageAction action) {
		switch (action) {
		case FLAG_ON_MESSAGE_CLICKED:
		case FLAG_ON_MESSAGE_RECEIVER:
			showMessage(messageId);
			break;
		default:
			break;
		}
	}

	
	public void showMessage(String messageId) {
		mServiceManager.pullBaseMessageById(messageId, new OnPullSingleMessageListener() {
			@Override
			public void onSuccess(final BaseMessage baseMessage) {
				mListener.updateMessageSuccess(baseMessage);
				
			}
			
			@Override
			public void onError(final String errorMsg) {
				mListener.updateMessageError(errorMsg);
				
			}
		});
	}
}
