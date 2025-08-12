// dosya: demo/demo/src/test/java/com/example/demo/controller/KoGunKapamaControllerTest.java
package com.example.demo.controller;

import com.example.demo.service.KoGunKapamaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class KoGunKapamaControllerTest {

    @InjectMocks
    private KoGunKapamaController controller;

    @Mock
    private KoGunKapamaService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessKoGunKapamaByDate() {
        LocalDate date = LocalDate.of(2023, 10, 30);
        ResponseEntity<Void> response = controller.processKoGunKapamaByDate(date);
        verify(service).processRecordsForDate(date);
        assertEquals(200, response.getStatusCode().value());
    }
}