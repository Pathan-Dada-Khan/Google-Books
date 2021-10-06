package com.example.findbooks;

import android.app.Dialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    BookAdapter adapter;
    NetworkInfo networkInfo;
    Dialog dialog;
    TextView info;
    ShimmerFrameLayout shimmerFrameLayout;

    private static final int GOOGLE_BOOKS_ID=1;
    private String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    String query;
    View noResult;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.green));
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        gridView = (GridView)findViewById(R.id.list);

        noResult = (View) findViewById(R.id.noResult);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();
        info = (TextView)findViewById(R.id.info);
        info.setVisibility(View.GONE);
        noResult.setVisibility(View.GONE);

        Intent intent =getIntent();
        query = intent.getStringExtra("key");
        query=query.replace(" ","+");
        searchFor();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListView.this,BooksView.class);
                Book current = adapter.getItem(position);

                intent.putExtra("image",current.getImage());
                intent.putExtra("title",current.getTitle());
                intent.putExtra("author",current.getAuthor());
                intent.putExtra("publisher",current.getPublisher());
                intent.putExtra("date",current.getPublishedDate());
                intent.putExtra("des",current.getDescription());
                intent.putExtra("lan",current.getLanguage());
                intent.putExtra("read",current.getRead());
                double price = current.getPrice();
                intent.putExtra("price",price);
                intent.putExtra("buy",current.getBuy());

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchFor(){
        connection();
        adapter = new BookAdapter(this,new ArrayList<Book>());
        adapter.clear();
        gridView.setAdapter(adapter);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this,GOOGLE_BOOKS_URL+query+"&maxResults=40");
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> data) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.hideShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        adapter.clear();
        if(data!=null && !data.isEmpty()){
            adapter.addAll(data);
        }
        else{
            noResult.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader) {
        adapter.clear();
    }

    public void connection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo =connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(GOOGLE_BOOKS_ID,null,this);
        }
        else{
            shimmerFrameLayout.setVisibility(View.GONE);
            noInternet();
        }
    }

    private void noInternet(){
        dialog = new Dialog(ListView.this);
        dialog.setContentView(R.layout.no_internet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.no_internet));
        }
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        Button refresh = dialog.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

    }


}