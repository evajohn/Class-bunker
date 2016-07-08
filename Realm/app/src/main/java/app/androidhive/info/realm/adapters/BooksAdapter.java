package app.androidhive.info.realm.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import app.androidhive.info.realm.R;
import app.androidhive.info.realm.activity.Main3Activity;
import app.androidhive.info.realm.app.Prefs;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class BooksAdapter extends RealmRecyclerViewAdapter<Book>  {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
    int i=7;
    public BooksAdapter(Context context) {

        this.context = context;
    }// create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books, parent, false);

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

        holder.textTitle.setText(book.getTitle());
        holder.bunked.setText(Integer.toString(book.getBunked()));
        int lo= book.getTotal_classes();
        final int po=book.getBunked();
        StringBuilder s1 = new StringBuilder();
        s1.append("You Have Bunked ");
        if(lo!=0) {
            double ans = 100-((lo - po) * 100 / lo);
            s1.append(Double.toString(ans));
            s1.append("%");
            holder.percentage.setText(s1);
        }
        else {
            s1.append(Integer.toString(book.getBunked()));
            s1.append("%");
            holder.percentage.setText(s1);
        }
            holder.subract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Book> results = realm.where(Book.class).equalTo("day", Integer.toString(i)).findAll();
                int i = 1, k;
                String s = (holder.bunked.getText()).toString();
                k = Integer.parseInt(s);
                k = k - i;
                realm.beginTransaction();
                if(results.get(position).getBunked()!=0) {
                    results.get(position).setBunked(k);
                }
                if(book.getTotal_classes()<book.getBunked())
                {
                    book.setTotal_classes(book.getBunked());
                }
                realm.commitTransaction();
                notifyDataSetChanged();
            }
        });
        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmResults<Book> results = realm.where(Book.class).equalTo("day", Integer.toString(i)).findAll();
                notifyDataSetChanged();
                int i = 1, k;
                String s = (holder.bunked.getText()).toString();
                k = Integer.parseInt(s);
                k = k + i;
                realm.beginTransaction();
                results.get(position).setBunked(k);
                if(results.get(position).getTotal_classes()<results.get(position).getBunked())
                {
                    results.get(position).setTotal_classes(book.getBunked());
                }
                realm.commitTransaction();
            }
        });
        //remove single match from realm
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editTitle = (EditText) content.findViewById(R.id.title);
                final EditText bunked = (EditText) content.findViewById(R.id.bunked);

                editTitle.setText(book.getTitle());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Book")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RealmResults<Book> results = realm.where(Book.class).equalTo("day", Integer.toString(i)).findAll();
                                realm.beginTransaction();

                                if (bunked.getText() == null || bunked.getText().toString().equals("") || bunked.getText().toString().equals(" ")) {
                                } else {
                                    results.get(position).setBunked(Integer.parseInt(bunked.getText().toString()));
                                }
                                results.get(position).setTitle(editTitle.getText().toString());

                                realm.commitTransaction();

                                notifyDataSetChanged();
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
           return false;
            }
        });

            //update single match from realm
            holder.card.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,Main3Activity.class);

                    i.putExtra("position",position);
                   context.startActivity(i);
                    }
            });


    }
    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder  {

        public CardView card;
        public TextView textTitle;
        public ImageButton increment;
        public TextView bunked;
        public ImageButton subract;
        public TextView percentage;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_books);
            textTitle = (TextView) itemView.findViewById(R.id.text_books_title);
            increment=(ImageButton) itemView.findViewById(R.id.button);
            bunked=(TextView) itemView.findViewById(R.id.bunked);
            subract=(ImageButton) itemView.findViewById(R.id.button1);
            percentage =(TextView) itemView.findViewById(R.id.no_bunked);
        }
    }
}
