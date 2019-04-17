package com.triz.goldenideabox.common.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.shiro.util.StringUtils;

public class ParameterHelper {

    public static List<Integer> StringToIntegerList(String value, String regex) {
        if (value == null || value.length() == 0) {
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

    public static String replaceSpecialStr(String str) {
        String repl = "";
        if (str != null) {
            str = str.replaceAll("（","(").replaceAll("）",")");
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            repl = m.replaceAll("");
        }

        return repl;
    }

    public static String replaceSpecialChar(String str) {
        String repl = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            repl = m.replaceAll("");
        }

        return repl.replaceAll("\\\\n","");
    }

    public static void main(String[] args) {

        List<Integer> lst = new ArrayList<>();
        lst.add(1);
        lst.add(2);
        lst.add(3);
        lst.add(4);
        System.out.println(StringUtils.join(lst.listIterator(), ","));
    }
}
