package com.example.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.Timestamp;
import java.time.LocalDate;
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
        // Arrange
        String phoneNumber = "5551234567";
        LocalDate startDate = LocalDate.of(2025, 8, 1);
        LocalDate endDate = LocalDate.of(2025, 8, 11);

        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"5551234567", "Mesaj 1", Timestamp.valueOf("2025-08-05 10:00:00"), "KOD1", "ServisA", "SMS_GONDER"},
                new Object[]{"5551234567", "Mesaj 2", Timestamp.valueOf("2025-08-06 11:00:00"), "KOD2", "ServisB", "SMS_GONDER_ARA"}
        );

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("phoneNumber"), eq(phoneNumber))).thenReturn(query);
        when(query.setParameter(eq("startDate"), any())).thenReturn(query);
        when(query.setParameter(eq("endDate"), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(mockResults);

        // Act
        List<Map<String, Object>> result = smsGonderService.getSmsRecordsByPhoneAndDate(phoneNumber, startDate, endDate);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Mesaj 1", result.get(0).get("messageBody"));
        assertEquals("SMS_GONDER", result.get(0).get("kaynakTablo"));
    }

    @Test
    void testGetSmsRecordsByPhoneAndDate_withoutFilters() {
        // Arrange
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // Act
        List<Map<String, Object>> result = smsGonderService.getSmsRecordsByPhoneAndDate(null, null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}