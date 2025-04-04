package com.sc.trade.repo;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.sc.trade.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository {

    @Value("${product-csv-filename:product.csv}")
    private String productCsvFilename;

    private final Map<Integer, String> productMap = MapUtil.newHashMap();

    @PostConstruct
    public void loadProductMap() {
        CsvReader reader = CsvUtil.getReader();
        List<Product> products = reader.read(
                ResourceUtil.getUtf8Reader(productCsvFilename), Product.class);
        products.forEach(product -> {
            productMap.put(product.getProductId(), product.getProductName());
        });

    }

    public String getProductName(Integer productId) {
        return productMap.getOrDefault(productId, "Missing Product Name");
    }
}
