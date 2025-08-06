// File: src/test/java/com/example/demo/service/KoOtoEvrakDurumServiceTest.java

package com.example.demo.service;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.repository.KoOtoEvrakDurumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KoOtoEvrakDurumServiceTest {

    @Mock
    private KoOtoEvrakDurumRepository repository;

    @InjectMocks
    private KoOtoEvrakDurumService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_whenRepositoryReturnsList() {
        KoOtoEvrakDurum e1 = new KoOtoEvrakDurum();
        KoOtoEvrakDurum e2 = new KoOtoEvrakDurum();
        when(repository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<KoOtoEvrakDurum> result = service.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findAll_whenRepositoryReturnsEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<KoOtoEvrakDurum> result = service.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void updateKoOtoEvrakDurumByKrediAndEvrakKodu_whenEntityExists() {
        KoOtoEvrakDurum existing = new KoOtoEvrakDurum();
        existing.setDurum(0);
        KoOtoEvrakDurum updateData = new KoOtoEvrakDurum();
        updateData.setDurum(1);

        when(repository.findByKrediNumarasiAndEvrakKodu("123", "E1"))
                .thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        KoOtoEvrakDurum result = service.updateKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1", updateData);
        assertNotNull(result);
        assertEquals(1, result.getDurum());
        verify(repository, times(1)).findByKrediNumarasiAndEvrakKodu("123", "E1");
        verify(repository, times(1)).save(existing);
    }

    @Test
    void updateKoOtoEvrakDurumByKrediAndEvrakKodu_whenEntityNotFound() {
        when(repository.findByKrediNumarasiAndEvrakKodu("123", "E1"))
                .thenReturn(Optional.empty());

        KoOtoEvrakDurum result = service.updateKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1", new KoOtoEvrakDurum());
        assertNull(result);
        verify(repository, times(1)).findByKrediNumarasiAndEvrakKodu("123", "E1");
        verify(repository, never()).save(any());
    }

    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_whenRepositoryReturnsList() {
        KoOtoEvrakDurum entity = new KoOtoEvrakDurum();
        when(repository.findAllByKrediNumarasi("123")).thenReturn(Arrays.asList(entity));

        List<KoOtoEvrakDurum> result = service.getKoOtoEvrakDurumByKrediNumarasi("123");
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByKrediNumarasi("123");
    }

    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_whenRepositoryReturnsEmpty() {
        when(repository.findAllByKrediNumarasi("123")).thenReturn(Collections.emptyList());

        List<KoOtoEvrakDurum> result = service.getKoOtoEvrakDurumByKrediNumarasi("123");
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAllByKrediNumarasi("123");
    }
}