package com.markjmind.uni;

import android.os.AsyncTask;
import android.util.Log;

class InnerAsyncTask extends AsyncTask<Void, Object, Boolean> implements UpdateEvent{
    private String taskKey;
    private boolean isStop;
    private Viewer jv;
    private UpdateEvent event;
    private String state;
    private ViewerBuilder builder;
    private ViewerListener viewerListener;


    public InnerAsyncTask(String taskKey, String state, Viewer jv, ViewerBuilder builder, ViewerListener viewerListener){
        this.taskKey = taskKey;
        this.state = state;
        this.isStop = false;
        this.jv = jv;
        this.builder = builder;
        this.viewerListener = viewerListener;
    }


    @Override
    protected void onPreExecute() {
//        if(inner_post_ACV.equals(state)){
//            jv.removeAfterOutAnim();
//        }
//        jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams());
//        if(builder.isPreLayout()){
////            jv.makeViewer(true);
//            //FIXME Viewer의 바인드부분을 잘비교하기 바람
//            jv.frame.removeAllViews();
//            jv.frame.addView(jv.viewer, jv.parentView.getLayoutParams());
//        }
//        jv.inner_pre();
        if(jv.TASK_LOAD.equals(state)){ // runLoad일경우
            if(builder.loadController.isEnable()) {  //로딩뷰를 설정했을경우
                //frame에 있는 LoadView를 add하여 화면에 보이게 한다.
                builder.loadController.show(builder.requestCode, jv.frame);
            }
        } else if(jv.TASK_EXCUTE.equals(state)){
            // TODO 작업중 build를 할수없어 builder에 requestCode를 못받는다.
            viewerListener.onPre(55555, jv);
        }else {
            if (builder.isPreLayout()) { // preView를 설정했을 경우
                jv.removeAfterOutAnim();  // 이전에 있던 뷰를 지우고
                jv.parentView.addView(jv.frame,jv. parentView.getLayoutParams()); // layout에  viewer.frame를 넣는다
                jv.frame.addView(jv.viewer); // viewer.frame에 실제 layout을 넣는다.
            }
            if(builder.loadController.isEnable()){ //로딩뷰를 설정했을경우
                jv.removeAfterOutAnim(); // 이전에 있던 뷰를 지우고
                jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams()); // layout에 viewer frame를 넣는다
                //frame에 있는 LoadView를 add하여 화면에 보이게 한다.
                builder.loadController.show(builder.requestCode, jv.frame);
            }
            viewerListener.onPre(builder.requestCode, jv);
        }
    }

    private Exception doInBackException=null;
    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            return viewerListener.onLoad(builder.requestCode, this, jv); //데이터 가져오기
        }catch(Exception e){
            doInBackException = e;
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        if (isStop) {
            builder.requestCode = Viewer.REQUEST_CODE_NONE;
            return;
        }
        if(values != null) {
            viewerListener.onUpdate(builder.requestCode, values[0], jv);
        }else{
            viewerListener.onUpdate(builder.requestCode, null, jv);
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (isStop) {
            builder.requestCode = Viewer.REQUEST_CODE_NONE;
            return;
        }
        if(result){ // 성공의 경우
            if(!jv.TASK_LOAD.equals(state)) { // Anync 호출일때
                if(jv.getParent()==null){
                    return;
                }
                if (!builder.isPreLayout()) { // PreLayout이 아닐때
                    jv.removeAfterOutAnim();
                    jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams());
                    if(jv.isEnablePostView()) {
                        jv.frame.addView(jv.viewer);
                    }
                }
                viewerListener.onPost(builder.requestCode, jv);
            }else{ //runLoad 호출일때
                viewerListener.onPost(builder.requestCode, jv);
            }
        }else{ // 실패의 경우
            viewerListener.onFail(builder.requestCode, doInBackException, jv);
            doInBackException = null;
        }
        jv.asyncTaskPool.remove(taskKey);
    }


    public void stop(){
        if(!isStop) {
            cancel(true);
            isStop = true;
            jv.asyncTaskPool.remove(taskKey);
            Log.w("Viewer", "Stop AsyncTask : " + taskKey);
            jv.onCancelled(builder.requestCode);
        }
    }

    public boolean isStop(){
        return isStop;
    }


    @Override
    public void update(Object value) {
        this.publishProgress(value);
     }

}