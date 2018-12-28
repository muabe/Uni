package com.markjmind.uni.exception;

import java.lang.reflect.Field;

/**
 * Created by markj on 2015-12-09.
 */
public class ErrorMessage {

    public static class Runtime{
        public static String inflater(Class<?> viewerClass){
            String msg =
                        "지정한 Layout resource를 읽을수 없습니다." +
                        "\n해당 Viewer에서 지정한 layout.id 를 확인하시기 바랍니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String box(Class<?> viewerClass){
            String msg =
                        "@Box을 지정하는 annotation의 value가 잘못되었습니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectLayout(Class<?> viewerClass){
            String msg =
                         "@Layout을 지정하는 annotation의 value가 잘못되었습니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectMethod(Class<?> viewerClass, String mehodName){
            String msg =
                        "@OnClick을 지정하는 annotation의 value가 잘못되었습니다."+
                                "\n"+mehodName+" 메소드와 매핑할 R.id 를 확인하시기 바랍니다."+
                        "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectField(Class<?> viewerClass, String fieldName){
            String msg =
                    "@GetView를 지정하는 annotation의 value가 잘못되었습니다." +
                            "\n"+fieldName+" 필드와 매핑할 R.id 를 확인하시기 바랍니다."+
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectParam(Class<?> viewerClass, String fieldName){
            String msg =
                    "@Param를 지정하는 annotation의 value가 잘못되었습니다." +
                            "\n"+fieldName+" 필드와 매핑할 R.id 를 확인하시기 바랍니다."+
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectParamNull(Class<?> viewerClass, Field field){
            String msg =
                    "@Param으로 지정된 "+field.getType().getName()+" "+field.getName()+" 에는 null 값이 들어갈수 없는 field입니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String injectFieldIllegalAccess(Class<?> viewerClass, String fieldName){
            String msg =
                        fieldName+"에 접근권한이 없습니다."+
                        "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }


        public static String fieldSecurity(Class<?> viewerClass, String fieldName){
            String msg =
                        fieldName+" 필드 SecurityException" +
                        "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String fieldNoSuch(Class<?> viewerClass, String fieldName){
            String msg =
                        ""+fieldName+" 필드와 매핑되는 R.id."+fieldName+" 가 존재하지 않습니다." +
                        "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String fieldIllegalArgument(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 필드와 매핑되는 R.id."+fieldName+" 를 잘못설정 하였습니다.." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String fieldIllegalAccess(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 필드에 접근권한이 없는 필드입니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }


        public static String  methodSecurity(Class<?> viewerClass, String fieldName){
            String msg =
                    fieldName+" 메소드 SecurityException" +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String  methodNoSuch(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 메소드와 매핑되는 R.id."+fieldName+" 가 존재하지 않습니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String  methodIllegalArgument(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 메소드와 매핑되는 R.id."+fieldName+" 를 잘못설정 하였습니다.." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String  methodIllegalAccess(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 메소드에 접근권한이 없는 필드입니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String fieldInstantiation(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 클래스에 객체를 생성할수 없습니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String defalutConstructInstantiation(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 클래스에 기본생성자가 반드시 있어야 합니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String uniLayoutClassCast(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 클래스가 UniLayout Type이 아닙니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String uniLayoutNoSuchMethod(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" UniLayout Type이 아니거나 매개변수로 Context 받는 생성자가 없습니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String invocationTarget(Class<?> viewerClass, String fieldName){
            String msg =
                    ""+fieldName+" 생성자 또는 메소드를 실행할수 없거나 객체를 생성할수 없습니다." +
                            "\n"+javaFile(viewerClass);
            return boxLine(msg);
        }

        public static String batchException(Class batchClass){
            String msg = batchClass.getName() + "배치 클래스를 생성할수 없습니다.";
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
