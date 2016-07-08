package app.androidhive.info.realm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class Main3Activity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    Realm realm;
    LayoutInflater inflater;
Toolbar toolbar;
    int k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView8);
        Bundle extras = getIntent().getExtras();
         k =extras.getInt("position");
        String i = Integer.toString(k);
        realm = RealmController.getInstance().getRealm();

        RealmResults<Book> results = realm.where(Book.class).equalTo("day", "7").findAll();
        textView.setText(results.get(k).getTitle());
        textView2.setText(Integer.toString(results.get(k).getBunked()));
        textView3.setText(Integer.toString(results.get(k).getTotal_classes()));
        int lo= results.get(k).getTotal_classes();
        final int po=results.get(k).getBunked();
        StringBuilder s1 = new StringBuilder();
        s1.append("You Have Bunked ");
        if(lo!=0) {
            double ans = 100-((lo - po) * 100 / lo);
            s1.append(Double.toString(ans));
            s1.append("%");
           textView4.setText(s1);
        }
        else {
            s1.append(Integer.toString(results.get(k).getBunked()));
            s1.append("%");
            textView4.setText(s1);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        else if(item.getItemId() == R.id.delete)
        {

            inflater = Main3Activity.this.getLayoutInflater();
            View content = inflater.inflate(R.layout.delete, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
            builder.setView(content)
                    .setTitle("Remove Classes")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RealmResults<Book> results = realm.where(Book.class).equalTo("day", "7").findAll();
                            Book b = results.get(k);
                            String title = b.getTitle();

                            // All changes to data must happen in a transaction
                            realm.beginTransaction();

                            // remove single match
                            results.remove(k);
                            realm.commitTransaction();
                            realm.notifyAll();
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
        else if(item.getItemId() == R.id.edit)
        {
            inflater = Main3Activity.this.getLayoutInflater();
            View content = inflater.inflate(R.layout.edit_item, null);
            final EditText editTitle = (EditText) content.findViewById(R.id.title);
            final EditText bunked = (EditText) content.findViewById(R.id.bunked);
            final EditText total = (EditText) content.findViewById(R.id.no);
            editTitle.setText(textView.getText());

            AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);
            builder.setView(content)
                    .setTitle("Edit Book")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RealmResults<Book> results = realm.where(Book.class).equalTo("day","7").findAll();
                            realm.beginTransaction();

                            if (bunked.getText() == null || bunked.getText().toString().equals("") || bunked.getText().toString().equals(" ")) {
                            } else {
                                results.get(k).setBunked(Integer.parseInt(bunked.getText().toString()));
                            }

                            if (total.getText() == null || total.getText().toString().equals("") || total.getText().toString().equals(" ")) {
                            } else {
                                results.get(k).setTotal_classes(Integer.parseInt(total.getText().toString()));
                            }
                            results.get(k).setTitle(editTitle.getText().toString());

                            realm.commitTransaction();

                            realm.notifyAll();
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



        return super.onOptionsItemSelected(item);
    }
}
