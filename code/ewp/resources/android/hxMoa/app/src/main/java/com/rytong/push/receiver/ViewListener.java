package com.rytong.push.receiver;

import org.androidpn.client.base.model.BaseMessage;

public interface ViewListener {

	void updateMessageSuccess(final BaseMessage baseMessage);
	
	void updateMessageError(final String errorMsg);
}
