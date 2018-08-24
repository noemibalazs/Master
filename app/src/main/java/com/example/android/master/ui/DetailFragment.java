package com.example.android.master.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.master.database.ArticleContract.ArticleEntry;

import com.example.android.master.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private TextView titleDetail;
    private TextView bylineDetail;
    private TextView bodyDetail;
    private ImageView photoDetail;
    private FloatingActionButton fButton;
    private String dateString;
    private Uri mCurrentUri;

    private static final int LOAD_ID = 12;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail_fragment, container, false);

        titleDetail = root.findViewById(R.id.detail_title);
        bylineDetail = root.findViewById(R.id.detail_byline);
        bodyDetail = root.findViewById(R.id.detail_body);

        photoDetail = root.findViewById(R.id.image_cover);

        Bundle bundle = getArguments();

        if (bundle!=null){

            mCurrentUri = bundle.getParcelable("uri");
        }


        LoaderManager manager = getLoaderManager();
        manager.initLoader(LOAD_ID, null, this);

        fButton = root.findViewById(R.id.fab_button);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from((Activity) getContext())
                        .setType("text/plain")
                        .setText("Some text")
                        .getIntent(), getString(R.string.action_share))
                );
            }
        });


        return root;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String [] projection = new String[]{
                ArticleEntry.ID,
                ArticleEntry.TITLE,
                ArticleEntry.AUTHOR,
                ArticleEntry.BODY,
                ArticleEntry.THUMB,
                ArticleEntry.PHOTO,
                ArticleEntry.DATE
        };
        return new CursorLoader(getContext(), mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

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

            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Rosario-Regular.ttf");

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
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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
