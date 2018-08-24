package com.example.android.master.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.master.R;
import com.example.android.master.database.ArticleContract.ArticleEntry;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


public class DetailActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView titleDetail;
    private TextView bylineDetail;
    private TextView bodyDetail;
    private ImageView photoDetail;
    private FloatingActionButton fButton;
    private String dateString;
    private Uri mCurrentUri;

    private static final int LOAD_ID = 3;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar =  findViewById(R.id.flexible_example_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        titleDetail = findViewById(R.id.detail_title);
        bylineDetail = findViewById(R.id.detail_byline);
        bodyDetail = findViewById(R.id.detail_body);

        photoDetail = findViewById(R.id.image_cover);

        LoaderManager manager = getLoaderManager();
        manager.initLoader(LOAD_ID, null, this);

        fButton = findViewById(R.id.fab_button);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(DetailActivity.this)
                        .setType("text/plain")
                        .setText("Some text")
                        .getIntent(), getString(R.string.action_share))
                );
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = new String[]{
                ArticleEntry.ID,
                ArticleEntry.TITLE,
                ArticleEntry.AUTHOR,
                ArticleEntry.BODY,
                ArticleEntry.THUMB,
                ArticleEntry.PHOTO,
                ArticleEntry.DATE
        };
        return new CursorLoader(DetailActivity.this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data==null || data.getCount() < 1){
            return;
        }

        if (data.moveToFirst()){

            int titleIndex = data.getColumnIndex(ArticleEntry.TITLE);
            int authorIndex = data.getColumnIndex(ArticleEntry.AUTHOR);
            int bodyIndex = data.getColumnIndex(ArticleEntry.BODY);
            int photoIndex = data.getColumnIndex(ArticleEntry.PHOTO);
            int dateIndex = data.getColumnIndex(ArticleEntry.DATE);

            String titleString = data.getString(titleIndex);
            String authorString = data.getString(authorIndex);
            String bodyString = data.getString(bodyIndex);
            String photoString = data.getString(photoIndex);
            dateString = data.getString(dateIndex);

            Picasso.get()
                    .load(photoString)
                    .placeholder(R.drawable.ec)
                    .error(R.drawable.ec)
                    .into(photoDetail);

            Typeface typeface = Typeface.createFromAsset(getBaseContext().getAssets(), "Rosario-Regular.ttf");

            titleDetail.setText(titleString);
            bodyDetail.setText(bodyString);
            bodyDetail.setTypeface(typeface);


            Date publishedDate = dateFormat();

            if (!publishedDate.before(START_OF_EPOCH.getTime())) {

                bylineDetail.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + "<br/>" + " by "
                                + authorString ));
            } else {
                bylineDetail.setText(Html.fromHtml(
                        outputFormat.format(publishedDate)
                                + "<br/>" + " by "
                                + authorString ));
            }


        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private Date dateFormat(){
        try{
            return dateFormat.parse(dateString);
        } catch (ParseException ex) {
            Log.e("TAG", ex.getMessage());
            Log.i("TAG", "passing today's date");
            return new Date();
        }
    }
}
