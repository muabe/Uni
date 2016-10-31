package com.markjmind.uni.mapper.annotiation;


import com.markjmind.uni.UniIntroFragment;
import com.markjmind.uni.mapper.ClassInjectAdapter;


/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-04
 */
public class TimeouttInjector extends ClassInjectAdapter<Timeout> {
    private UniIntroFragment introFragment;

    public TimeouttInjector(UniIntroFragment introFragment){
        this.introFragment = introFragment;
    }

    @Override
    public Class<Timeout> getAnnotationType() {
        return Timeout.class;
    }

    @Override
    public void injectClass(Timeout annotation, Class targetClass, Object targetObject) {
        introFragment.setTimeout(annotation.value());
    }

}
