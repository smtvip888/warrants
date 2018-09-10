package com.sparetime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by muye on 17/11/7.
 */
public class Test {

    @org.junit.Test
    public void test(){

        String s = "<div>123<img src='base64:123456' />234<img src='base64:333222'/>123<div>";
        List<String> list = new ArrayList<>();

        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(s);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                String str_img = m_img.group(2);
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    String str_src = m_src.group(3);
                    list.add(str_src);
                }
                result_img = m_img.find();
            }
        }

        System.out.println(s);
    }
}
