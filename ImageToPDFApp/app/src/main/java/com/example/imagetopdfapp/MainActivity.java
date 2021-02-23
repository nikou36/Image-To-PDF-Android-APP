package com.example.imagetopdfapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.imagetopdfapp.adapters.FileItemsAdapter;
import com.example.imagetopdfapp.models.FileData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/*
Displays converted files in a recyclerview if there has been at least one file converted. Otherwise,
the recycler view is hidden and is replaced by a text view.

Button on the bottom moves user to conversion activity
 */

public class MainActivity extends AppCompatActivity {
    private TextView emptyMessage;
    private RecyclerView fileFeed;
    private RecyclerView.Adapter fileAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FileData> files;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fileFeed = (RecyclerView) findViewById(R.id.fileFeed);
        this.emptyMessage = (TextView) findViewById(R.id.emptyMessage);

        final Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ChoseImageActivity.class);
                startActivity(i);
            }
        });
    }
    /*
    Search in the app's target folder for converted pdf files
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<FileData> searchPdfInExternalStorage(File folder) {
        ArrayList<FileData> MyFiles = new ArrayList<FileData>();
        if (folder != null) {
            if (folder.listFiles() != null) {
                for (File file : folder.listFiles()) {
                    if (file.isFile()) {
                        //.pdf files
                        if (file.getName().contains(".pdf")) {
                            long fileSizeInBytes = file.length();
                            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                            long fileSizeInKB = fileSizeInBytes / 1024;
                            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                            long fileSizeInMB = fileSizeInKB / 1024;
                            String creation = "Unknown";
                            try {
                                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                                creation = attr.creationTime().toString();
                                System.out.println(attr.creationTime().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(fileSizeInKB +"");
                            String path = file.getPath();
                            path = path.replace("/storage/emulated/0/","");
                            MyFiles.add(new FileData(path,fileSizeInKB,creation));
                            Log.d("filePath-------", "" + file.getPath());
                        }
                    } else {
                        searchPdfInExternalStorage(file);
                    }
                }
            }
        }
        return MyFiles;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume(){
        super.onResume();
        File dir = new File(Environment.getExternalStorageDirectory(),"IMAGE2PDF");
        this.files = searchPdfInExternalStorage(dir);
        //If no converted files exist, don't initialize recyclerview and display hidden text view
        if(files.size() == 0){
            this.fileFeed.setVisibility(View.INVISIBLE);
            this.emptyMessage.setVisibility(View.VISIBLE);
        }else{
            //If files exist init the recycler view
            this.fileAdapter = new FileItemsAdapter(files);
            this.layoutManager = new LinearLayoutManager(this);
            this.fileFeed.setAdapter(fileAdapter);
            this.fileFeed.setLayoutManager(layoutManager);
        }

    }


}