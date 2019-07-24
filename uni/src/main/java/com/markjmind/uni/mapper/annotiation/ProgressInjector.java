package com.markjmind.uni.mapper.annotiation;

import com.markjmind.uni.mapper.ClassInjectAdapter;
import com.markjmind.uni.progress.ProgressBuilder;
import com.markjmind.uni.progress.UniProgress;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-05
 */
public class ProgressInjector extends ClassInjectAdapter<Progress> {

    private ProgressBuilder progress;

    public ProgressInjector(ProgressBuilder progress){
        this.progress = progress;
    }

    @Override
    public void injectClass(Progress annotation, Class targetClass, Object targetObject) {
        if(progress!=null) {
            Class<UniProgress> type = (Class<UniProgress>) annotation.type();
            int mode = annotation.mode();

            if (!type.equals(Progress.None.class)) {
                try {
                    UniProgress info = type.newInstance();
                    progress.set(mode, info);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                int res = annotation.res();
                if (res != -1) {
                    progress.set(mode, res);
                }
            }
        }
    }

    @Override
    public Class<Progress> getAnnotationType() {
        return Progress.class;
    }
}