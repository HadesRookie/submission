package com.graduation.submission.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @ClassName ValidateUtil
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/21 23:54
 * @Version 1.0
 **/
public class ValidateUtil {
    private static final Pattern P_EMAIL = Pattern
            .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /**
     *
     * @描述：校验email格式
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:38:45
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        return str != null && P_EMAIL.matcher(str).matches();
    }

    private static final Pattern SIMPLE_PASSWORD = Pattern
            .compile("^[0-9_a-zA-Z]{6,20}$");
    public static boolean isSimplePassword(String str) {
        return StringUtils.isNotBlank(str) && SIMPLE_PASSWORD.matcher(str).matches();
    }

    private static final Pattern PASSWORD = Pattern
            .compile("");
    /*private static final Pattern YOUNG_PASSWORD = Pattern
            .compile("");*/
    private static final Pattern CENTER_PASSWORD = Pattern
            .compile("");
    private static final Pattern STRONG_PASSWORD = Pattern
            .compile("");

    /**
     *
     * @描述：密码校验：匹配小写字母、大写字母、数字、特殊符号的两种及两种以上【非中文】
     * @创建人：wyait
     * @创建时间：2017年1月5日15:19:17
     * @param str
     * @return
     */
    public static boolean isPassword(String str) {
        return str != null && PASSWORD.matcher(str).matches();
    }
    /*public static boolean isYoungPassword(String str) {
        return str != null && YOUNG_PASSWORD.matcher(str).matches();
    }*/
    public static boolean isCenterPassword(String str) {
        return str != null && CENTER_PASSWORD.matcher(str).matches();
    }

    public static boolean isStrongPassword(String str) {
        return str != null && STRONG_PASSWORD.matcher(str).matches();
    }

    private static final Pattern P_MOBILEPHONE = Pattern.compile("^1\\d{10}$");

    /**
     *
     * @描述：校验是否为11位1开头手机号
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:39:01
     * @param str
     * @return
     */
    public static boolean isMobilephone(String str) {
        return StringUtils.isNotBlank(str) && P_MOBILEPHONE.matcher(str).matches();
    }

    // 正负数字
    private static final Pattern P_NUMBER = Pattern.compile("^[-\\+]?[\\d]+$");

    /**
     *
     * @描述：校验是否正负数字
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:39:21
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return StringUtils.isNotBlank(str) && P_NUMBER.matcher(str).matches();
    }

    // QQ校验 4_12位
    private static final Pattern QQ = Pattern.compile("[1-9][0-9]{4,12}");

    /**
     *
     * @描述：校验是否为4-12位正整数
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:39:42
     * @param str
     * @return
     */
    public static boolean isQQ(String str) {
        return StringUtils.isNotBlank(str) && QQ.matcher(str).matches();
    }

    // 6位数验证码
    private static final Pattern CODE = Pattern.compile("[0-9]{6}$");

    /**
     *
     * @描述：校验是否为6位数字验证码
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:39:59
     * @param str
     * @return
     */
    public static boolean isCode(String str) {
        return StringUtils.isNotBlank(str) && CODE.matcher(str).matches();
    }

    // 图片验证码 4位随机字母和数字
    private static final Pattern PICCODE = Pattern.compile("\\w{4}$");

    /**
     *
     * @描述：校验是否为4位随机正整数和字母
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:40:15
     * @param str
     * @return
     */
    public static boolean isPicCode(String str) {
        return str != null && PICCODE.matcher(str).matches();
    }

    // 正负数（包含小数、整数）
    private static final Pattern P_DOUBLE = Pattern.compile("");

    /**
     *
     * @描述：校验是否为正负数（包含小数、整数）
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:40:53
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        return str != null && P_DOUBLE.matcher(str).matches();
    }

    // 是否全是中文，如果有其他非中文字符，false
    private static final Pattern P_CHINESE = Pattern
            .compile("^[\u0391-\uFFE5]+$");

    /**
     *
     * @描述：校验是否为中文汉字
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:41:17
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        return str != null && P_CHINESE.matcher(str).matches();
    }

    // 是否包含中文，包含即为true
    private static final Pattern P_CHINESE_A = Pattern
            .compile("[\u0391-\uFFE5]");

    /**
     *
     * @描述：校验是否包含中文
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:41:32
     * @param str
     * @return
     */
    public static boolean hasChinese(String str) {
        return str != null && P_CHINESE_A.matcher(str).find();
    }

