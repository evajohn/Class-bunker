package app.androidhive.info.realm.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.stephentuso.welcome.WelcomeScreenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.adapters.RealmBooksAdapter;
import app.androidhive.info.realm.adapters.dashadapter;
import app.androidhive.info.realm.app.AlarmReceiver;
import app.androidhive.info.realm.app.ParallaxPageTransformer;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class Dashboard extends AppCompatActivity {

    private dashadapter adapter;
    private Realm realm;
    DrawerLayout mDrawerLayout;
    RecyclerView recycler;
    WelcomeScreenHelper welcomeScreen;
    SharedPreferences prefs = null;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinate);
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        welcomeScreen = new WelcomeScreenHelper(this, ParallaxPageTransformer.class);
        welcomeScreen.show(savedInstanceState);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        adapter = new dashadapter(this);
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        String day;
        day=getdate(weekDay);
        this.realm = RealmController.with(this).getRealm();
        RealmResults<Book> main = realm.where(Book.class).equalTo("day", day).findAll();
        setRealmAdapter(main);

        setupRecycler();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id =menuItem.getItemId();
                        if(id == R.id.timetable)
                        {
                            if(RealmController.with(getApplication()).getBooks().size() == 0)
                            {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Add Atleast one subject in Dashboard", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                            else {
                                Intent i = new Intent(Dashboard.this, Main2Activity.class);
                                startActivity(i);
                            }
                        }
                        else if(id ==R.id.setting)
                        {
                            Intent i= new Intent(Dashboard.this,SettingsActivity.class);
                            startActivity(i);
                        }
                        else if(id==R.id.tutorial)
                        {
                            welcomeScreen.forceShow();
                        }
                        else if(id == R.id.dash)
                        {

                                Intent i = new Intent(Dashboard.this, MainActivity.class);
                                startActivity(i);


                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void setRealmAdapter(RealmResults<Book> books) {

        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), books, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        adapter.notifyDataSetChanged();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        adapter.notifyDataSetChanged();

        super.onDestroy();
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
        else if (s.equals("Saturday"))
            return "5";
        else
            return "9";

    }
    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        registerForContextMenu(recycler);

        recycler.setAdapter(adapter);


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        Calendar calendar = Calendar.getInstance();
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        weekDay = dayFormat.format(calendar.getTime());
        String day;
        day=getdate(weekDay);


        RealmResults<Book> main= realm.where(Book.class).equalTo("day","7").findAll();
        RealmResults<Book> fragment = realm.where(Book.class).equalTo("day",day).findAll();
        if(item.getTitle()=="Bunked"){

            if (!fragment.get((int) adapter.getItemId()).isIfbunked()) {
                for(int i=0;i<main.size();i++) {

                    if (fragment.get((int) adapter.getItemId()).getTitle().equals(main.get(i).getTitle())) {
                        realm.beginTransaction();
                        main.get(i).setBunked(main.get(i).getBunked() + 1);
                        if (main.get(i).getTotal_classes() < main.get(i).getBunked()) {
                            main.get(i).setTotal_classes(main.get(i).getBunked());
                            fragment.get((int) adapter.getItemId()).setUpdate(true);
                        }
                        fragment.get((int) adapter.getItemId()).setCancled(false);
                        fragment.get((int) adapter.getItemId()).setIfbunked(true);
                        realm.commitTransaction();
                    }
                }
            }

            adapter.notifyDataSetChanged();

        }
        else if(item.getTitle()=="Cancled"){
            if(fragment.get((int)adapter.getItemId()).isExtra())
            {
                if (fragment.get((int) adapter.getItemId()).isUpdate()) {
                    {

                    }
                }
                    realm.beginTransaction();
                fragment.remove((int) adapter.getItemId());
                realm.commitTransaction();
                adapter.notifyDataSetChanged();
            }
            else {

                if(fragment.get((int) adapter.getItemId()).isIfbunked())
                {            for(int i=0;i<main.size();i++) {

                        if (fragment.get((int) adapter.getItemId()).getTitle().equals(main.get(i).getTitle())) {
                            realm.beginTransaction();
                            main.get(i).setBunked(main.get(i).getBunked() - 1);
                            if (fragment.get((int) adapter.getItemId()).isUpdate()) {
                                main.get(i).setTotal_classes(main.get(i).getBunked());
                            }
                            fragment.get((int) adapter.getItemId()).setCancled(true);
                            fragment.get((int) adapter.getItemId()).setIfbunked(false);
                            realm.commitTransaction();
                            adapter.notifyDataSetChanged();

                        }

                }
                }
                else
                {
                    realm.beginTransaction();
                    fragment.get((int) adapter.getItemId()).setCancled(true);
                    fragment.get((int) adapter.getItemId()).setIfbunked(false);
                    realm.commitTransaction();
                    adapter.notifyDataSetChanged();

                }
                adapter.notifyDataSetChanged();

            }
            adapter.notifyDataSetChanged();
        }
           else{
            return false;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id ==    R.id.action_settings) {
            Intent i= new Intent(Dashboard.this,SettingsActivity.class);
            startActivity(i);
        }
        else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WelcomeScreenHelper.DEFAULT_WELCOME_SCREEN_REQUEST) {
            String welcomeKey = data.getStringExtra(ParallaxPageTransformer.WELCOME_SCREEN_KEY);

            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), welcomeKey + " completed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), welcomeKey + " canceled", Toast.LENGTH_SHORT).show();
            }

        }

    }
    @Override
    protected void onResume() {
        super.onResume();
               adapter.notifyDataSetChanged();
        if (prefs.getBoolean("firstrun", true)) {
            Start();

            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }
    public void Start()
    {

      /*  Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
        updateTime.set(Calendar.HOUR_OF_DAY, 17);
        updateTime.set(Calendar.MINUTE, 00);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent recurringDownload = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(
                Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringDownload);
*/
        Calendar cal = Calendar.getInstance();
        // add 5 minutes to the calendar object
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(Dashboard.this, AlarmReceiver.class);
        // In reality, you would want to have a static variable for the request code instead of 192837
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Toast.makeText(this, "The Subjects will BE updated once a day", Toast.LENGTH_LONG).show();
        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,1, sender);

    }


}
