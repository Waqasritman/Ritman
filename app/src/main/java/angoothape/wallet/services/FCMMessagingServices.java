package angoothape.wallet.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import angoothape.wallet.NewSplash;
import angoothape.wallet.R;

public class FCMMessagingServices extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "channel_01";
    private Map<String, String> data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null) {
            data = remoteMessage.getData();
            //   Log.e("onMessageReceived: ", data.get(MESSAGE));
            if (data.size() == 0) {

                if (remoteMessage.getNotification().getImageUrl() != null) {
                    sendImageNotification(remoteMessage.getNotification().getTitle()
                            , remoteMessage.getNotification().getBody()
                            , remoteMessage.getNotification().getImageUrl());
                } else {
                    sendNoti(remoteMessage.getNotification().getTitle()
                            , remoteMessage.getNotification().getBody());
                }


            }
        }
    }


    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


    private void sendImageNotification(String title, String message, Uri uri) {

        Bitmap image = getBitmapFromUrl(uri.toString());

        int notificationId = 2017;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }


//        Intent intent = new Intent();
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(NewSplash.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent notificationPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification.Builder notificationBuilder = new Notification.Builder
                (this).setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(
                        new Notification.BigTextStyle()
                                .bigText(message))
                .setStyle(new Notification.BigPictureStyle().bigPicture(image))
                .setSmallIcon(R.mipmap.icon)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, notificationBuilder
                    .build());
        }
    }


    private void sendNoti(String title, String message) {
        int notificationId = 2017;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }


        Intent intent = new Intent();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification.Builder notificationBuilder = new Notification.Builder
                (this).setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(
                        new Notification.BigTextStyle()
                                .bigText(message))
//                .setStyle(new Notification.BigPictureStyle().bigPicture(image))
                .setSmallIcon(R.mipmap.icon)
                .setContentIntent(notificationPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, notificationBuilder
                    .build());
        }
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
    }
}
