package com.example.imagetopdfapp.models;

public class FileData {
    private String filePath;
    private long size;
    private String creationTime;

    public FileData(String filePath, long size, String creationTime) {
        this.filePath = filePath;
        this.size = size;
        this.creationTime = creationTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