    // 搜索条件各名称校验
    private static final Pattern NAME = Pattern
            .compile("");

    /**
     *
     * @描述：搜索名称校验（字母、数字、汉字、下划线等符号）
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:41:49
     * @param str
     * @return
     */
    public static boolean isSearchName(String str) {
        return str != null && NAME.matcher(str).matches();
    }

    /**
     * 银行卡简单校验（16或19位）
     */
    private static final Pattern BANKCODE = Pattern
            .compile("");

    /**
     *
     * @描述：银行卡号校验
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:43:28
     * @param str
     * @return
     */
    public static boolean isBankCode(String str) {
        return str != null && BANKCODE.matcher(str).matches();
    }

    /**
     * 6位数字短信验证码
     */
    private static final Pattern MESCODE = Pattern.compile("^[0-9]{6}$");

    public static boolean isMessageCode(String str) {
        return str != null && MESCODE.matcher(str).matches();
    }

    /**
     * 正整数、小数点后两位    金额校验
     */
    private static final Pattern MONEY = Pattern
            .compile("");

    /**
     *
     * @描述：正整数、小数点后两位    金额校验
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:44:00
     * @param str
     * @return
     */
    public static boolean isMoney(String str) {
        return str != null && MONEY.matcher(str).matches();
    }

    /**
     * 小于等于100的正整数、小数点后两位
     */
    private static final Pattern BAIMONEY = Pattern
            .compile("");

    /**
     *
     * @描述：小于等于100的正整数、小数点后两位
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:44:00
     * @param str
     * @return
     */
    public static boolean isBaiMoney(String str) {
        return str != null && BAIMONEY.matcher(str).matches();
    }

    // 备注校验 可输入：中英文、空格、中英文标点下划线。
    private static final Pattern REMARK = Pattern
            .compile("");

    /**
     *
     * @描述：备注校验 可输入：中英文、空格、中英文标点下划线。
     * @创建人：wyait
     * @创建时间：2016年12月21日 下午1:44:09
     * @param str
     * @return
     */
    public static boolean isRemark(String str) {
        return str != null && REMARK.matcher(str).matches();
    }

    /**
     * 微信号校验
     */
    private static final Pattern WXNUM = Pattern
            .compile("");

    /**
     *
     * @描述：微信号校验
     * @创建人：wyait
     * @创建时间：2016年12月26日17:52:08
     * @param str
     * @return
     */
    public static boolean isWxnum(String str) {
        return str != null && WXNUM.matcher(str).matches();
    }

    /**
     * 用户名校验:字母、数字、中文、下划线组成2-20位【不能以下划线开头】
     */
    private static final Pattern USERNAME = Pattern
            .compile("");

    /**
     *
     * @描述：用户号校验
     * @创建人：wyait
     * @创建时间：2016年12月26日17:52:08
     * @param str
     * @return
     */
    public static boolean isUsername(String str) {
        return str != null && USERNAME.matcher(str).matches();
    }

    /**
     * 用户名校验:字母、中文、点组成2-20位
     */
    private static final Pattern REALRNAME = Pattern
            .compile("");

    /**
     *
     * @描述：真实姓名校验
     * @创建人：wyait
     * @创建时间：2016年12月26日17:52:08
     * @param str
     * @return
     */
    public static boolean isRealname(String str) {
        return str != null && REALRNAME.matcher(str).matches();
    }
    /**
     * 用户名校验:字母、中文、点组成2-20位
     */
    private static final Pattern ADDRIP = Pattern
            .compile("");

    public static boolean isAddrIP(String str) {
        return str != null && ADDRIP.matcher(str).matches();
    }
    //只能输入两位小数的校验
    private static final Pattern MONEY2 = Pattern
            .compile("");

    public static boolean isMONEY2(String str) {
        return str != null && MONEY2.matcher(str).matches();
    }

    /**年份支持1000-3999 支持横线**/
    private static final Pattern DATE=Pattern
            .compile("");
    public static boolean isDate(String str){
        return str != null && DATE.matcher(str).matches();
    }
}
