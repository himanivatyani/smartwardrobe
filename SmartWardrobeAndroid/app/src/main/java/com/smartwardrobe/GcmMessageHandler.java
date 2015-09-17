package com.smartwardrobe;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.smartwardrobe.object.Cloth;

/**
 * Created by leechunhoe on 13/9/15.
 */
public class GcmMessageHandler extends NotificationsHandler
{
    public static final int NOTIFICATION_ID = 5;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context context;

    @Override
    public void onRegistered(Context context, final String gcmRegistrationId)
    {
        super.onRegistered(context, gcmRegistrationId);

        new AsyncTask<Void, Void, Void>()
        {

            protected Void doInBackground(Void... params)
            {
                try
                {
                    MainActivity.mClient.getPush().register(gcmRegistrationId, null);
                    return null;
                } catch (Exception e)
                {
                    // handle error
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onReceive(Context context, Bundle bundle)
    {
        Cloth cloth = (Cloth) bundle.getSerializable(Cloth.KEY_SELF);

        if (cloth == null || cloth.getAction().equals("register"))
        {
            sendReminderNotification(context);
        } else if (cloth.getAction().equals("reminder"))
        {
            sendRegisterNotification(context);
        }
    }


    public static void sendRegisterNotification(Context context)
    {
        String msg = "1 new cloth added. Click to register.";

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, UpdateClothDetailsActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.hanger)
                        .setContentTitle("SmartWardrobe")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public static void sendReminderNotification(Context context)
    {
        String msg = "Your cloth Little Grey has been ignored for 10 years. Please wear it or sell/donate!";

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, UpdateClothDetailsActivity.class), 0);

        Bitmap bitmap = BitmapFactory.decodeFile(Values.APP_ROOT + "3333333333333333.jpg");

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0,
                bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                bitmap.getWidth(),
                bitmap.getWidth());

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.hanger)
                        .setLargeIcon(croppedBitmap)
                        .setContentTitle("SmartWardrobe")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


//    String mes;
//    private Handler handler;
//
//    public GcmMessageHandler()
//    {
//        super("GcmMessageHandler");
//    }
//
//    @Override
//    public void onCreate()
//    {
//        // TODO Auto-generated method stub
//        super.onCreate();
//        handler = new Handler();
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent)
//    {
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);
//
//        mes = extras.getString("rfid");
//
//        handler.post(new Runnable()
//        {
//            public void run()
//            {
//                Toast.makeText(getApplicationContext(), "rfid = " + mes, Toast.LENGTH_LONG).show();
//            }
//        });
//
//        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("rfid"));
//
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
//    }
}
