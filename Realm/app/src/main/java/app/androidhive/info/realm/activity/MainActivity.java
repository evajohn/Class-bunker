package app.androidhive.info.realm.activity;

import android.app.AlarmManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.stephentuso.welcome.WelcomeScreenHelper;

import java.util.Calendar;
import java.util.TimeZone;

import app.androidhive.info.realm.adapters.TestAdapter;
import app.androidhive.info.realm.app.AlarmReceiver;
import app.androidhive.info.realm.R;
import app.androidhive.info.realm.adapters.BooksAdapter;
import app.androidhive.info.realm.adapters.RealmBooksAdapter;
import app.androidhive.info.realm.app.ParallaxPageTransformer;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import atownsend.swipeopenhelper.SwipeOpenItemTouchHelper;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements TestAdapter.ButtonCallbacks {

    private TestAdapter adapter1;
    private Realm realm;
    private CoordinatorLayout coordinatorLayout;
    private SwipeOpenItemTouchHelper helper;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;
Toolbar toolbar;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
       coordinatorLayout = (CoordinatorLayout) findViewById(R.id
               .coordinate);

       fab = (FloatingActionButton) findViewById(R.id.fab);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        //get realm instance
        this.realm = RealmController.with(this).getRealm();
        //set toolbar
      /*   if (savedInstanceState != null) {
            helper.restoreInstanceState(savedInstanceState);
        }*/
       toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       ActionBar supportActionBar = getSupportActionBar();
           supportActionBar.setDisplayHomeAsUpEnabled(true);
        setupRecycler();
        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getBooks());


        //add new item
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflater = MainActivity.this.getLayoutInflater();
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editTitle = (EditText) content.findViewById(R.id.title);
                final EditText bunked = (EditText) content.findViewById(R.id.bunked);
                final EditText number = (EditText) content.findViewById(R.id.no);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(content)
                        .setTitle("Add Classes")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Book book = new Book();
                                int i=7;
                                book.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());

                                book.setTitle( editTitle.getText().toString());
                                book.setDay(Integer.toString(i));
                                if (editTitle.getText() == null || editTitle.getText().toString().equals("") || editTitle.getText().toString().equals(" ")) {
                                    Toast.makeText(MainActivity.this, "Entry not saved, missing title", Toast.LENGTH_SHORT).show();
                                }
                                else if (!TextUtils.isDigitsOnly(bunked.getText()))
                                {
                                    Toast.makeText(MainActivity.this, "Entry not saved, Wrong Input", Toast.LENGTH_SHORT).show();
                                }
                                else if (!TextUtils.isDigitsOnly(number.getText()))
                                {
                                    Toast.makeText(MainActivity.this, "Entry not saved, Wrong Input", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    if (bunked.getText() == null || bunked.getText().toString().equals("") || bunked.getText().toString().equals(" ")) {
                                        book.setBunked(0);
                                    }
                                    else
                                        book.setBunked(Integer.parseInt(bunked.getText().toString()));

                                    if (number.getText() == null || number.getText().toString().equals("") || number.getText().toString().equals(" ")) {
                                        book.setTotal_classes(0);
                                    }
                                    else
                                        book.setTotal_classes(Integer.parseInt(number.getText().toString()));

                                    // Persist your data easily
                                    if(book.getTotal_classes()<book.getBunked())
                                    {
                                        book.setTotal_classes(book.getBunked());
                                    }
                                    realm.beginTransaction();
                                    realm.copyToRealm(book);
                                    realm.commitTransaction();
                                    adapter1.notifyDataSetChanged();

                                    // scroll the recycler view to bottom
                                    recycler.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }


    public void setRealmAdapter(RealmResults<Book> books) {

        RealmResults<Book> siva =realm.where(Book.class).equalTo("day","7").findAll();

        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getApplicationContext(), siva, true);
        // Set the data and tell the RecyclerView to draw
        adapter1.setRealmAdapter(realmAdapter);
        adapter1.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu,menu);
    return true;
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
        adapter1 = new TestAdapter(this,false,this);
        helper = new SwipeOpenItemTouchHelper(new SwipeOpenItemTouchHelper.SimpleCallback(
                SwipeOpenItemTouchHelper.START | SwipeOpenItemTouchHelper.END));

        recycler.setAdapter(adapter1);
        helper.attachToRecyclerView(recycler);
        helper.setCloseOnAction(false);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
         else if (id ==    R.id.action_settings) {
            Intent i= new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void removePosition(final int position) {
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RealmResults<Book> temp = realm.where(Book.class).equalTo("day","7").findAll();
        final Book book = temp.get(position);

        View content = inflater.inflate(R.layout.delete, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(content)
                .setTitle("Remove Classes")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        realm.beginTransaction();
                        delete(book.getTitle());
                        realm.commitTransaction();
                        adapter1.notifyDataSetChanged();

                    }

                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public  void modify(String s,String s1)
    {
        RealmResults<Book> siva =realm.where(Book.class).findAll();
        for(int i=0;i<siva.size();i++)
        {
            if(siva.get(i).getTitle().equals(s1))
            {
                siva.get(i).setTitle(s);
            }
        }
    }
    public  void delete(String s1)
    {
        RealmResults<Book> siva =realm.where(Book.class).findAll();
        for(int i=0;i<siva.size();i++)
        {
            if(siva.get(i).getTitle().equals(s1))
            {
                siva.get(i).removeFromRealm();
                i=-1;
            }

        }
    }
    @Override
    public void editPosition(final int position) {
        RealmResults<Book> siva =realm.where(Book.class).equalTo("day","7").findAll();
        final Book book = siva.get(position);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.edit_item, null);
        final EditText editTitle = (EditText) content.findViewById(R.id.title);
        final EditText bunked = (EditText) content.findViewById(R.id.bunked);
        final EditText total = (EditText) content.findViewById(R.id.no);
        editTitle.setText(book.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(content)
                .setTitle("Edit Book")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Book> results = realm.where(Book.class).findAll();
                        realm.beginTransaction();
                        if(!editTitle.getText().toString().equals(book.getTitle()))
                        {
                            modify(editTitle.getText().toString(),book.getTitle());
                        }
                        if (bunked.getText() == null || bunked.getText().toString().equals("") || bunked.getText().toString().equals(" ")) {
                        } else {
                            results.get(position).setBunked(Integer.parseInt(bunked.getText().toString()));
                        }
                        if (total.getText() == null || total.getText().toString().equals("") || total.getText().toString().equals(" ")) {
                        } else {
                            results.get(position).setTotal_classes(Integer.parseInt(total.getText().toString()));
                        }

                        results.get(position).setTitle(editTitle.getText().toString());

                        if(results.get(position).getTotal_classes()<results.get(position).getBunked())
                        {
                            results.get(position).setTotal_classes(results.get(position).getBunked());
                        }
                        realm.commitTransaction();

                        adapter1.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }
}