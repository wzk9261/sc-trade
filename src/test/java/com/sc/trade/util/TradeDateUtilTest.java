package com.sc.trade.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TradeDateUtilTest {

    @Test
    void isValidDate() {
        assertTrue(TradeDateUtil.isValidDate("20160101"));
        assertFalse(TradeDateUtil.isValidDate("2016010101"));
    }
}