package com.markjmind.uni;

import android.os.AsyncTask;

import com.markjmind.uni.viewer.UpdateEvent;

class InnerUniAsyncTask extends AsyncTask<Void, Object, Boolean> implements UpdateEvent{
    private boolean isCancel;


    public InnerUniAsyncTask(){
        this.isCancel = false;
    }


    @Override
    protected void onPreExecute() {

    }

    private Exception doInBackException=null;
    @Override
    protected Boolean doInBackground(Void... params) {
        try{

            return true;
        }catch(Exception e){
            doInBackException = e;
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        if(!isCancel){

        }else{
            cancel();
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(!isCancel){

        }else{
            cancel();
        }
    }

    public void cancel(){

    }

    public boolean isCancel(){
        return isCancel;
    }


    @Override
    public void update(Object value) {
        this.publishProgress(value);
     }

}