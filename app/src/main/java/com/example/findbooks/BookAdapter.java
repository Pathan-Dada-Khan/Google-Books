package com.example.findbooks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Activity content, ArrayList<Book> book){
        super(content , 0 , book);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.books,parent,false);
        }

        ImageView imageView = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);
        TextView date = view.findViewById(R.id.date);

        Book current = getItem(position);
        String image = current.getImage();
        if(!image.isEmpty() && image!=null){
            image=image.replace("http","https");
            Picasso.get().load(image).into(imageView);
        }
        title.setText(current.getTitle());
        author.setText(current.getAuthor());
        date.setText(current.getPublishedDate());

        return view;
    }
}
