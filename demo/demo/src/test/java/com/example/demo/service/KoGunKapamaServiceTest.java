// dosya: `demo/demo/src/test/java/com/example/demo/service/KoGunKapamaServiceTest.java`
package com.example.demo.service;

import com.example.demo.entity.KoGunKapama;
import com.example.demo.repository.KoGunKapamaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KoGunKapamaServiceTest {

    @Mock
    private KoGunKapamaRepository repository;

    @InjectMocks
    private KoGunKapamaService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void processRecordsForDate_whenRecordNotExists_shouldSaveRecordAndDeleteAfterDay() {
        LocalDate day = LocalDate.of(2023, 10, 30);
        when(repository.existsByTarih(day)).thenReturn(false);
        when(repository.save(any(KoGunKapama.class))).thenAnswer(invocation -> {
            KoGunKapama saved = invocation.getArgument(0);
            // simüle etmek amacıyla kayıt edildiğini varsayalım
            return saved;
        });

        service.processRecordsForDate(day);

        ArgumentCaptor<KoGunKapama> captor = ArgumentCaptor.forClass(KoGunKapama.class);
        verify(repository, times(1)).existsByTarih(day);
        verify(repository, times(1)).save(captor.capture());
        KoGunKapama savedRecord = captor.getValue();
        assertEquals(day, savedRecord.getTarih());
        assertEquals("FINANS", savedRecord.getUserName());
        assertNotNull(savedRecord.getProcessDate());
        // processDate saat bilgisi içermelidir
        assertTrue(savedRecord.getProcessDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(repository, times(1)).deleteRecordsAfterDay(day);
    }

    @Test
    void processRecordsForDate_whenRecordExists_shouldOnlyDeleteAfterDay() {
        LocalDate day = LocalDate.of(2023, 10, 30);
        when(repository.existsByTarih(day)).thenReturn(true);

        service.processRecordsForDate(day);

        verify(repository, times(1)).existsByTarih(day);
        verify(repository, never()).save(any(KoGunKapama.class));
        verify(repository, times(1)).deleteRecordsAfterDay(day);
    }
}