package com.sc.trade.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TradeDateUtil {

    /**
     * higher performance than parsing the date string
     */
    public boolean isValidDate(String dateStr) {
        if (StrUtil.isBlank(dateStr) || dateStr.length() != 8 || !NumberUtil.isInteger(dateStr))
            return false;

        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 6));
        int day = Integer.parseInt(dateStr.substring(6, 8));

        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;

        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            return false;
        }

        if (month == 2) {
            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
            if (isLeapYear && day > 29) {
                return false;
            } else return isLeapYear || day <= 28;
        }

        return true;
    }
}

