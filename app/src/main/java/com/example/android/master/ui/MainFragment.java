package com.example.android.master.ui;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.master.R;
import com.example.android.master.adapter.ArticleAdapter;
import com.example.android.master.data.Article;
import com.example.android.master.database.ArticleContract.ArticleEntry;
import com.example.android.master.inter.MyInterface;
import com.example.android.master.loader.ArticleLoader;


import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>>, MyInterface{

    private Boolean isTablet ;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private CoordinatorLayout layout;

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int LOAD_ID = 9;

    public MainFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.main_fragment,container, false );

        layout =  root.findViewById(R.id.coordinator_layout);
        progressBar = root.findViewById(R.id.progress_bar);

        recyclerView = root.findViewById(R.id.recycler_view);
        adapter = new ArticleAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columns));

        isTablet = ((MainActivity)getActivity()).tabletMode();

        ConnectivityManager manager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info!=null && info.isConnected()){

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOAD_ID, null, this);

        }

        else {
            progressBar.setVisibility(View.GONE);
        }

        return root;
    }

    public boolean isChecked(){
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info.isConnected() && info!=null){
            return true;
        }

        return false;
    }

    public void snack(){
        Snackbar snackbar = Snackbar.make(layout, "I hope you will enjoy!", Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        String link = "https://go.udacity.com/xyz-reader-json";
        return new ArticleLoader(getContext(), link);

    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {

        progressBar.setVisibility(View.GONE);

        if (data!=null && !data.isEmpty()){
            adapter.bindData(data);
            snack();
        } else {
            Toast.makeText(getContext(), getString(R.string.sorry), Toast.LENGTH_SHORT).show();
        } if (!isChecked()){
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Network is available!");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

    }


    @Override
    public void onHandler(int articleId) {

        if (!isTablet){

            Intent actIntent = new Intent(getContext(), DetailActivity.class);
            Uri currentUri = ContentUris.withAppendedId(ArticleEntry.CONTENT_URI, articleId);
            actIntent.setData(currentUri);
            startActivity(actIntent);

        } else {


                Bundle bundle = new Bundle();
                Uri currentUri = ContentUris.withAppendedId(ArticleEntry.CONTENT_URI, articleId);
                bundle.putParcelable("uri", currentUri);

                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(bundle);
                FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.container, fragment).commit();

        }
    }
}
