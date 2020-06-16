package com.markjmind.uni;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.LayoutInjector;
import com.markjmind.uni.mapper.annotiation.adapter.AopAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.AutoBinderAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.BinderAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ImportAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnCheckedChangeAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ProgressAdapter;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.CancelObservable;
import com.markjmind.uni.thread.LoadEvent;
import com.markjmind.uni.thread.aop.AopListener;

import java.util.ArrayList;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-04
 */
public class UniTask implements UniInterface, AopListener {
    private UniLayout uniLayout;
    public UniMapper mapper;
    public Store<?> param;
    public ProgressBuilder progressBuilder;


    private Context context;
    private boolean isAsync;

    private TaskController taskController;
    private CancelObservable cancelObservable;
    private boolean enableMapping; //매필을 할수 있는지 여부(UniAsyncTask는 매핑을 안함)

    private ArrayList<UniLayout> importor = new ArrayList<>();

    private boolean binded = false;
//    private LayoutInflater inflater;
//    private ViewGroup container;
    private BindLayoutInfo bindLayoutInfo = new BindLayoutInfo();

    public UniTask() {
        this(false);
    }

    /**
     * Bind를 거치지 않고 내부적으로 UniTask를 생성해서 할 경우
     * enbaleMapping true로 설정
     * @param enableMapping
     */
    UniTask(boolean enableMapping) {
        uniLayout = null;
        setEnableMapping(enableMapping);
        mapper = new UniMapper();
        isAsync = true;
        param = new Store<>();
        progressBuilder = new ProgressBuilder();
        cancelObservable = new CancelObservable();
        taskController = new TaskController(this);
        taskController.addAop(this);
    }

     public BindLayoutInfo getBindLayoutInfo(){
        return bindLayoutInfo;
    }

    private void beforeBind(){
        importor.clear();
        mapper.addSubscriptionOnInit(new ParamAdapter(param));
        if(enableMapping) {
            mapper.addSubscriptionOnInit(new ProgressAdapter(progressBuilder));
            mapper.addSubscriptionOnInit(new AutoBinderAdapter(uniLayout, bindLayoutInfo.getLayoutInflater()));
            mapper.addSubscriptionOnInit(new BinderAdapter(uniLayout, bindLayoutInfo.getLayoutInflater()));
            mapper.addSubscriptionOnInit(new LayoutInjector(bindLayoutInfo.getLayoutInflater(), uniLayout, bindLayoutInfo.getContainer()));
        }
        mapper.addSubscriptionOnStart(new ImportAdapter(uniLayout, importor));
        mapper.addSubscriptionOnStart(new AopAdapter());
        mapper.injectSubscriptionOnInit();
    }

    private void afterBind(){
        mapper.addSubscriptionOnStart(new GetViewAdapter());
        mapper.addSubscriptionOnStart(new OnClickAdapter());
        mapper.addSubscriptionOnStart(new OnCheckedChangeAdapter());
        mapper.injectSubscriptionOnStart();
    }

    private void binding(){
        if(!binded) {
            beforeBind();
            taskController.getUniInterface().onBind();
            afterBind();
            binded = true;
        }
    }

    /**
     * Execute는 여러번 일어날수있다는것에 주의
     */
    void taskBinding() {
        binding();
    }

    void bindImport(){
        for(UniLayout uniLayout : importor){
            uniLayout.getTask().execute();
        }
    }

    public void setBindInfo(Object mappingObj, UniLayout uniLayout, LayoutInflater inflater, ViewGroup container){
        this.uniLayout = uniLayout;
        this.context = uniLayout.getContext();
        this.mapper.reset(this.uniLayout, mappingObj);
//        this.uniLayout.setUniTask(this);
        if (inflater == null) {
            inflater = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        }
        if(container==null){
            container = uniLayout;
        }
        this.progressBuilder.setParents(uniLayout);

        this.bindLayoutInfo.setBindLayoutInfo(inflater, container);
    }


    void initAtrribute(UniLayout uniLayout, UniInterface uniInterface){
        uniLayout.param = param;
        uniLayout.progressBuilder = progressBuilder;
        mapper.setInjectParents(UniLayout.class);
        setUniInterface(uniInterface);
    }

