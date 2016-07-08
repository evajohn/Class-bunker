package app.androidhive.info.realm.adapters;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import app.androidhive.info.realm.R;
import app.androidhive.info.realm.activity.Main3Activity;
import app.androidhive.info.realm.model.Book;
import app.androidhive.info.realm.realm.RealmController;
import atownsend.swipeopenhelper.BaseSwipeOpenViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by siva on 27/6/16.
 */
public class TestAdapter extends RealmRecyclerViewAdapter<Book>  {

    private final Context context;
    private final boolean horizontal;
    private Realm realm;
    int i=7;
    private LayoutInflater inflater;
    private final ButtonCallbacks callbacks;
    private List<String> items = new ArrayList<>();

   public interface ButtonCallbacks {
        void removePosition(int position);
        void editPosition(int position);
    }
    public void set(int k)
    {
        i=k;
    }

    public TestAdapter(Context context, boolean horizontal, ButtonCallbacks callbacks) {
        this.context = context;
        this.horizontal = horizontal;
        this.callbacks = callbacks;
        TypefaceProvider.registerDefaultIconSets();

    }


    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.test, parent, false), callbacks);

    }


    @Override public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {


        realm = RealmController.getInstance().getRealm();

        // get the article
        final Book book = getItem(position);
        // cast the generic view holder to our specific one
        final TestViewHolder holder = (TestViewHolder) viewholder;
        // set the title and the snippet
        String s = book.getTitle();
        StringBuilder  qw = new StringBuilder(s);
        if(s.length()<12)
        {
            int we=s.length();
            while(we<12)
            {
                qw.append("  ");
                we++;
            }
        }
        holder.textView.setText(qw);
        holder.bunked.setText(Integer.toString(book.getBunked()));
        int lo= book.getTotal_classes();
        final int po=book.getBunked();
        StringBuilder s1 = new StringBuilder();
        double ans=0;
        s1.append("You Have Bunked ");
        if(lo!=0) {
            ans = 100-((lo - po) * 100 / lo);
            s1.append(Double.toString(ans));
            s1.append("%");
            holder.percentage.setText(s1);
        }
        else {
            s1.append(Integer.toString(book.getBunked()));
            s1.append("%");
            holder.percentage.setText(s1);
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

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Main3Activity.class);

                i.putExtra("position",position);
                context.startActivity(i);
            }
        });

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
                results.get(position).setCancled(true);
                results.get(position).setBunked(k);
                if(results.get(position).getTotal_classes()<results.get(position).getBunked())
                {
                    results.get(position).setTotal_classes(book.getBunked());
                }
                realm.commitTransaction();
            }
        });

    }

    @Override public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;    }

    public void removePosition(int adapterPosition) {
        items.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
    public void editPosition(final int position1)
    {
        final Book book = getItem(position1);

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
                            results.get(position1).setBunked(Integer.parseInt(bunked.getText().toString()));
                        }
                        results.get(position1).setTitle(editTitle.getText().toString());

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
    }

    static class TestViewHolder extends BaseSwipeOpenViewHolder {

        public LinearLayout contentView;
        public TextView textView;
        public TextView deleteButton;
        public TextView editButton;
        public BootstrapButton increment;
        public TextView bunked;
        public TextView percentage;
        public BootstrapButton subract;

        public TestViewHolder(final View view, final ButtonCallbacks callbacks) {
            super(view);

            contentView = (LinearLayout) view.findViewById(R.id.content_view);
            textView = (TextView) view.findViewById(R.id.display_text);
            percentage = (TextView) view.findViewById(R.id.percentage);
            deleteButton = (TextView) view.findViewById(R.id.delete_button);
            editButton = (TextView) view.findViewById(R.id.edit_button);
            bunked=(TextView) itemView.findViewById(R.id.no_bunked);
            subract=(BootstrapButton) itemView.findViewById(R.id.button1);
            increment =(BootstrapButton) itemView.findViewById(R.id.button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    callbacks.removePosition(getAdapterPosition());
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    callbacks.editPosition(getAdapterPosition());
                }
            });
        }

        @NonNull @Override public View getSwipeView() {
            return contentView;
        }

        @Override public float getEndHiddenViewSize() {
            return editButton.getMeasuredWidth();
        }

        @Override public float getStartHiddenViewSize() {
            return deleteButton.getMeasuredWidth();
        }

        @Override public void notifyStartOpen() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
        }

        @Override public void notifyEndOpen() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.blue));
        }
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



    static class HorizontalTestViewHolder extends BaseSwipeOpenViewHolder {
        public LinearLayout contentView;
        public TextView textView;
        public TextView deleteButton;
        public TextView editButton;


        public HorizontalTestViewHolder(final View view, final ButtonCallbacks callbacks) {
            super(view);
            contentView = (LinearLayout) view.findViewById(R.id.content_view);
            textView = (TextView) view.findViewById(R.id.display_text);
            deleteButton = (TextView) view.findViewById(R.id.delete_button);
            editButton = (TextView) view.findViewById(R.id.edit_button);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    callbacks.removePosition(getAdapterPosition());
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    callbacks.editPosition(getAdapterPosition());
                }
            });
        }

        @NonNull @Override public View getSwipeView() {
            return contentView;
        }

        @Override public float getEndHiddenViewSize() {
            return editButton.getMeasuredHeight();
        }

        @Override public float getStartHiddenViewSize() {
            return deleteButton.getMeasuredHeight();
        }
    }
}