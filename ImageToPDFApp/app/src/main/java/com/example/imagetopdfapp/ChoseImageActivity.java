package com.example.imagetopdfapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import android.graphics.pdf.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/*
Allow user to chose an image via the gallery or by taking one with a camera.
The image is then converted to pdf.
 */


public class ChoseImageActivity extends AppCompatActivity {
    private ImageButton galleryButton;
    private ImageButton cameraButton;
    private ImageView mImageView;
    private  Button submitButton;
    //True if an image is chosen
    private boolean chosenPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_image);
        this.chosenPhoto = false;
        this.galleryButton = findViewById(R.id.imagePickerButton);
        this.cameraButton = findViewById(R.id.cameraButton);
        this.mImageView = findViewById(R.id.selectedImageView);
        this.submitButton = findViewById(R.id.submissionButton);
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenPhoto){
                    //If an image is chosen/taken then proceed to convert it
                    Toast pickImgToast = new Toast(v.getContext());
                    pickImgToast.makeText(v.getContext(), "Image Converted", Toast.LENGTH_SHORT).show();
                    try {
                        convertImageToPDF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    //If an image is not chosen, make a toast to notify the user.
                    Toast pickImgToast = new Toast(v.getContext());
                    pickImgToast.makeText(v.getContext(), "Please pick an image before submitting", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);

            }
        });

        this.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any ac
            }
        });
    }

    /*
    Converts the bitmap of the chosen image into a pdf file. Saves in the folder IMAGE2PDF
     */

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void convertImageToPDF() throws IOException {
        BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        PdfDocument pdf = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(),1).create();

        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap,0,0,null);
        pdf.finishPage(page);



        File root = new File(Environment.getExternalStorageDirectory(), "IMAGE2PDF");
        if(!root.exists()){
            root.mkdir();
            Log.i("Directiory does not exist","True");
        }
        //Randomly generate file name
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".pdf";

        File file = new File(root,fname);
        if(file.exists()){
            file.delete();
        }
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            pdf.writeTo(fileOutputStream);
            pdf.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        pdf.close();
    }

    /*
    Start either the gallery picker or camera depending on user choice
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    this.chosenPhoto = true;
                    this.mImageView.setImageBitmap(imageBitmap);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    this.chosenPhoto = true;
                    this.mImageView.setImageURI(selectedImage);
                }
                break;
        }
    }
}