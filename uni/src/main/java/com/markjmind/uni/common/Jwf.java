package com.markjmind.uni.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-11-28
 */

public class Jwf {


    public static class Number {
        public String zeroX(int x, int position){

//        double d = 123.555;
//        System.out.format("%07.2f", d);
//        // 출력 결과: 0123.56
            // 실수는, 소수점 이하까지 모두 포함한 길이를
            // 기준으로 지정해야 함
            // 그리고 끝에서 반올림되었음
            return String.format("%0"+position+"d", x);
        }

        public static String comma(String num, int digits){
            String pattern = "###,###,###,###,###,###.#######";  //패턴문양
            NumberFormat parser = new DecimalFormat(pattern);  //객체생성
            if(digits > 0) {
                parser.setParseIntegerOnly(false);  //숫자형만 할것인지(true로 할경우 소수점 않나옴)
                parser.setMinimumFractionDigits(digits); //소수점 최소자리
                parser.setMaximumFractionDigits(digits);  //소수점 최대자리
            }else{
                parser.setParseIntegerOnly(true);
            }
            try {
                return parser.format(parser.parse(num)); //패턴형식으로 리턴
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public static String comma(String num){
            return comma(num, 0);
        }

        public static String comma(int num, int digits){
            return comma(""+num, digits);
        }

        public static String comma(int num){
            return comma(""+num, 0);
        }

        public static String comma(long num, int digits){
            return comma(""+num, digits);
        }

        public static String comma(long num){
            return comma(""+num, 0);
        }

        public static String comma(float num, int digits){
            return comma(""+num, digits);
        }

        public static String comma(float num){
            return comma(""+num, 0);
        }

        public static String comma(double num, int digits){
            return comma(""+num, digits);
        }

        public static String comma(double num){
            return comma(""+num, 0);
        }
    }
}
