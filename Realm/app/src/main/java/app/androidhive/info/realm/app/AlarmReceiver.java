package app.androidhive.info.realm.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import app.androidhive.info.realm.activity.Main2Activity;
import app.androidhive.info.realm.activity.MainActivity;
import app.androidhive.info.realm.activity.Updater;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by siva on 17/6/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private Realm realm;

    @Override
    public void onReceive(Context context, Intent arg1) {
        // For our recurring task, we'll just display a message
      /*
                               }*/
        Intent scheduledIntent = new Intent(context, Updater.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);


    }

}