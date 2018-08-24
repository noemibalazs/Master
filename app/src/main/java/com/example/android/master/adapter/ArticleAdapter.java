package com.example.android.master.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.example.android.master.data.Article;
import com.example.android.master.inter.MyInterface;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context mContext;
    private List<Article> mArticles;
    private String date;

    private MyInterface myInterface;

    public ArticleAdapter(Context context, MyInterface face){
        mContext = context;
        myInterface = face;
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ArticleViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, final int position) {

        Article article = mArticles.get(position);

        final String title = article.getArticleTitle();
        final String author = article.getAuthor();
        date = article.getPublishedDate();
        final String photo = article.getArticlePhoto();
        final String thumb = article.getArticleThumb();
        final String body = article.getBody();
        final int id = article.getArticleId();

        Date publishedDate = parsePublishedDate();

        if (!publishedDate.before(START_OF_EPOCH.getTime())) {

            holder.subTitle.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + "<br/>" + " by "
                            + author ));
        } else {
            holder.subTitle.setText(Html.fromHtml(
                    outputFormat.format(publishedDate)
                            + "<br/>" + " by "
                            + author ));
        }

        holder.title.setText(title);

        Picasso.get()
                .load(thumb)
                .error(R.drawable.ec)
                .placeholder(R.drawable.ec)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put(ArticleEntry.TITLE, title);
                values.put(ArticleEntry.AUTHOR, author);
                values.put(ArticleEntry.BODY, body);
                values.put(ArticleEntry.THUMB, thumb);
                values.put(ArticleEntry.PHOTO, photo);
                values.put(ArticleEntry.DATE, date);
                values.put(ArticleEntry.ID, id);

                Uri uri = mContext.getContentResolver().insert(ArticleEntry.CONTENT_URI, values);
                Log.v("TAG", "New uri was inserted " + uri);

                myInterface.onHandler(id);


            }
        });


    }

    @Override
    public int getItemCount() {
        if (mArticles == null){
            return 0;
        }

        return mArticles.size();
    }

    private Date parsePublishedDate() {
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e("TAG", ex.getMessage());
            Log.i("TAG", "passing today's date");
            return new Date();
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView subTitle;
        private ImageView image;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.article_title);
            subTitle = itemView.findViewById(R.id.article_subtitle);
            image = itemView.findViewById(R.id.thumbnail);
        }
    }

    public void bindData(List<Article> articles){
        mArticles = articles;
        notifyDataSetChanged();
    }
}
