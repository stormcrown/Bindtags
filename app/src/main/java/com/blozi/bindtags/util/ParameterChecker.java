package com.blozi.bindtags.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @aothor 骆长涛
 * @datetime 2018/1/23 8:38
 * @description 用于参数检测
 **/
public class ParameterChecker {
    private static final Pattern integerNumPattern = Pattern.compile("[0-9]*");

    /**
     * @aothor 骆长涛
     * @description 检测字符串参数 ，当参数不为空，且去除前后空格后不为空串时返回true
     * @datetime 2018/1/23 8:38
     * @param parameter 待检测字符串
     * @return 当参数不为空，且去除前后空格后不为空串时返回true；否则返回false
     * */
    public static boolean checkStringParameter(String parameter){
        if (parameter!=null && !"".equals(parameter.trim())){
            return true;
        }
        return false;
    }
    public static String getString(Object parameter){
        if (parameter!=null && !"".equals(parameter.toString().trim())  ){
            if(parameter instanceof String) return  ((String) parameter).trim();
            else return parameter.toString();
        }
        return "";
    }
    /**
     * @aothor 骆长涛
     * @decription 检测参数是否为整数类型参数
     * @datetime 2018/1/23 8:38
     * @param parameter 待检测的参数
     * @return  当参数为整数型数据时，返回true,否则返回false
     * */
    public static boolean checkIntegerNumParameter(String parameter){
        if(checkStringParameter(parameter)){
            Matcher isIntegerNum = integerNumPattern.matcher(parameter);
            return isIntegerNum.matches();
        }
        return false;
    }
    /**
     * @aothor 骆长涛
     * @datetime 2018-01-24 08:44:00
     * @description 检测参数是否为 y 或者 n
     * @param parameter
     * @return 如果字符串为 y 或者 n 则返回True 否则为false
     *
     * */
    public  static boolean checkYesNo(String parameter){
        if(checkStringParameter(parameter) && ("y".equals(parameter) || "n".equals(parameter) )){
            return true;
        }
        return false;
    }
    /**
     * @aothor 骆长涛
     * @datetime 2018-02-12 08:44:00
     * @description 检测参数是否为 布尔类型
     * @param parameter
     * @return 如果字符串为 true 或者 false 则返回True 否则为false
     *
     * */
    public  static boolean checkBoolean(String parameter){
        if(checkStringParameter(parameter) && ("true".equalsIgnoreCase(parameter.trim()) || "false".equalsIgnoreCase(parameter.trim()) )){
            return true;
        }
        return false;
    }
    /**
     * @aothor 骆长涛
     * @datetime 2018-02-12 08:44:00
     * @description 检测参数是否为 布尔类型
     * @param parameter
     * @return 如果字符串为 true 或者 false 则返回True 否则为false
     *
     * */
    public  static Boolean getBoolean(String parameter){
        if(checkBoolean(parameter) ){
            return Boolean.parseBoolean(parameter);
        }
        return null;
    }
}
