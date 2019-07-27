package com.example.windows10.latihan9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class SinglePhoto extends AppCompatActivity {
    ImageAdapter myImageAdapter;
    ImageView iv;
    ArrayList<String> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_photo);
        setTitle("Detail Image");

        String imgPath = this.getIntent().getExtras().getString("path");
        iv = (ImageView) findViewById(R.id.singleimage);
        BitmapFactory.Options myBitmap = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, myBitmap);
        iv.setImageBitmap(bitmap);
    }
}
