package com.casic.cloud.hyperloop.common.utils.Verify;

import com.casic.cloud.hyperloop.common.utils.Verify.support.VerifyCode;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成器
 *
 * @see --------------------------------------------------------------------------------------------------------------
 * @see 可生成数字、大写、小写字母及三者混合类型的验证码
 * @see 支持自定义验证码字符数量,支持自定义验证码图片的大小,支持自定义需排除的特殊字符,支持自定义干扰线的数量,支持自定义验证码图文颜色
 * @see --------------------------------------------------------------------------------------------------------------
 * @see 另外,给Shiro加入验证码有多种方式,也可以通过继承修改FormAuthenticationFilter类,通过Shiro去验证验证码
 * @see 而这里既然使用了SpringMVC,也为了简化操作,就使用此工具生成验证码,并在Controller中处理验证码的校验
 * @see --------------------------------------------------------------------------------------------------------------
 */
@Slf4j
public class VerifyCodeUtil extends VerifyCode {

    /**
     * 生成随机背景颜色 180-240 区间
     */
    private Color generateRandomBgColor() {
        Random random = new Random();
        return new Color(random.nextInt(61) + 180, random.nextInt(61) + 180, random.nextInt(61) + 180);
    }

    /**
     * 生成随机干扰线颜色 0-255 区间
     */
    private Color generateRandomLineColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * 生成随机字体颜色 50-160 区间
     */
    private Color generateRandomTxtColor() {
        Random random = new Random();
        return new Color(random.nextInt(111) + 50, random.nextInt(111) + 50, random.nextInt(111) + 50);
    }


    /**
     * 主方法，生成图片验证码
     *
     * @return 图片流
     */
    public void generateImageCode(HttpSession session, HttpServletResponse response) {
        String verifyCode = this.generateTextCode();
        BufferedImage bufferedImage = this.generateImage(verifyCode);
        try {
            ImageIO.write(bufferedImage, "jpeg", response.getOutputStream());
            session.setAttribute("verifyCode", verifyCode);
            log.info("【主方法，生成图片验证码】已将验证码放入session,verifyCode={}", verifyCode);
        } catch (Exception e) {
            log.error("【主方法，生成图片验证码】失败");
            e.printStackTrace();
        }
    }

    /**
     * 生成验证码字符串
     *
     * @return 验证码字符串
     */
    public String generateTextCode() {
        if (this.getLength() <= 0) {
            return "";
        }
        StringBuffer verifyCode = new StringBuffer();
        int i = 0;
        Random random = new Random();
        switch (this.getType()) {
            case VerifyType.TYPE_NUM_ONLY:
                while (i < this.getLength()) {
                    int t = random.nextInt(10);
                    //排除特殊字符
                    if (null == this.getExcludeString() || this.getExcludeString().indexOf(t + "") < 0) {
                        verifyCode.append(t);
                        i++;
                    }
                }
                break;
            case VerifyType.TYPE_LETTER_ONLY:
                while (i < this.getLength()) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 65 && t <= 90)) && (null == this.getExcludeString() || this.getExcludeString().indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case VerifyType.TYPE_ALL_MIXED:
                while (i < this.getLength()) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57)) && (null == this.getExcludeString() || this.getExcludeString().indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case VerifyType.TYPE_NUM_UPPER:
                while (i < this.getLength()) {
                    int t = random.nextInt(91);
                    if ((t >= 65 || (t >= 48 && t <= 57)) && (null == this.getExcludeString() || this.getExcludeString().indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case VerifyType.TYPE_NUM_LOWER:
                while (i < this.getLength()) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 48 && t <= 57)) && (null == this.getExcludeString() || this.getExcludeString().indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case VerifyType.TYPE_UPPER_ONLY:
                while (i < this.getLength()) {
                    int t = random.nextInt(91);
                    if ((t >= 65) && (null == this.getExcludeString() || this.getExcludeString().indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case VerifyType.TYPE_LOWER_ONLY:
                while (i < this.getLength()) {
                    int t = random.nextInt(123);
                    if ((t >= 97) && (null == this.getExcludeString() || this.getExcludeString().indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
        }
        return verifyCode.toString();
    }

    /**
     * 已有验证码,生成验证码图片
     *
     * @return 图片缓存对象
     */
    public BufferedImage generateImage(String textCode) {
        //创建内存图像
        BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics graphics = bufferedImage.getGraphics();
        //画背景图
        graphics.setColor(null == this.getBackColor() ? generateRandomBgColor() : this.getBackColor());
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        //画干扰线
        Random random = new Random();
        if (this.getInterLine() > 0) {
            int x = 0, y = 0, x1 = this.getWidth(), y1 = 0;
            for (int i = 0; i < this.getInterLine(); i++) {
                graphics.setColor(null == this.getLineColor() ? generateRandomBgColor() : this.getLineColor());
                y = random.nextInt(this.getHeight());
                y1 = random.nextInt(this.getHeight());
                graphics.drawLine(x, y, x1, y1);
            }
        }
        //字体大小为图片高度的80%
        int fsize = (int) (this.getHeight() * 0.75);
        int fx = this.getHeight() - fsize;
        int fy = fsize;
        //设定字体
        graphics.setFont(new Font("Default", Font.PLAIN, fsize));
        //写验证码字符
        for (int i = 0; i < textCode.length(); i++) {
            fy = this.isRandomLocation() ? (int) ((Math.random() * 0.3 + 0.6) * this.getHeight()) : fy;
            graphics.setColor(null == this.getForeColor() ? generateRandomTxtColor() : this.getForeColor());
            //将验证码字符显示到图象中
            graphics.drawString(textCode.charAt(i) + "", fx, fy);
            fx += fsize * 0.9;
        }
        graphics.dispose();
        return bufferedImage;
    }
}
