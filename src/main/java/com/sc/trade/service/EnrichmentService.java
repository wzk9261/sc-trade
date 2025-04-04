package com.sc.trade.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.csv.*;
import com.sc.trade.constant.CsvHeaderConstant;
import com.sc.trade.repo.ProductRepository;
import com.sc.trade.util.TradeDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentService {
    private final ProductRepository productRepo;

    public void process(InputStream input, OutputStream output) throws IOException {
        CsvReadConfig readConfig = CsvReadConfig.defaultConfig()
                .setHeaderLineNo(0)
                .setTrimField(true);
        CsvWriteConfig writeConfig = CsvWriteConfig.defaultConfig();

        // try-with-resources
        try (CsvReader reader = CsvUtil.getReader(IoUtil.getUtf8Reader(input), readConfig);
             CsvWriter writer = CsvUtil.getWriter(IoUtil.getUtf8Writer(output), writeConfig)) {
            writer.writeHeaderLine(CsvHeaderConstant.ENRICHED_HEADERS);
            reader.stream().forEach(row -> {
                processRow(row, writer);
            });
        }
    }

    private void processRow(CsvRow row, CsvWriter writer) {
        String date = row.getByName(CsvHeaderConstant.DATE);
        if (!TradeDateUtil.isValidDate(date)) {
            log.error("Invalid date format, discard row: {}", row.getRawList());
            return;
        }

        Integer productId = Integer.valueOf(row.getByName("productId"));
        String productName = productRepo.getProductName(productId);
        if (productName.contains("Missing")) {
            log.warn("Missing product mapping for ID: {}", productId);
        }

        writer.writeLine(
                date,
                productName,
                row.getByName(CsvHeaderConstant.CURRENCY),
                row.getByName(CsvHeaderConstant.PRICE)
        );
    }
}
