package com.casic.cloud.hyperloop.common.utils.Verify.support;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2019/10/23 12:44
 * @version: V1.0
 */
@Setter
@Getter
public abstract class VerifyCode {

    public class VerifyType {
        public static final int TYPE_NUM_ONLY = 0;//验证码类型为仅数字,即0~9
        public static final int TYPE_LETTER_ONLY = 1;//验证码类型为仅字母,即大小写字母混合
        public static final int TYPE_ALL_MIXED = 2;//验证码类型为数字和大小写字母混合
        public static final int TYPE_NUM_UPPER = 3;//验证码类型为数字和大写字母混合
        public static final int TYPE_NUM_LOWER = 4;//验证码类型为数字和小写字母混合
        public static final int TYPE_UPPER_ONLY = 5;//验证码类型为仅大写字母
        public static final int TYPE_LOWER_ONLY = 6;//验证码类型为仅小写字母
    }

    //默认属性，支持外部配置
    private int type = VerifyType.TYPE_ALL_MIXED;//验证码类型,参见本类的静态属性
    private  int length = 4;//验证码字符长度,要求大于0的整数
    private String excludeString = null;//需排除的特殊字符
    private int width = 100;//图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
    private int height = 35;//图片高度
    private int interLine = 10;//图片中干扰线的条数
    private boolean randomLocation = true;//每个字符的高低位置是否随机
    private Color backColor = null;//图片颜色,若为null则表示采用随机颜色
    private Color foreColor = null;//字体颜色,若为null则表示采用随机颜色
    private Color lineColor = null;//干扰线颜色,若为null则表示采用随机颜色
}
