package com.example.myapplication;


import android.content.Context;

import com.example.myapplication.persistence.entity.Episode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileHandle {
/*
    Context mContext;

    public FileHandle(Context context){
        mContext = context;
    }

    public List<Episode> getAllEpisodes(){
        List<Episode> episodes = new ArrayList<>();
        File[] files = this.mContext.getFilesDir().listFiles();



        return episodes;
    }

    public void createEpisodeJSONFile(Episode episode){
        Random rdm = new Random();
        String FILENAME = String.valueOf(rdm.nextInt());
        String string = episode.parseEpisodeToJSON().toString();

        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(FILENAME, mContext.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */
}
