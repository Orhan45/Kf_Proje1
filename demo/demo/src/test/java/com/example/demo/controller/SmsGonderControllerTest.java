package com.example.demo.controller;

import com.example.demo.service.SmsGonderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SmsGonderControllerTest {

    @InjectMocks
    private SmsGonderController controller;

    @Mock
    private SmsGonderService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSmsRecordsNoFilter() {
        when(service.getSmsRecordsByPhoneAndDate(null, null, null, null))
                .thenReturn(Collections.emptyList());
        List<Map<String, Object>> result = controller.getSmsRecords(null, null, null, null);
        verify(service).getSmsRecordsByPhoneAndDate(null, null, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSmsRecordsWithParams() {
        String phone = "1234567890";
        String smsKod = "TESTKOD";
        LocalDateTime startDate = LocalDateTime.of(2023, 10, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 10, 31, 23, 59);
        Map<String, Object> record = new HashMap<>();
        record.put("phoneNumber", phone);
        record.put("smsKod", smsKod);
        record.put("insertDate", LocalDateTime.now());
        when(service.getSmsRecordsByPhoneAndDate(phone, smsKod, startDate, endDate))
                .thenReturn(List.of(record));
        List<Map<String, Object>> result = controller.getSmsRecords(phone, smsKod, startDate, endDate);
        verify(service).getSmsRecordsByPhoneAndDate(phone, smsKod, startDate, endDate);
        assertEquals(1, result.size());
        assertEquals(phone, result.get(0).get("phoneNumber"));
        assertEquals(smsKod, result.get(0).get("smsKod"));
    }
}