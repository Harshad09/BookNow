package com.example.photoreceipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    EditText product;
    EditText price;
    EditText quantity;
    Button submit;


    LinearLayout layout_view;
    ImageView imgResultImage;
    Button btnConvertToImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        printLayout = findViewById(R.id.print);
//
        product = findViewById(R.id.product);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        submit = findViewById(R.id.submit);
//



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productName = product.getText().toString();
                int ProductPrice = Integer.parseInt(price.getText().toString());
                int ProductQuantity = Integer.parseInt(quantity.getText().toString());
                int TotalPrice = ProductPrice * ProductQuantity;

                if(product.equals("") || price.equals("") || quantity.equals("")) {
                    Toast.makeText(MainActivity.this, "Please Enter all Fields !", Toast.LENGTH_SHORT).show();
                }else {
                      Intent intent = new Intent(MainActivity.this,PrintReceipt.class);
                      intent.putExtra("ProductName",productName);
                      intent.putExtra("ProductPrice",ProductPrice);
                      intent.putExtra("ProductQuantity",ProductQuantity);
                      intent.putExtra("TotalPrice",TotalPrice);
                      startActivity(intent);
                }
            }
        });


    }
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}