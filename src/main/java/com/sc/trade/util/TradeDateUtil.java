package com.sc.trade.util;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TradeDateUtil extends DateUtil {

    public boolean isValidDate(String date) {
        try {
            DateUtil.parse(date, DatePattern.PURE_DATE_PATTERN);
            return true;
        } catch (DateException e) {
            return false;
        }
    }
}
