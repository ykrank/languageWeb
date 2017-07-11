package com.rytong.push.receiver;

import org.androidpn.client.NotifierNew;
import org.androidpn.client.base.BasePushReceiver;

import com.ghbank.moas.R;


public class MyPushReceiver extends BasePushReceiver{
	
	@Override
	protected void adjustNotifierPropertyBeforeShown(NotifierNew notifier) {
		// TODO Auto-generated method stub
		notifier.setNotificationIcon(R.drawable.icon);
		super.adjustNotifierPropertyBeforeShown(notifier);
	}
}
