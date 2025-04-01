package com.sc.trade.service;

import cn.hutool.core.text.csv.*;
import com.sc.trade.repo.ProductRepository;
import com.sc.trade.util.TradeDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentService {
    private final ProductRepository productRepo;

    public void process(InputStream input, OutputStream output) throws IOException {
        CsvReadConfig readConfig = CsvReadConfig.defaultConfig()
                .setHeaderLineNo(0)
                .setTrimField(true);

        CsvWriteConfig writeConfig = CsvWriteConfig.defaultConfig()
                .setAlwaysDelimitText(true);

        try (CsvReader reader = CsvUtil.getReader(
                new InputStreamReader(input), readConfig);
             CsvWriter writer = CsvUtil.getWriter(
                     new OutputStreamWriter(output), writeConfig)) {

            writer.writeLine("date", "productName", "currency", "price");

            reader.stream().forEach(row -> {
                if (!processRow(row, writer)) {
                    log.warn("Discarded invalid row: {}", row.getRawList());
                }
            });
        }
    }

    private boolean processRow(CsvRow row, CsvWriter writer) {
        // 日期验证
        String date = row.getByName("date");
        if (!TradeDateUtil.isValidDate(date)) {
            log.error("Invalid date format: {}", date);
            return false;
        }

        // 产品名称映射
        String productId = row.getByName("productId");
        String productName = productRepo.getProductName(productId);
        if (productName.contains("Missing")) {
            log.warn("Missing product mapping for ID: {}", productId);
        }

        // 写入结果
        writer.writeLine(
                date,
                productName,
                row.getByName("currency"),
                row.getByName("price")
        );
        return true;
    }
}
