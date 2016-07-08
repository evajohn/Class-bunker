package app.androidhive.info.realm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by siva on 17/6/16.
 */
public class Updater extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        this.realm = RealmController.getInstance().getRealm();
        String day;
        day=getdate(weekDay);
        RealmResults<Book> siva = realm.where(Book.class).equalTo("day", "7").findAll();
        if(siva.size()>0) {
            RealmResults<Book> temp = realm.where(Book.class).equalTo("day", day).findAll();
            int[] x = new int[siva.size()];
            int count = 0, i = 0;
            for (int jw=0;jw<siva.size();jw++)
            {    count = 0;
                for (int q=0;q<temp.size();q++) {

                    if (temp.get(q).isUpdate()) {
                             break;
                    }
                    if (siva.get(jw).getTitle().equals(temp.get(q).getTitle())) {

                        if (!temp.get(q).isCancled()) {

                            count++;

                        }
                    }

                }
                x[i] = count;
                i++;
            }


            for (int l = 0; l < siva.size(); l++) {

                realm.beginTransaction();
                int j = siva.get(l).getTotal_classes();
                j = j + x[l];
                siva.get(l).setTotal_classes(j);
                realm.commitTransaction();
            }

            for (int l = 0; l < temp.size(); l++) {

                realm.beginTransaction();
                temp.get(l).setCancled(false);
                temp.get(l).setUpdate(false);
                temp.get(l).setIfbunked(false);

                if (temp.get(l).isExtra()) {
                    temp.remove(l);
                }

                realm.commitTransaction();

            }
        }
        Intent j = new Intent(Updater.this,Dashboard.class);
        startActivity(j);
    }
    public String getdate(String s)
    {
        if(s.equals("Monday"))
            return "0";
        else if (s.equals("Tuesday"))
            return "1";
        else if (s.equals("Wednesday"))
            return "2";
        else if (s.equals("Thursday"))
            return "3";
        else if (s.equals("Friday"))
            return "4";
        else if (s.equals("Satruday"))
            return "5";
        else
            return "7";

    }
}