    void initAtrribute(UniFragment uniFragment, UniInterface uniInterface){
        uniFragment.param = this.param;
        uniFragment.progressBuilder = this.progressBuilder;
        mapper.setInjectParents(UniFragment.class);
        setUniInterface(uniInterface);
    }

    void initAtrribute(UniDialog uniDialog, UniInterface uniInterface){
        uniDialog.param = this.param;
        uniDialog.progressBuilder = this.progressBuilder;
        mapper.setInjectParents(UniDialog.class);
        setUniInterface(uniInterface);
    }

    public void syncUniLayout(UniLayout uniLayout){
        uniLayout.param = param;
        uniLayout.progressBuilder = progressBuilder;
        uniLayout.setUniTask(this);
    }



//    public void bindFragment(UniFragment uniFragment) {
//        setEnableMapping(true); //바인드가 되면 매핑을 할수있다.
//        uniLayout.param = param;
//        uniLayout.progressBuilder = progressBuilder;
//        uniFragment.setUniTask(this);
//        //UniFragment에서 새로감싼 uniInterface가 필요하다.
//        setUniInterface(uniFragment.getUniInterface(this));
//        mapper.setInjectParents(UniTask.class);
//        //setBindInfo는 onCreateView에서 동적으로 호출
//    }


    /**
     * UniLayout에다 task를 입히는 방법
     * @param uniLayout
     */
    public void bind(UniLayout uniLayout) {
        setEnableMapping(true); //바인드가 되면 매핑을 할수있다.
        syncUniLayout(uniLayout); // property와 Unitask를 변경한다.
        mapper.setInjectParents(UniTask.class); //Injection 클래스를 UniTask고정
        this.setBindInfo(this, uniLayout, null, null);
        binding();
    }



    CancelObservable getCancelObservable() {
        return cancelObservable;
    }

    void setEnableMapping(boolean enable) {
        this.enableMapping = enable;
    }

    /***************************************************************************************************************
     * Uni 외부지원 함수 관련
     **************************************************************************************************************/

    public View findViewById(int id) {
        return uniLayout.findViewById(id);
    }

    public View findViewWithTag(Object tag) {
        return uniLayout.findViewWithTag(tag);
    }

    public UniLayout getUniLayout() {
        return uniLayout;
    }

    public void setAsync(boolean isAsync) {
        this.isAsync = isAsync;
    }

    public boolean isAsync() {
        return this.isAsync;
    }


    /***************************************************************************************************************
     * Context 함수 관련
     **************************************************************************************************************/
    public Context getContext() {
        return context;
    }

    public Resources getResource() {
        return context.getResources();
    }

    public Context getApplicationContext() {
        return context.getApplicationContext();
    }

    public PackageManager getPackageManager() {
        return context.getPackageManager();
    }

    /***************************************************************************************************************
     * execute 관련
     **************************************************************************************************************/
    public TaskController getTask() {
        if (enableMapping) {
            taskController.init(this, cancelObservable);
        } else {
            taskController.init(null, cancelObservable);
        }
        return taskController;
    }

    public void setUniInterface(UniInterface uniInterface) {
        taskController.setUniInterface(uniInterface);
    }

    public UniInterface getUniInterface() {
        return taskController.getUniInterface();
    }

    /***************************************************************************************************************
     * UniTask Interface 관련
     **************************************************************************************************************/
    @Override
    public void onBind() {

    }

    @Override
    public void onPre() {

    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {

    }

    @Override
    public void onUpdate(Object value, CancelAdapter cancelAdapter) {

    }

    @Override
    public void onPost() {

    }

    @Override
    public void onPostFail(String message, Object arg) {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onCancelled(boolean attached) {

    }

    /***************************************************************************************************************
     * AOP Interface 관련
     **************************************************************************************************************/


    @Override
    public void beforeOnPre() {

    }

    @Override
    public void afterOnPre() {
        bindImport();
    }

    @Override
    public void beforeOnLoad() {

    }

    @Override
    public void afterOnLoad() {

    }

    @Override
    public void beforeOnPost() {

    }

    @Override
    public void afterOnPost() {

    }

    @Override
    public void beforeOnCancel() {

    }

    @Override
    public void afterOnCancel() {

    }

    @Override
    public void beforeOnException(Exception e) {

    }

    @Override
    public void afterOnException(Exception e) {

    }
}

