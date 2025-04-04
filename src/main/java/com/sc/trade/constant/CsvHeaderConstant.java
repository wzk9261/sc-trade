package com.sc.trade.constant;

public class CsvHeaderConstant {

    public static final String DATE = "date";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String CURRENCY = "currency";
    public static final String PRICE = "price";
    public static final String[] TRADE_HEADERS = new String[]{DATE, PRODUCT_ID, CURRENCY, PRICE};
    public static final String[] ENRICHED_HEADERS = new String[]{DATE, PRODUCT_NAME, CURRENCY, PRICE};
}
