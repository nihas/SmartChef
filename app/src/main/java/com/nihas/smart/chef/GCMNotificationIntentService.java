package com.nihas.smart.chef;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nihas.smart.chef.activities.SplashActivity;
import com.nihas.smart.chef.app.SmartChefApp;


public class GCMNotificationIntentService extends IntentService {
	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	
	private Context context;
	

	@Override
	protected void onHandleIntent(Intent intent) {
		context = getApplicationContext();
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);
	
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				for (int i = 0; i < 3; i++) {
					Log.i("debug", "Working... " + (i + 1) + "/5 @ "
							+ SystemClock.elapsedRealtime());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				if (SmartChefApp.readFromPreferences(getApplicationContext(), Keys.push, true)) {
					String msg = extras.getString("message");
					if(msg != null){	
					String[] msgValues = msg.split("-");
					if (msgValues.length > 1) {
						//if (!msgValues[0].trim().equalsIgnoreCase(DealWithItApp.readFromPreferences(getApplicationContext(), Keys.id, Constants.DEFAULT_STRING))) {
							sendNotification(msgValues[1]);
						//}
					} else {
						sendNotification(extras.getString("message"));
					}

				}else{
					Log.e("Alert","Push notification error");
					//DealWithItApp.showAToast("Push notification error");
				}
			}		
				Log.i("debug", "Received: " + extras.toString());
			} else {
				Log.d("Alert","Push notification error");
				//DealWithItApp.showAToast("Push notification error");
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	@SuppressWarnings("deprecation")
	private void sendNotification(String msg) {

		Log.e("Notification",msg);

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SplashActivity.class), 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		        .setSmallIcon(getNotificationIcon())
		        .setContentTitle(getResources().getString(R.string.app_name))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg)
				.setLights(getResources().getColor(R.color.colorPrimary), 300, 300)
				.setVibrate(new long[] { 100, 250 })
				.setColor(getResources().getColor(R.color.colorPrimary))
				.setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);
		mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	private int getNotificationIcon() {
	    boolean whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	    return whiteIcon ? R.drawable.cup : R.mipmap.ic_launcher;
	}
}