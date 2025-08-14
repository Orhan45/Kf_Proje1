package com.example.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmsGonderServiceTest {

    @InjectMocks
    private SmsGonderService smsGonderService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSmsRecordsByPhoneAndDate_withFilters() {
        String phoneNumber = "5551234567";
        String smsKod = "KOD1";
        LocalDateTime startDate = LocalDateTime.of(2025, 8, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 8, 11, 23, 59);

        List<Object[]> mockResults = Arrays.asList(
                new Object[]{ "5551234567", "Mesaj 1", Timestamp.valueOf("2025-08-05 10:00:00"), "KOD1", "ServisA", "SMS_GONDER" },
                new Object[]{ "5551234567", "Mesaj 2", Timestamp.valueOf("2025-08-06 11:00:00"), "KOD1", "ServisB", "SMS_GONDER_ARA" }
        );

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        // Telefon numaraları dinamik parametreler ile gönderildiği için setParameter ayarlarını denetlemeye gerek kalmadan sonucunu döndürüyoruz.
        when(query.setParameter(eq("smsKod"), eq(smsKod))).thenReturn(query);
        when(query.setParameter(eq("startDate"), any())).thenReturn(query);
        when(query.setParameter(eq("endDate"), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);

        List<Map<String, Object>> result = smsGonderService.getSmsRecordsByPhoneAndDate(phoneNumber, smsKod, startDate, endDate);

        assertEquals(2, result.size());
        assertEquals("Mesaj 1", result.get(0).get("messageBody"));
        assertEquals("SMS_GONDER", result.get(0).get("kaynakTablo"));
    }

    @Test
    void testGetSmsRecordsByPhoneAndDate_withoutFilters() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        List<Map<String, Object>> result = smsGonderService.getSmsRecordsByPhoneAndDate(null, null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}