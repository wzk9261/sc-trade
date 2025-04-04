package com.sc.trade.service;


import com.sc.trade.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EnrichmentServiceTest {

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private EnrichmentService enrichmentService;

    @Test
    public void process_ValidData_ShouldWriteCorrectly() throws IOException {
        String input = "date,productId,price\n20230101,1,100";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        when(productRepo.getProductName(1)).thenReturn("Product1");

        enrichmentService.process(inputStream, outputStream);

        String expectedOutput = "date,productName,currency,price\r\n20230101,Product1,,100";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void process_InvalidDate_ShouldLogErrorAndSkipRow() throws IOException {
        String input = "date,productId,price\n20231301,1,100";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        enrichmentService.process(inputStream, outputStream);

        String expectedOutput = "date,productName,currency,price";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void process_MissingProduct_ShouldLogWarning() throws IOException {
        String input = "date,productId,price\n20230101,2,100";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        when(productRepo.getProductName(2)).thenReturn("Missing Product");

        enrichmentService.process(inputStream, outputStream);

        String expectedOutput = "date,productName,currency,price\r\n20230101,Missing Product,,100";
        assertEquals(expectedOutput, outputStream.toString());
    }
}
