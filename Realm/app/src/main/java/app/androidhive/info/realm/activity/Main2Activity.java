package app.androidhive.info.realm.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import app.androidhive.info.realm.Fragment.Tuesday;
import app.androidhive.info.realm.Fragment.friday;
import app.androidhive.info.realm.Fragment.monday;
import app.androidhive.info.realm.Fragment.saturday;
import app.androidhive.info.realm.Fragment.thursday;
import app.androidhive.info.realm.Fragment.wednesday;
import app.androidhive.info.realm.R;
import app.androidhive.info.realm.adapters.BooksAdapter;
import app.androidhive.info.realm.adapters.RealmBooksAdapter;
import app.androidhive.info.realm.adapters.TimetableAdapter;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;


public class Main2Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Realm realm;
    private LayoutInflater inflater;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private String Label;
    TimetableAdapter adapter;
    List<String > students;
    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       adapter = new TimetableAdapter(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Add Class");
        TypefaceProvider.registerDefaultIconSets();

        FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_a);
actionB.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        inflater = Main2Activity.this.getLayoutInflater();
        View content = inflater.inflate(R.layout.time_add_extra, null);
        spinner = (MaterialSpinner) content.findViewById(R.id.spinner1);
        loadStudent_Simple();
        Label = students.get(0);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Label = item;
            }

        });
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setView(content)
                .setTitle("Add Subject")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Book book = new Book();
                        int i = viewPager.getCurrentItem();
                        book.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());
                        book.setTitle(Label);
                        book.setDay(Integer.toString(i));
                        book.setExtra(true);
                        book.setUpdate(false);
                        book.setCancled(false);
                        book.setIfbunked(false);
                        realm.beginTransaction();
                        realm.copyToRealm(book);
                        realm.commitTransaction();
                        adapter.notifyDataSetChanged();
                        recreate();

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
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.action_b);
        menuMultipleActions.addButton(actionC);

        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflater = Main2Activity.this.getLayoutInflater();
                View content = inflater.inflate(R.layout.time_add, null);
              spinner = (MaterialSpinner) content.findViewById(R.id.spinner1);
loadStudent_Simple();
                       Label = students.get(0);
                spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                    @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                               Label = item;
                    }

                });
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setView(content)
                        .setTitle("Add Subject")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Book book = new Book();
                                int i = viewPager.getCurrentItem();;
                                book.setId(RealmController.getInstance().getBooks().size() + System.currentTimeMillis());
                                book.setTitle(Label);
                                book.setDay(Integer.toString(i));
                                book.setExtra(false);
                                book.setUpdate(false);
                                book.setCancled(false);
                                book.setIfbunked(false);
                                    // Persist your data easily
                                    realm.beginTransaction();
                                    realm.copyToRealm(book);
                                    realm.commitTransaction();
                                    adapter.notifyDataSetChanged();
                                recreate();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    private void loadStudent_Simple(){
        students = new ArrayList<>();
        for(Book b: realm.where(Book.class).equalTo("day","7").findAll())
        {
           students.add(b.getTitle());

        }
        spinner.setItems(students);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new monday(), "Monday");
        adapter.addFrag(new Tuesday(), "Tuesday");
        adapter.addFrag(new wednesday(), "Wednesday");
        adapter.addFrag(new thursday(), "Thursday");
        adapter.addFrag(new friday(), "Friday");
        adapter.addFrag(new saturday(), "Saturday");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
       else if(item.getItemId() == R.id.action_settings)
        {
            Intent i = new Intent(Main2Activity.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}