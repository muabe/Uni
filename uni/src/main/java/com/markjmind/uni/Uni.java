package com.markjmind.uni;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-03-07
 */
public class Uni {

    public static <T extends UniTask>T bind(UniLayout uniLayout, T uniTask){
        uniTask.init(uniLayout, uniTask, uniTask, false, null);
        return uniTask;
    }

    public static <T extends UniTask>T bind(Activity activity, int unilayout_id, T uniTask){
        UniLayout uniLayout = (UniLayout)activity.findViewById(unilayout_id);
        return Uni.bind(uniLayout, uniTask);
    }

    public static <T extends UniTask>T bind(Dialog dialog, int unilayout_id, T uniTask){
        UniLayout uniLayout = (UniLayout)dialog.findViewById(unilayout_id);
        return Uni.bind(uniLayout, uniTask);
    }

//    public static <T extends UniTask>T bind(UniLayout uniLayout, Class<T> uniTaskClass){
//        try {
//            T uniTask = (T)uniTaskClass.newInstance();
//            uniTask.init(uniLayout, uniTask, uniTask, false, null);
//            return uniTask;
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//            throw new UinMapperException("", null);
//        } catch (IllegalAccessException e) {
//            throw new UinMapperException("", null);
//        }
//    }
//    public static <T extends UniTask>T bind(Activity activity, int unilayout_id, Class<T> uniTaskClass){
//        UniLayout uniLayout = (UniLayout)activity.findViewById(unilayout_id);
//        return Uni.bind(uniLayout, uniTaskClass);
//    }
//
//    public static <T extends UniTask>T bind(Dialog dialog, int unilayout_id, Class<T> uniTaskClass){
//        UniLayout uniLayout = (UniLayout)dialog.findViewById(unilayout_id);
//        return Uni.bind(uniLayout, uniTaskClass);
//    }

    public static <T extends UniTask>T create(Context context, T uniTask){
        uniTask.init(new UniLayout(context), uniTask, uniTask, true, null);
        return uniTask;

    }

    public static <T extends UniTask>T add(ViewGroup parent, T uniTask){
        parent.addView(uniTask.getUniLayout());
        return uniTask;
    }
    public static <T extends UniTask>T add(Activity activity, int parent_id, T uniTask){
        ViewGroup parent = (ViewGroup)activity.findViewById(parent_id);
        return Uni.add(parent, uniTask);
    }
    public static <T extends UniTask>T add(Dialog dialog, int parent_id, T uniTask){
        ViewGroup parent = (ViewGroup)dialog.findViewById(parent_id);
        return Uni.add(parent, uniTask);
    }


    public static <T extends UniLayout>T  replace(ViewGroup parents, T uniLayout){
        UniTask uniTask = new UniTask();
        parents.removeAllViews();
        parents.addView(uniLayout);
        uniTask.init(uniLayout, uniLayout, uniLayout, true, parents);
        return uniLayout;
    }
    public static <T extends UniLayout>T replace(Activity activity, int parent_id, T uniLayout){
        ViewGroup parent = (ViewGroup)activity.findViewById(parent_id);
        return Uni.replace(parent, uniLayout);
    }
    public static <T extends UniLayout>T replace(UniDialog dialog, int parent_id, T uniLayout){
        ViewGroup parent = (ViewGroup)dialog.findViewById(parent_id);
        return Uni.replace(parent, uniLayout);
    }


//    public static <T extends UniTask>T add(ViewGroup parent, Class<T> uniTaskClass){
//        T uniTask = Uni.create(parent.getContext(), uniTaskClass);
//        parent.addView(uniTask.getUniLayout());
//        return uniTask;
//    }
//
//    public static <T extends UniTask>T add(Activity activity, int parent_id, Class<T> uniTaskClass){
//        ViewGroup parent = (ViewGroup)activity.findViewById(parent_id);
//        return Uni.add(parent, uniTaskClass);
//    }
//
//    public static <T extends UniTask>T add(Dialog dialog, int parent_id, Class<T> uniTaskClass){
//        ViewGroup parent = (ViewGroup)dialog.findViewById(parent_id);
//        return Uni.add(parent, uniTaskClass);
//    }
//
//    public  <T extends UniTask>T replace(ViewGroup parent, Class<T> uniTaskClass){
//        parent.removeAllViews();
//        return Uni.add(parent, uniTaskClass);
//    }
//
//    public  <T extends UniTask>T replace(Activity activity, int parent_id, Class<T> uniTaskClass){
//        ViewGroup parent = (ViewGroup)activity.findViewById(parent_id);
//        parent.removeAllViews();
//        return Uni.add(parent, uniTaskClass);
//    }
//
//    public  <T extends UniTask>T replace(Dialog dialog, int parent_id, Class<T> uniTaskClass){
//        ViewGroup parent = (ViewGroup)dialog.findViewById(parent_id);
//        parent.removeAllViews();
//        return Uni.add(parent, uniTaskClass);
//    }
//
//    public  <T extends UniTask>T replace(UniLayout uniLayout, int parent_id, Class<T> uniTaskClass){
//        ViewGroup parent = (ViewGroup)uniLayout.getRootView().findViewById(parent_id);
//        parent.removeAllViews();
//        return Uni.add(parent, uniTaskClass);
//    }



}
