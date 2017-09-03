package singledevapps.wvstask.sync;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import singledevapps.wvstask.MainActivity;
import singledevapps.wvstask.R;
import singledevapps.wvstask.helper.Apis;
import singledevapps.wvstask.helper.NetworkCalls;
import singledevapps.wvstask.helper.NewsSouce;
import singledevapps.wvstask.helper.SharedPrefHelper;


public class NewsJob extends Job {

    public static final String TAG = "newsJob";
    private Context context;

    public NewsJob(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("News Headlines")
                .setContentText("Notification from Android Job Demo App.")
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext())
                .notify(new Random().nextInt(), notification);
        for (String aNewsSourceList : NewsSouce.newsSourceList) {
            NetworkCalls nc = new NetworkCalls();
            String jsonStr = nc.getServerCall(Apis.getHitUrl(aNewsSourceList));
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                    SharedPrefHelper.saveJsonOffline(context.getApplicationContext(), jsonStr, aNewsSourceList);
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return Result.SUCCESS;
    }

    public static void schedulePeriodic() {
        new JobRequest.Builder(NewsJob.TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(8), TimeUnit.HOURS.toMillis(1))
                .setUpdateCurrent(true)
                .setPersisted(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }
}
