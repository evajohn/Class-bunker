package app.androidhive.info.realm.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import android.view.ContextMenu.ContextMenuInfo;

import java.util.Calendar;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by siva on 17/6/16.
 */
public class TimetableAdapter extends RealmRecyclerViewAdapter<Book> implements
        View.OnCreateContextMenuListener {

    final Context context;

    private Realm realm;
    int i=7;
    int temp=0;


    public TimetableAdapter(Context context) {

        this.context = context;

    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new CardViewHolder(view);
    }
    public void set(int k)
    {
        i=k;

    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            realm = RealmController.getInstance().getRealm();

            // get the article
            final Book book = getItem(position);
            // cast the generic view holder to our specific one
            final CardViewHolder holder = (CardViewHolder) viewHolder;
            // set the title and the snippet

        if(!book.isExtra()) {
            holder.textTitle.setText(book.getTitle());
            holder.card.setOnLongClickListener(new View.OnLongClickListener() {
                                                   @Override
                                                   public boolean onLongClick(View v) {

                                                       temp=position;
                                                       return false;
                                                   }
                                               }
            );

        }

        else
        {
            holder.textTitle.setText(book.getTitle() + " (ExtraClass)");

            holder.card.setOnLongClickListener(new View.OnLongClickListener() {
                                                   @Override
                                                   public boolean onLongClick(View v) {

                                                       temp=position;
                                                       return false;
                                                   }
                                               }
            );
        }

        }

    public long getItemId() {
        return temp;
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select The Action");

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public CardView card;
        public TextView textTitle;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

                itemView.setOnCreateContextMenuListener(this);

            card = (CardView) itemView.findViewById(R.id.card_books);
            textTitle = (TextView) itemView.findViewById(R.id.text_books_title);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(3, v.getId(), 0, "Remove");

        }



    }
}

