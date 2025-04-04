package com.sc.trade.controller;

import cn.hutool.core.util.CharsetUtil;
import com.sc.trade.service.EnrichmentService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnrichControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnrichmentService enrichmentService;

    @InjectMocks
    private EnrichController enrichController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(enrichController).build();
    }

    @Test
    public void enrichData_FileIsEmpty_ProcessFileAndSetResponse() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", new byte[0]);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new DelegatingServletOutputStream(outputStream);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        assertDoesNotThrow(() -> mockMvc.perform(multipart("/api/v1/enrich")
                        .file(file))
                .andExpect(status().isOk()));
    }

}
