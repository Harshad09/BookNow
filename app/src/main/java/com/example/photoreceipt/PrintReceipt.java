package com.example.photoreceipt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintReceipt extends AppCompatActivity {


    TextView row_product,row_quantity,row_price,row_total,final_row_product,final_row_total;

    Button btnConvertToImage,btnConvertToPdf;
    LinearLayout layout_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_receipt);

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        Intent intent = getIntent();
        String productName = intent.getStringExtra("ProductName");
        int ProductPrice = intent.getIntExtra("ProductPrice",200);
        int ProductQuantity = intent.getIntExtra("ProductQuantity",2);
        int TotalPrice = intent.getIntExtra("TotalPrice",400);

        row_product = findViewById(R.id.row_product);
        row_price = findViewById(R.id.row_price);
        row_quantity = findViewById(R.id.row_quantity);
        row_total = findViewById(R.id.row_total);
        final_row_product = findViewById(R.id.final_row_product);
        final_row_total = findViewById(R.id.final_row_total);
//        btnConvertToImage = findViewById(R.id.convert);
        btnConvertToPdf = findViewById(R.id.convertToPdf);
        layout_view = findViewById(R.id.print);

        row_product.setText(productName);
        final_row_product.setText(productName);
        row_price.setText(String.valueOf(ProductPrice));
        row_quantity.setText(String.valueOf(ProductQuantity));
        row_total.setText(String.valueOf(TotalPrice));
        final_row_total.setText(String.valueOf(TotalPrice));

//        ******************************************************************* //

        btnConvertToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PdfGenerator.getBuilder()
                        .setContext(PrintReceipt.this)
                        .fromViewSource()
                        .fromView(layout_view)
                        .setPageSize(PdfGenerator.PageSize.A4)
                        .setFileName("BookNow_Pdf")
                        .setFolderName(productName)
                        .openPDFafterGeneration(true)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                                Toast.makeText(PrintReceipt.this, "Operation Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onStartPDFGeneration() {
                                /*When PDF generation begins to start*/
//                        Toast.makeText(PrintReceipt.this, "Generating Pdf", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFinishPDFGeneration() {
                                /*When PDF generation is finished*/
                        Toast.makeText(PrintReceipt.this, "Pdf Generated", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
//                                Toast.makeText(PrintReceipt.this, response.getPath(), Toast.LENGTH_SHORT).show();
                                Log.d("path",response.getPath());
                            }
                        });

            }
        });



//        ******************************************************************* //

//        btnConvertToImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bitmap bitmap = getBitmapFromView(layout_view);
//
//                storeImage(bitmap);
//
//                PackageManager pm = PrintReceipt.this.getPackageManager();
//                String pack = "Receipt";
//                try {
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                    String path = MediaStore.Images.Media.insertImage(PrintReceipt.this.getContentResolver(), bitmap, "Title", null);
//                    Uri imageUri = Uri.parse(path);
//
//                    @SuppressWarnings("unused")
//                    PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);
//
//                    Intent waIntent = new Intent(Intent.ACTION_SEND);
//                    waIntent.setType("image/*");
//                    waIntent.setPackage(pack);
//                    waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
//                    waIntent.putExtra(Intent.EXTRA_TEXT, pack);
//                    PrintReceipt.this.startActivity(Intent.createChooser(waIntent, "Share with"));
//                } catch (Exception e) {
//                    Log.e("Error on sharing", e + " ");
//                    Toast.makeText(PrintReceipt.this, "Image Stored at Internal storage pictures", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        btnConvertToPdf.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//
//                Bitmap bitmapForPdf = getBitmapFromView(layout_view);
//                PdfDocument pdfDocument = new PdfDocument();
//                PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmapForPdf.getWidth(),bitmapForPdf.getHeight(),1).create();
//
//                PdfDocument.Page page = pdfDocument.startPage(pi);
//                Canvas canvas = page.getCanvas();
//                Paint paint = new Paint();
//                paint.setColor(Color.parseColor("#FFFFFF"));
//                canvas.drawPaint(paint);
//
//                bitmapForPdf = Bitmap.createScaledBitmap(bitmapForPdf,bitmapForPdf.getWidth(),bitmapForPdf.getHeight(),true);
//                paint.setColor(Color.BLUE);
//                canvas.drawBitmap(bitmapForPdf,0,0,null);
//                pdfDocument.finishPage(page);
//
//                //save pdf
//
//                File root = new File(Environment.getExternalStorageDirectory(),"BookNow");
//                if(!root.exists()) {
//                    root.mkdir();
//                }
//                String name = productName+".pdf";
//                File file = new File(root,name);
//                try {
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    pdfDocument.writeTo(fileOutputStream);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PrintReceipt.this, e.toString(), Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PrintReceipt.this, e.toString(), Toast.LENGTH_SHORT).show();
//                }
//                pdfDocument.close();
//
//                Toast.makeText(PrintReceipt.this, "Pdf saved in InternalStorage/BookNow", Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            bgDrawable.draw(canvas);
        }   else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted1");
                return true;
            } else {

                Log.v("TAG","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted2");
                return true;
            } else {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:

                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission

                }
                break;

            case 3:
                Log.d("TAG", "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission

                }
                break;
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }



}