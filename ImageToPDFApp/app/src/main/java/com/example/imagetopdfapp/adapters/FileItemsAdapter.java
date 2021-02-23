package com.example.imagetopdfapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagetopdfapp.R;
import com.example.imagetopdfapp.models.FileData;

import java.util.ArrayList;
/*
Custom adapter to for displaying pdf file information
 */


public class FileItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<FileData> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView path;
        public TextView size;
        public TextView created;
        public View layout;

        public ViewHolder(View itemView){
            super(itemView);
            layout = itemView;
            path = (TextView) itemView.findViewById(R.id.pdfPath);
            size = (TextView) itemView.findViewById(R.id.fileSizeText);
            created = (TextView) itemView.findViewById(R.id.creationText);
        }
    }

    public void add(int position,FileData item) {
        values.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }


    public FileItemsAdapter(ArrayList<FileData> data){
        values = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.file_item_layout,parent,false);
        FileItemsAdapter.ViewHolder vh = new FileItemsAdapter.ViewHolder(v);
        return vh;
    }
    /*
    Bind the file path, file size in KB and file creation date-time to their respective text Views
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FileData data = values.get(position);
        FileItemsAdapter.ViewHolder fileHolder = (FileItemsAdapter.ViewHolder) holder;
        fileHolder.path.setText(data.getFilePath());
        fileHolder.size.setText(data.getSize() + " KB");
        fileHolder.created.setText(data.getCreationTime());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
