package com.markjmind.uni.common;

import android.util.Log;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-05-18
 */
public class SimpleLog {
    private Class<?> tagClass;
    private String tag;
    public static boolean isLog = true;
    private LogListener listener;

    private int callNum = 5;

    public SimpleLog(Class<?> tagClass){
        this.tagClass = tagClass;
        this.tag = tagClass.getSimpleName();
    }

    public SimpleLog(Class<?> tagClass, LogListener listener){
        this.tagClass = tagClass;
        this.tag = tagClass.getSimpleName();
        this.setLogListener(listener);
    }

    public SimpleLog(Class<?> tagClass, String tag){
        this.tagClass = tagClass;
        this.tag = tag;
    }

    public void e(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("e", tag, msg);
            }
            Log.e(getLogPoint(), msg);
        }
    }

    public void i(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("i", tag, msg);
            }
            Log.i(getLogPoint(), msg);
        }
    }

    public void d(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("d", tag, msg);
            }
            Log.d(getLogPoint(), msg);
        }
    }

    public void w(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("w", tag, msg);
            }
            Log.w(getLogPoint(), msg);
        }
    }

    public void ev(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("e", tag, msg);
            }
        }
    }

    public void iv(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("i", tag, msg);
            }
        }
    }

    public void dv(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("d", tag, msg);
            }
        }
    }

    public void wv(String msg){
        if(SimpleLog.isLog) {
            if(listener!=null){
                listener.log("w", tag, msg);
            }
        }
    }

    public void setCallNum(int callNum){
        this.callNum = callNum;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public Class getTagClass(){
        return this.tagClass;
    }

    private String getLogPoint(){
        return ".Log"+Loger.callClass(tagClass);
    }

    public static void setLog(boolean isLog){
        SimpleLog.isLog = isLog;
    }

    public void setLogListener(LogListener listener){
        this.listener = listener;
    }

    public interface LogListener{
        void log(String state, String tag, String log);
    }
}
