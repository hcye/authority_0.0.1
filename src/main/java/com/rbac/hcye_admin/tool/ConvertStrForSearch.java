package com.rbac.hcye_admin.tool;


public class ConvertStrForSearch {
    public static String getFormatedString(String str){
        String result="%";
        char[] chars=new char[str.length()];
        str.getChars(0, str.length(),chars, 0);
        for (char c:chars){
            result+=c+"%";
        }
        return result;
    }
}
