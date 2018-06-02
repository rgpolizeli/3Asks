package com.example.myapplication.persistence.entity;

import android.app.Application;
import android.os.AsyncTask;

import com.example.myapplication.FileHandle;

import java.util.List;

public class EpisodeRepository {
    private List<Episode> mEpisodes;
    private FileHandle mFileHandle;

    public EpisodeRepository(Application application) {
        //this.mFileHandle = new FileHandle(application);
        //this.mEpisodes = this.mFileHandle.getAllEpisodes();
    }

    public List<Episode> getAllEpisodes() {
        return this.mEpisodes;
    }

    private void loadEpisodes(){

    }

    public void createEpisode(){
        //Episode newEpisode = new Episode();
        //new createEpisodeAsyncTask(mFileHandle).execute(newEpisode);
    }

    private static class createEpisodeAsyncTask extends AsyncTask<Episode, Void, Void> {

        private FileHandle mAsyncFileHandle;

        createEpisodeAsyncTask(FileHandle fileHandle) {
            mAsyncFileHandle = fileHandle;
        }

        @Override
        protected Void doInBackground(final Episode... params) {
            //mAsyncFileHandle.createEpisodeJSONFile(params[0]);
            return null;
        }
    }


}
