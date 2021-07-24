package ritman.wallet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import ritman.wallet.di.XMLdi.apicaller.LoginDummyTask;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.os.SystemClock.elapsedRealtime;

public class OnClearFromRecentService  extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ClearFromRecentService", "Service Started");
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            Log.e("next leve;", "Service Started");
            LoginDummyTask task = new LoginDummyTask();
            task.execute();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmService.set(ELAPSED_REALTIME, elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
//        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
//        restartServiceTask.setPackage(getPackageName());
//        restartServiceTask.putExtra("logout","true");
//      //  PendingIntent restartPendingIntent =PendingIntent.getService(getApplicationContext(), 1,restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
//        startService(restartServiceTask);
       // Toast.makeText(getApplicationContext(), "AS onTaskRemoved called", Toast.LENGTH_LONG).show();
       // stopSelf();
    }
}
