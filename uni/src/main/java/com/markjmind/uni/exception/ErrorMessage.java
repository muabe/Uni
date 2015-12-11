package com.markjmind.uni.exception;

/**
 * Created by markj on 2015-12-09.
 */
public class ErrorMessage {

    public static class Runtime{
        public static String inflater(Class<?> viewerClass){
            String msg =
                    "지정한 Layout resource를 읽을수 없습니다." +
                    "\n해당 Viewer에서 지정한 layout.id 를 확인하시기 바랍니다."
                    + "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String box(Class<?> viewerClass){
            String msg =
                    "@Box을 지정하는 annotation의 value가 잘못되었습니다."
                    + "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectLayout(Class<?> viewerClass){
            String msg =
                    "@Layout을 지정하는 annotation의 value가 잘못되었습니다."
                    + "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectMethod(Class<?> viewerClass){
            String msg =
                    "method에 해당하는 ID(Null)가 잘못 지정되었습니다."
                    + "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }



    }

    private static String boxLine(String msg){
        String box =
                "\n------------------------------------------------------------------------------------------\n" +
                msg +
                "\n------------------------------------------------------------------------------------------";
        return box;
    }

    private static String javaFile(Class<?> clz){
        return clz.getName()+"("+clz.getSimpleName()+".java:0)";
    }
}
