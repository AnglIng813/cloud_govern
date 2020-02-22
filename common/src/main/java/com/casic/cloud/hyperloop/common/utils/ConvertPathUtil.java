package com.casic.cloud.hyperloop.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @Description:URI转正则工具
 * @Author: LDC
 * @Date: 2019/11/15 14:10
 * @version: V1.0
 */
public class ConvertPathUtil {

    /***
     * @Description:通配符转正则
     * @Author: LDC
     * @Date: 2019/11/15 14:37
     */
    public static String getRegPath(String requestURI) {
        char[] chars = requestURI.toCharArray();
        StringBuffer sb = new StringBuffer();
        boolean preX = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '*') {//匹配到*
                if (preX) {//第二次遇到将 ** 替换为 .*
                    sb.append(".*");
                    preX = false;
                } else if (i == chars.length - 1) {//且是最后一个字符，则将 * 转换为 [^/]*
                    sb.append("[^/]*");
                } else { //*后还有字符，不做操作
                    preX = true;
                    continue;
                }
            } else {//非*
                if(preX){//如果上一次是*先把*对应的[^/]*添加进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if(chars[i] == '?'){//？替换为.
                    sb.append(".");
                }else{//普通字符直接添加
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    /***
     * @Description:白名单校验
     * @Author: LDC
     * @Date: 2019/11/15 14:37
     */
    public static boolean isWhiteUrlList(String servletPath, List<String> whiteUrl) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        whiteUrl.stream().forEach(e -> {
            if (isMaches(e, servletPath)) {
                atomicBoolean.getAndSet(true);
            }
        });
        return atomicBoolean.get();
    }

    private static boolean isMaches(String whiteUrl, String servletPath) {
        String regx = ConvertPathUtil.getRegPath(StringUtils.trim(whiteUrl));
        //System.out.println(regx);
        return Pattern.compile(regx).matcher(servletPath).matches();
    }

    public static void main(String[] args) {
        String[] whiteUri = {"/abc/*", "/acd/**", "/*.do", "*.js", "/abc/bcd","/**.html"};
        List<String> list = Arrays.asList(whiteUri);
        System.out.println(isWhiteUrlList("/abc/bcd/ccc/aaa", list));//false
        System.out.println(isWhiteUrlList("/abc/ccc", list));//true
        System.out.println(isWhiteUrlList("/acd/bcd/ccc", list));//true
        System.out.println(isWhiteUrlList("/abc.do", list));//true
        System.out.println(isWhiteUrlList("/abc.js", list));//false
        System.out.println(isWhiteUrlList("/xxx/xxx/xxx/xxx.html", list));//false
    }
}
