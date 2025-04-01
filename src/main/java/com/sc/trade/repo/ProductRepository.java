package com.sc.trade.repo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {
    private final ConcurrentHashMap<String, String> productMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadProductMap() {
        CsvReader reader = CsvUtil.getReader();
        CsvData data = reader.read(FileUtil.file("test.csv"));
        List<CsvRow> rows = data.getRows();
        for (CsvRow csvRow : rows) {
            String productId = csvRow.getByName("productId");
            String productName = csvRow.getByName("productName");
            productMap.put(productId, productName);
        }
    }

    public String getProductName(String productId) {
        return productMap.getOrDefault(productId, "Missing Product Name");
    }
}
