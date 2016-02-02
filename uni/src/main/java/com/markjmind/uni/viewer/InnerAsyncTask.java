package com.markjmind.uni.viewer;

import android.os.AsyncTask;
import android.util.Log;

import com.markjmind.uni.exception.UniLoadFailException;

class InnerAsyncTask extends AsyncTask<Void, Object, Boolean> implements UpdateEvent{
    private String taskKey;
    private boolean isStop;
    private Viewer jv;
    private UpdateEvent event;
    private String state;
    private ViewerBuilder builder;
    private UniAsyncTask uniAsyncTask;


    public InnerAsyncTask(String taskKey, String state, Viewer jv, ViewerBuilder builder, UniAsyncTask uniAsyncTask){
        this.taskKey = taskKey;
        this.state = state;
        this.isStop = false;
        this.jv = jv;
        this.builder = builder;
        this.uniAsyncTask = uniAsyncTask;
    }


    @Override
    protected void onPreExecute() {
        if(Viewer.TASK_LOAD.equals(state)){ /** runLoad일경우 */
            if(builder.progressController.isEnable()) {  //로딩뷰를 설정했을경우
                //frame에 있는 LoadView를 add하여 화면에 보이게 한다.
                builder.progressController.show(builder.requestCode, jv.frame);
            }
        } else if(Viewer.TASK_EXCUTE.equals(state)){  /** excute를 했을경우 */
            if(builder.progressController.isEnable()) {  //로딩뷰를 설정했을경우
                //frame에 있는 LoadView를 add하여 화면에 보이게 한다.
                builder.progressController.show(builder.requestCode, jv.frame);
            }
            uniAsyncTask.pre(jv.builder.requestCode, jv);
        }else { /** Anync 호출일때 preView를 설정했을 경우(change, add시) */
            if (builder.isPreLayout()) { // preView를 설정했을 경우
                jv.removeAfterOutAnim();  // 이전에 있던 뷰를 지우고
                jv.parentView.addView(jv.frame,jv. parentView.getLayoutParams()); // layout에  viewer.frame를 넣는다
                jv.frame.addView(jv.viewer); // viewer.frame에 실제 layout을 넣는다.
            }
            if(builder.progressController.isEnable()){ //로딩뷰를 설정했을경우
                jv.removeAfterOutAnim(); // 이전에 있던 뷰를 지우고
                jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams()); // layout에 viewer frame를 넣는다
                //frame에 있는 LoadView를 add하여 화면에 보이게 한다.
                builder.progressController.show(builder.requestCode, jv.frame);
            }
            uniAsyncTask.pre(builder.requestCode, jv);
        }
    }

    private Exception doInBackException=null;
    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            uniAsyncTask.load(builder.requestCode, this, jv); //데이터 가져오기
            return true;
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
            uniAsyncTask.update(builder.requestCode, values[0], jv);
        }else{
            uniAsyncTask.update(builder.requestCode, null, jv);
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (isStop) {
            builder.requestCode = Viewer.REQUEST_CODE_NONE;
            return;
        }
        if(result){ // 성공의 경우
            if(jv.TASK_LOAD.equals(state)) { /** runLoad일경우 */
                uniAsyncTask.post(builder.requestCode, jv);
            }else if(jv.TASK_EXCUTE.equals(state)) {  /** excute를 했을경우 */
                uniAsyncTask.post(builder.requestCode, jv);
            }else { /** Anync 호출일때 */
                if(jv.getParent()==null){
                    return;
                }
                if (!builder.isPreLayout()) { // PreLayout이 아닐때 화면을 지우고 layout add
                    jv.removeAfterOutAnim();
                    jv.parentView.addView(jv.frame, jv.parentView.getLayoutParams());
                    if(jv.isEnablePostView()) {
                        jv.frame.addView(jv.viewer);
                    }
                }
                uniAsyncTask.post(builder.requestCode, jv);
            }
        }else{ // 실패의 경우
            if(doInBackException!=null && doInBackException instanceof UniLoadFailException){
                uniAsyncTask.fail(builder.requestCode, false, doInBackException.getMessage(), null, jv);
            }else{
                uniAsyncTask.fail(builder.requestCode, true, doInBackException.getMessage(), doInBackException, jv);
            }

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
            uniAsyncTask.stop(builder.requestCode, jv);
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