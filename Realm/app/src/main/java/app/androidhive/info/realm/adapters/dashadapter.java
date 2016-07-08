package app.androidhive.info.realm.adapters;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.activity.Main3Activity;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by siva guru on 05-07-2016.
 */

public class dashadapter extends RealmRecyclerViewAdapter<Book>  implements View.OnCreateContextMenuListener {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;
    int i=7;
    int temp;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select The Action");

    }
    public long getItemId() {
        return temp;
    }

    public dashadapter(Context context) {

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

if(!book.isExtra()) {
    RealmResults<Book> main = realm.where(Book.class).equalTo("day", "7").findAll();
    Book q = book;

    for (Book t : main) {
        if (t.getTitle().equals(book.getTitle())) {
            q = t;
        }
    }
    if (book.isCancled()) {
        holder.textTitle.setText(q.getTitle() + "(Cancled)");
    } else if (book.isIfbunked())
    {
        holder.textTitle.setText(q.getTitle() + "(Bunked)");
    }

    else {
        holder.textTitle.setText(q.getTitle());
    }
    holder.bunked.setText("You have Bunked a total of  " + Integer.toString(q.getBunked()) + " Out of " + Integer.toString(q.getTotal_classes()));
    int lo = q.getTotal_classes();
    final int po = q.getBunked();
    double ans=0;
    if (lo != 0) {
         ans = 100 - ((lo - po) * 100 / lo);

        holder.percent.setText("Your Bunk Percent is : " + Double.toString(ans) + "%");
    } else {
        holder.percent.setText("Your Bunk Percent is :0");
    }
    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
    boolean bAppUpdates = SP.getBoolean("applicationUpdates",false);
    if(bAppUpdates)
    {
        if(ans>75)
        {
            Notify();
        }
    }
    holder.card.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            temp = position;
            return false;
        }
    });
         }
        else
     {
         RealmResults<Book> main = realm.where(Book.class).equalTo("day", "7").findAll();
         Book q = book;

         for (Book t : main) {
             if (t.getTitle().equals(book.getTitle())) {
                 q = t;
             }
         }
         if (book.isIfbunked())
         {
             holder.textTitle.setText(q.getTitle() + "(Bunked)");
         }

         else {
             holder.textTitle.setText(q.getTitle() +"(Extra)");
         }
         holder.bunked.setText("You have Bunked a total of  " + Integer.toString(q.getBunked()) + " Out of " + Integer.toString(q.getTotal_classes()));
         int lo = q.getTotal_classes();
         final int po = q.getBunked();
         if (lo != 0) {
             double ans = 100 - ((lo - po) * 100 / lo);

             holder.percent.setText("Your Bunk Percent is : " + Double.toString(ans) + "%");
         } else {
             holder.percent.setText("Your Bunk Percent is :0");
         }

         holder.card.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 temp = position;
                 return false;
             }
         });
     }
    }
    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }
    private void Notify(){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.photo)
                        .setContentTitle("Class Bunker")
                        .setContentText("You Have Bunked more than 75% in a Subject");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, Main3Activity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Main3Activity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }



    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public CardView card;
        public TextView textTitle;
        public TextView bunked;
        public TextView percent;
        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);

            card = (CardView) itemView.findViewById(R.id.card_books);
            textTitle = (TextView) itemView.findViewById(R.id.text_books_title);
            bunked = (TextView) itemView.findViewById(R.id.bunked1);
            percent = (TextView) itemView.findViewById(R.id.percent);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(1, v.getId(), 0, "Cancled");//groupId, itemId, order, title
            menu.add(2, v.getId(), 0, "Bunked");

        }

    }
}
