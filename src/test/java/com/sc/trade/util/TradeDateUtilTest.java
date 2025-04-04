package com.sc.trade.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TradeDateUtilTest {

    @Test
    void isValidDate_ValidDate_ReturnsTrue() {
        assertTrue(TradeDateUtil.isValidDate("20160101"));
    }

    @Test
    void isValidDate_InvalidDate_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("2016010101"));
    }

    @Test
    void isValidDate_EmptyString_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate(""));
    }

    @Test
    void isValidDate_NullInput_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate(null));
    }

    @Test
    void isValidDate_WrongFormat_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("2016-01-01"));
    }

    @Test
    void isValidDate_NonNumericString_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("abcd"));
    }

    @Test
    void isValidDate_StringLengthNot8_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("201601"));
    }

    @Test
    void isValidDate_MonthLessThan1_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20160001"));
    }

    @Test
    void isValidDate_MonthGreaterThan12_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20161301"));
    }

    @Test
    void isValidDate_DayLessThan1_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20160100"));
    }

    @Test
    void isValidDate_DayGreaterThan31_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20160132"));
    }

    @Test
    void isValidDate_DayGreaterThan30ForApril_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20160431"));
    }

    @Test
    void isValidDate_DayGreaterThan29ForFebruaryInLeapYear_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20160230"));
    }

    @Test
    void isValidDate_DayGreaterThan28ForFebruaryInNonLeapYear_ReturnsFalse() {
        assertFalse(TradeDateUtil.isValidDate("20150229"));
    }

}
