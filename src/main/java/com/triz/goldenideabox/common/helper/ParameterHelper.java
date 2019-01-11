package com.triz.goldenideabox.common.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterHelper {

    public static List<Integer> StringToIntegerList(String value, String regex) {
        if (value == null || value.length() == 0){
            return null;
        }
        List<Integer> lst = new ArrayList<Integer>();
        for (String s : value.split(regex)) {
            lst.add(Integer.valueOf(s));
        }
        return lst;
    }

    public static List<String> StringToStringList(String value, String regex) {

        return Arrays.asList(value.split(regex));
    }
}
