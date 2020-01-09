package com.thtf.common.log.util.ip;

import com.thtf.common.log.util.ip.entity.PhoneInfo;
import com.thtf.common.log.util.ip.utils.IPUtil;
import com.thtf.common.log.util.ip.utils.PhoneUtils;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @GitHub https://github.com/niezhiliang
 * @Date 2019-08-14 11:02
 */
public class Test {

    public static void main(String[] args) {

        PhoneUtils phoneUtils = new PhoneUtils();
        PhoneInfo phoneInfo = phoneUtils.lookup("13521866141");

        System.out.println(phoneInfo);

        String  cityInfo = IPUtil.getCityInfo("111.196.184.62");

        System.out.println(cityInfo);
    }

}
