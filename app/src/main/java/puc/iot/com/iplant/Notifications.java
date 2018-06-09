package puc.iot.com.iplant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import java.util.Arrays;

public final class Notifications {
    private static void expanded(Context context, String[] contentText, int iconID,int titleID){
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), iconID);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,contentText[0])
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setColorized(true)
                        .setLargeIcon(icon)
                        .setColor(ContextCompat.getColor(context,R.color.white))
                        .setContentTitle(context.getString(titleID));

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.setBigContentTitle(context.getString(titleID));
        for (String plantName : contentText) {

            inboxStyle.addLine(plantName);
        }
        mBuilder.setStyle(inboxStyle);

        Intent resultIntent = new Intent(context, PlantActivity.class);
        resultIntent.putExtra(Plant.ID,contentText);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(PlantActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.notify(Arrays.hashCode(contentText), mBuilder.build());
        }
    }

    private static void simple(Context context, String plantId, String contentText,int iconID,int titleID){
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), iconID);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context,plantId)
                        .setSmallIcon(R.drawable.ic_menu_send)
                        .setLargeIcon(icon)
                        .setColor(ContextCompat.getColor(context,R.color.white))
                        .setContentTitle(context.getString(titleID))
                        .setContentText(contentText);
        Intent resultIntent = new Intent(context, PlantActivity.class);
        resultIntent.putExtra(Plant.ID,plantId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(PlantActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.notify(plantId.hashCode(), mBuilder.build());
        }
    }

    public static void needTurnOnWater(Context context, String plantId, String name){
        simple(context,
                plantId,
                name,
                R.drawable.notf_d,
                R.string.I_want_water);
    }

    public static void needTurnOnWater(Context context, String[] names){
        expanded(context,
                names,
                R.drawable.notf_d,
                R.string.I_want_water);
    }
    public static void needTurnOffWater(Context context, String plantId, String name){
        simple(context,
                plantId,
                name,
                R.drawable.notf_w,
                R.string.someone_is_drowning);
    }
    public static void needTurnOffWater(Context context, String names[]){
        expanded(context,
                names,
                R.drawable.notf_w,
                R.string.someone_is_drowning);
    }
    public static void wasWatered(Context context, String plantId, String plantName,String userName){
        simple(context,
                plantId,
                plantName + ": "+userName,
                R.drawable.notf_n,
                R.string.someone_watered_your_plants);
    }
    public static void wasWatered(Context context, String[] plantNames,String userName){

        expanded(context,
                plantNames,
                R.drawable.notf_n,
                R.string.someone_watered_your_plants);
    }

}
