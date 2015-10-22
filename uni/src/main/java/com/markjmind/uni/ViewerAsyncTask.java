package com.markjmind.uni;

import android.os.AsyncTask;
import android.util.Log;

class ViewerAsyncTask extends AsyncTask<Void, Object, Boolean> implements UpdateEvent{
    private String taskKey;
    private boolean isStop;
    private Viewer jv;
    private UpdateEvent event;


    public ViewerAsyncTask(String taskKey, Viewer jv){
        this.taskKey = taskKey;
        this.isStop = false;
        this.jv = jv;
    }


    @Override
    protected void onPreExecute() {
//        if(jv.TASK_ACV.equals(jv.task)){
//            jv.removeAfterOutAnim();
//        }
//        jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams());
//        if(jv.builder.isPreLayout()){
////            jv.makeViewer(true);
//            //FIXME Viewer의 바인드부분을 잘비교하기 바람
//            jv.frame.removeAllViews();
//            jv.frame.addView(jv.viewer, jv.parentView.getLayoutParams());
//        }
//        jv.inner_pre();
        if(!jv.TASK_LOAD.equals(jv.task)){
            if (jv.builder.isPreLayout()) { // preView를 설정했을 경우
                jv.removeAfterOutAnim();  // 이전에 있던 뷰를 지우고
                jv.parentView.addView(jv.frame,jv. parentView.getLayoutParams()); // layout에  viewer.frame를 넣는다
                jv.frame.addView(jv.viewer); // viewer.frame에 실제 layout을 넣는다.
            }
            if(jv.builder.hasLoadView && jv.builder.loadView!=null) { //로딩뷰를 설정했을경우
                jv.removeAfterOutAnim(); // 이전에 있던 뷰를 지우고
                jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams()); // layout에 viewer frame를 넣는다
                jv.frame.addView(jv.builder.loadView); //로딩뷰 띄우기
                if(jv.builder.updateListener!=null) { //updateListener 있으면 init을 호출해준다.
                    jv.builder.updateListener.init(jv.builder.requestCode, jv.builder.loadView);
                }
            }
            jv.inner_pre(); //onPre 호출
        }else{ // runLoad일경우
            if(jv.builder.hasLoadView && jv.builder.loadView!=null) {  //로딩뷰를 설정했을경우
                jv.frame.addView(jv.builder.loadView); // 이전에 있던 뷰를 지우고
                if(jv.builder.updateListener!=null) { //updateListener 있으면 init을 호출해준다.
                    jv.builder.updateListener.init(jv.builder.requestCode, jv.builder.loadView);
                }
            }
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return jv.inner_load(this); //데이터 가져오기
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        if (isStop) {
            jv.builder.requestCode = Viewer.REQUEST_CODE_NONE;
            return;
        }
        if(values!=null){
            jv.inner_update(values[0]);
        }else{
            jv.inner_update(null);
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (isStop) {
            jv.builder.requestCode = Viewer.REQUEST_CODE_NONE;
            return;
        }
        if(result){ // 성공의 경우
            if(!jv.TASK_LOAD.equals(jv.task)) { // Anync 호출일때
                if(jv.getParent()==null){
                    return;
                }
                if (!jv.builder.isPreLayout()) { // PreLayout이 아닐때
                    jv.removeAfterOutAnim();
                    jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams());
                    jv.frame.addView(jv.viewer);
//                    if (jv.asyncBind(jv, jv.task)) {
//                        jv.inner_post();
//                    }
                }
                jv.inner_post();
            }else{ //runLoad 호출일때
                jv.inner_post();
            }
        }else{ // 실패의 경우
            jv.inner_view_fail();
        }
        jv.asyncTaskPool.remove(taskKey);
    }


    public void stop(){
        if(!isStop) {
            cancel(true);
            isStop = true;
            jv.asyncTaskPool.remove(taskKey);
            Log.w("Viewer", "Stop AsyncTask : " + taskKey);
            jv.onCancelled(jv.builder.requestCode);
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