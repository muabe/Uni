package com.markjmind.uni.viewer;

/**
 * Created by codemasta on 2015-11-16.
 */
public abstract class UniAsyncTask {

    private CancelListener cancelListener;

    /**
     *
     * @param requestCode
     * @param build
     */
    public void onBind(int requestCode, ViewerBuilder build, Viewer viewer){}

    /**
     * Thread가 시작되기 전에 화면을 정의한다.
     * @param requestCode
     */
    public void onPre(int requestCode, Viewer viewer){}

    /**
     * 네트워크등 Thread를 정의한다.
     * @param requestCode
     * @param event
     * @return
     * @throws Exception
     */
    public abstract void onLoad(int requestCode, UpdateEvent event, Viewer viewer) throws Exception;

    /**
     *
     * @param requestCode
     * @param value
     */
    public void onUpdate(int requestCode, Object value, Viewer viewer){}

    /**
     * onLoad 리턴값이 true 일때 호출된다.<br>
     * Viewer를 재정의해야할 함수<br>
     * Viewer에 데이터값이나 셋팅하거나 view표현되어야할 값들 셋팅한다.
     * @param requestCode
     */
    public abstract void onPost(int requestCode, Viewer viewer);

    /**
     * onLoad의 리턴값이 false일때 호출된다.<br>
     * 로딩이 실패했을 경우 표현해야할 화면을 정의한다.
     * @param requestCode
     * @param e
     */
    public void onFail(int requestCode, boolean isException, String message, Exception e, Viewer viewer){}

    /**
     *
     * @param requestCode
     */
    public void onCancelled(int requestCode, Viewer viewer){}


    void bind(int requestCode,ViewerBuilder build, Viewer viewer){
        this.onBind(requestCode, build, viewer);
        if(build.getParamStore().size()>0) {
            viewer.param.putAll(build.getParamStore());
        }
        build.getParamStore().clear();
    }

    void pre(int requestCode, Viewer viewer) {
        this.onPre(requestCode, viewer);
    }

    void load(int requestCode, UpdateEvent event, Viewer viewer) throws Exception {
        viewer.setEnablePostView(true);
        this.onLoad(requestCode, event, viewer);
    }

    void update(int requestCode, Object value, Viewer viewer) {
        this.onUpdate(requestCode, value, viewer);
        if(viewer.builder.progressController.progressViewListener !=null){
            viewer.builder.progressController.progressViewListener.onUpdate(requestCode, viewer.builder.progressController.progressView, value);
        }
    }

    void post(int requestCode, Viewer viewer) {
        if(viewer.getParent()==null || viewer.frame==null){
            if(!viewer.isPrepare) {
                return;
            }
        }

        //액티비티나 다이얼로그가 종료되다면 cancel한다.
        if(viewer.builder.mode.equals(ViewerBuilder.TYPE_MODE.ACTIVITY)){
            if(viewer.getActivity().isFinishing()){
                Viewer.cancelTaskAll(viewer);
                viewer.builder.requestCode = Viewer.REQUEST_CODE_NONE;
                return;
            }
        }else if(viewer.builder.mode.equals(ViewerBuilder.TYPE_MODE.DIALOG)){
            if(!viewer.getDialog().isShowing()){
                Viewer.cancelTaskAll(viewer);
                viewer.builder.requestCode = Viewer.REQUEST_CODE_NONE;
                return;
            }
        }else{
            viewer.builder.requestCode = Viewer.REQUEST_CODE_NONE;
            return;
        }

        // 로딩뷰를 설정했는지 여부에따라 로딩뷰를 삭제한다.
        // 로딩뷰는 한번뜨고 메모리에서 사라진다.(재사용시 데이터 초기화 문제)
        if(viewer.builder.progressController.isShow(viewer.frame)){
            viewer.builder.progressController.onDestroy(viewer.builder.requestCode, viewer.frame);
        }

        if(viewer.isPrepare){
            this.onPost(requestCode, viewer);
        }else{
            //hashCode가 구조상 유일 아이디가 될수있음을 확인함
            int hashId = viewer.frame.hashCode();
            viewer.frame.setId(hashId);
            if(viewer.findGobalView(hashId)!=null){
                this.onPost(requestCode, viewer);
            }
            viewer.frame.setId(-1);
        }
        viewer.builder.requestCode = Viewer.REQUEST_CODE_NONE;
    }

    void fail(int requestCode, boolean isException, String message, Exception e, Viewer viewer) {
        if(viewer.getParent()!=null && viewer.frame!=null){
            // 로딩뷰를 설정했는지 여부에따라 로딩뷰를 삭제한다.
            if(viewer.builder.progressController.isShow(viewer.frame)){
                viewer.builder.progressController.onDestroy(viewer.builder.requestCode, viewer.frame);
            }
        }
        this.onFail(requestCode, isException, message, e, viewer);
        viewer.builder.requestCode = Viewer.REQUEST_CODE_NONE;
    }

    void stop(int requestCode, Viewer viewer) {
        if(viewer.getParent()!=null && viewer.frame!=null){
            // 로딩뷰를 설정했는지 여부에따라 로딩뷰를 삭제한다.
            if(viewer.builder.progressController.isShow(viewer.frame)){
                viewer.builder.progressController.onDestroy(viewer.builder.requestCode, viewer.frame);
            }
        }
        this.onCancelled(requestCode, viewer);
        if(cancelListener!=null){
            cancelListener.onCancel(viewer.builder.requestCode, viewer);
        }
        viewer.builder.requestCode = Viewer.REQUEST_CODE_NONE;
    }

    public void setCancelListener(CancelListener cancelListener){
        this.cancelListener = cancelListener;
    }
}
