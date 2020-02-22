package com.casic.cloud.hyperloop.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:校验工具
 * @Author: LDC
 * @Date: 2019/07/31 17:32
 * @version: V1.0
 */
public class ValidateUtil {

    /*密码长度至少为8位，必须包含大小写字母/数字/符号任意两者组合*/
    public static final String PWD_VALIDATOR = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$)^.{8,}$";
    public static final Pattern PWD_PATTERN = Pattern.compile(PWD_VALIDATOR);
    public static final int MARK = 33; //字符不包含空格
    public static final int NUMBER = 48;//数字
    public static final int UPPERCASE = 65;//大写字母
    public static final int LOWERCASE = 97;//小写字母
    public static Integer length = 8; //默认长度

    /**
     * @Description: 密码校验
     * @Author: LDC
     * @Date: 2019/8/5 9:25
     */
    public static boolean validatePwd(String pwd) {
        if (StringUtils.isEmpty(pwd)) {
            return false;
        }
        String str = pwd.replaceAll("\\s*", "");

        Matcher matcher = PWD_PATTERN.matcher(str);
        return matcher.matches();
    }

    /**
     * @Description: 根据策略生成密码
     * 密码长度至少为8位，必须包含大小写字母/数字/符号任意两者组合
     * @Author: LDC
     * @Date: 2019/8/5 9:25
     */
    public static String initPwd() {
        String pwd = "";
        Random random = new Random();
        int[] code = new int[4];
        for (int i = 0; i < length; i++) {
            initCodeArr(random, code);//每次重置数组内容，增加密码强度
            pwd+=(char)code[random.nextInt(4)];
        }
        if(validatePwd(pwd)) return pwd;
        return initPwd();
    }

    private static void initCodeArr(Random random, int[] code) {
        code[0] = MARK + random.nextInt(15);
        code[1] = NUMBER + random.nextInt(10);
        code[2] = UPPERCASE + random.nextInt(26);
        code[3] = LOWERCASE + random.nextInt(26);
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public static void main(String[] args) {
        String pwd = initPwd();
        System.out.println(pwd);
        System.out.println(validatePwd(pwd));

    }
}
