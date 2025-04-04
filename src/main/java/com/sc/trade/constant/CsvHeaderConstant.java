package com.sc.trade.constant;

public interface CsvHeaderConstant {

    String DATE = "date";
    String PRODUCT_ID = "productId";
    String PRODUCT_NAME = "productName";
    String CURRENCY = "currency";
    String PRICE = "price";
    String[] PRODUCT_HEADERS = new String[]{PRODUCT_ID, PRODUCT_NAME};
    String[] TRADE_HEADERS = new String[]{DATE, PRODUCT_ID, CURRENCY, PRICE};
    String[] ENRICHED_HEADERS = new String[]{DATE, PRODUCT_NAME, CURRENCY, PRICE};
}
