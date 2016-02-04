package com.markjmind.uni;

import com.markjmind.uni.builder.FragmentBuilder;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-29
 */
public class Uni {

//    public static <T extends UniView>ViewBuilder view(Context context, Class<T> clz){
//        return view(context, clz, null);
//
//    }

//    protected static <T extends UniView>ViewBuilder view(Context context, Class<T> clz, BuildInterface injectionObject){
//        try {
//            T uniV = (T)clz.getConstructor(Context.class).newInstance(context);
//            if(injectionObject==null){
//                injectionObject = uniV;
//            }
//            ViewBuilder builder = new ViewBuilder(uniV, injectionObject);
//            return builder;
//
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public static <T extends UniFragment>FragmentBuilder<T> fragment(Class<T> clz){
        return fragment(clz, null);
    }

    public static <T extends UniFragment>FragmentBuilder<T> fragment(Class<T> clz, Object injectionObject){
        try {
            T uniFragment = (T)clz.newInstance();
            if(injectionObject==null){
                injectionObject = uniFragment;
            }
            FragmentBuilder<T> builder = new FragmentBuilder<>(uniFragment, uniFragment);
            builder.setUniFragment(uniFragment);
            return builder;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static UniFragment create(UniFragment uniFragment, ViewGroup container){
////        UniView uniView = new UniView(uniFragment.getActivity());
////        LayoutInflater inflater = ((LayoutInflater) uniView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
//        uniView.setUniInterface(uniFragment);
////        Mapper mapper = new Mapper(uniView);
////        int layoutId = mapper.injectionLayout(uniFragment);
////        uniView.setLayout((ViewGroup) inflater.inflate(layoutId, container, false));
//        uniView.bindLayout(null);
////        mapper.injectionView(uniFragment);
//        return uniFragment;
//    }

//    public UniView bind(){
//        LayoutInflater inflater = ((LayoutInflater) uniView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
//        Mapper mapper = new Mapper(uniView);
//        int layoutId = mapper.injectionLayout(injectionObject);
//        uniView.setLayout((ViewGroup) inflater.inflate(layoutId, container, false));
//        uniView.bindLayout(container);
//        mapper.injectionView(injectionObject);
//        if(uniInterface!=null) {
//            uniView.setUniInterface(uniInterface);
//        }
//        return uniView;
//    }
}
