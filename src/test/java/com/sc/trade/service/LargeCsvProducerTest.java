package com.sc.trade.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import com.sc.trade.constant.CsvHeaderConstant;
import org.junit.jupiter.api.Test;

public class LargeCsvProducerTest {

    @Test
    public void generateCsv() {
        CsvWriter writer = CsvUtil.getWriter("src/test/resources/large_trade.csv", CharsetUtil.CHARSET_UTF_8);
        writer.writeHeaderLine(CsvHeaderConstant.ENRICHED_HEADERS);
        for (int i = 0; i < 1000000; i++) {
            writer.write(new String[]{getRandomDate(), getRandomId(), getRandomCurrency(), getRandomPrice()});
        }
    }

    private String getRandomDate() {
        return DateUtil.format(RandomUtil.randomDay(10, 100), DatePattern.PURE_DATE_PATTERN);
    }

    private String getRandomId() {
        return String.valueOf(RandomUtil.randomInt(0, 15));
    }

    private String getRandomCurrency() {
        return switch (RandomUtil.randomInt(0, 4)) {
            case 0 -> "EUR";
            case 1 -> "USD";
            case 2 -> "CNY";
            default -> "SGD";
        };
    }

    private String getRandomPrice() {
        return String.valueOf(RandomUtil.randomFloat(10, 100));
    }

}
