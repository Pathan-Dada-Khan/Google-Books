package com.example.findbooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class BooksView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.green));
        }


        ImageView imageView = (ImageView)findViewById(R.id.book_image);
        TextView title = (TextView)findViewById(R.id.title);
        TextView author = (TextView)findViewById(R.id.author_json);
        TextView publisher = (TextView)findViewById(R.id.publisher_json);
        TextView date = (TextView)findViewById(R.id.date_json);
        TextView description = (TextView)findViewById(R.id.description_json);
        TextView language = (TextView)findViewById(R.id.language_json);
        Button sorry = (Button)findViewById(R.id.sorry);

        Button read = (Button)findViewById(R.id.read);
        Button buy = (Button)findViewById(R.id.buy);

        Intent intent = getIntent();

        String image = intent.getStringExtra("image");
        if(!image.isEmpty() && image!=null) {
            image = image.replace("http", "https");

            Glide.with(this).load(image).into(imageView);
        }
        title.setText(intent.getStringExtra("title"));
        author.setText(intent.getStringExtra("author"));
        publisher.setText(intent.getStringExtra("publisher"));
        date.setText(intent.getStringExtra("date"));
        description.setText(intent.getStringExtra("des"));
        language.setText(intent.getStringExtra("lan"));
        double price = intent.getDoubleExtra("price",0.0);
        if(price==0.0){
            read.setVisibility(View.GONE);
            buy.setVisibility(View.GONE);
        }
        else{
            sorry.setVisibility(View.GONE);
        }
        char rupee = 8377;
        buy.setText("Buy "+rupee+price);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent readIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(intent.getStringExtra("read")));
                startActivity(readIntent);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent buyIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(intent.getStringExtra("buy")));
                startActivity(buyIntent);
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
}