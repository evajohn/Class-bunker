package app.androidhive.info.realm.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.adapters.BooksAdapter;
import app.androidhive.info.realm.adapters.RealmBooksAdapter;
import app.androidhive.info.realm.adapters.TimetableAdapter;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by siva on 17/6/16.
 */
public class friday extends Fragment {


    TimetableAdapter adapter;
    String day="Monday";
    private Realm realm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.content_main, container, false);

        adapter = new TimetableAdapter(getContext());

        this.realm = RealmController.with(this).getRealm();
adapter.set(4);
        setRealmAdapter(realm.where(Book.class).equalTo("day","4").findAll());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        registerForContextMenu(recyclerView);

        return  recyclerView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){

        RealmResults<Book> main= realm.where(Book.class).equalTo("day","7").findAll();
        RealmResults<Book> fragment = realm.where(Book.class).equalTo("day","4").findAll();
        if(item.getTitle()=="Remove"){
                realm.beginTransaction();
                fragment.remove((int) adapter.getItemId());
                realm.commitTransaction();

            adapter.notifyDataSetChanged();
        }else{
            return false;
        }
        return true;
    }
    public void setRealmAdapter(RealmResults<Book> books) {

        RealmResults<Book> temp;

        RealmBooksAdapter realmAdapter = new RealmBooksAdapter(this.getActivity().getApplicationContext(), books, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

}

