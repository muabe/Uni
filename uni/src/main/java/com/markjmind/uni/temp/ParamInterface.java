/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.temp;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
interface ParamInterface<T>{
    /**
     * 다른 Viewer로 넘기는 파라미터를 설정한다.
     *
     * @param key   Parameter Key
     * @param value Parameter Value
     */

    public T addParam(String key, Object value);

    /**
     * 다른 Viewer로 전달한 파라미터를 받는다.
     *
     * @param key Parameter Key
     * @return Parameter Value
     */
    public Object getParam(String key);

    /**
     * 다른 Viewer로 전달한 파라미터를 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter Value
     */
    public Object optParam(String key, Object defalut);

    /**
     * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.
     *
     * @param key Parameter Key
     * @return Parameter String Value
     */
    public String getParamString(String key);

    /**
     * 다른 Viewer로 전달한 파라미터를 String형으로 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter String Value
     */
    public String optParamString(String key, String defalut);

    /**
     * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.
     *
     * @param key Parameter Key
     * @return Parameter Int Value
     */
    public int getParamInt(String key);

    /**
     * 다른 Viewer로 전달한 파라미터를 Int형으로 받는다.<br>
     * key에 대응하는 value가 없으면 defalut값을 리턴한다.
     *
     * @param key     Parameter Key
     * @param defalut value
     * @return Parameter Int Value
     */
    public int optParamInt(String key, int defalut);

}
