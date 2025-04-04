package com.sc.trade.controller;

import cn.hutool.core.util.CharsetUtil;
import com.sc.trade.service.EnrichmentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EnrichController {
    private final EnrichmentService enrichmentService;

    @PostMapping(value = "/enrich",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void enrichData(@RequestPart MultipartFile file,
                           HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=enriched.csv");
        enrichmentService.process(file.getInputStream(), response.getOutputStream());
        response.flushBuffer();
    }
}
