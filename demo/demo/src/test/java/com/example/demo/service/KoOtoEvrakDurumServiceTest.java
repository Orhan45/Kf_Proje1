package com.example.demo.service;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.repository.KoOtoEvrakDurumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KoOtoEvrakDurumServiceTest {

    @InjectMocks
    private KoOtoEvrakDurumService service;

    @Mock
    private KoOtoEvrakDurumRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // getKoOtoEvrakDurumByKrediNumarasi metodu için testler
    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_shouldReturnList_whenRecordsExist() {
        // Hazırlık
        String krediNumarasi = "12345";
        List<KoOtoEvrakDurum> mockList = Collections.singletonList(new KoOtoEvrakDurum());
        when(repository.findAllByKrediNumarasi(krediNumarasi)).thenReturn(mockList);

        // Çalıştırma
        List<KoOtoEvrakDurum> result = service.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);

        // Kontrol
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAllByKrediNumarasi(krediNumarasi);
    }

    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_shouldReturnEmptyList_whenNoRecordsExist() {
        // Hazırlık
        String krediNumarasi = "12345";
        when(repository.findAllByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        // Çalıştırma
        List<KoOtoEvrakDurum> result = service.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);

        // Kontrol
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAllByKrediNumarasi(krediNumarasi);
    }

    // updateKoOtoEvrakDurumByKrediAndEvrakKodu metodu için testler
    @Test
    void updateKoOtoEvrakDurumByKrediAndEvrakKodu_shouldUpdateAndReturnEntity_whenRecordExists() {
        // Hazırlık
        String krediNumarasi = "12345";
        String evrakKodu = "E1";
        KoOtoEvrakDurum existingEntity = new KoOtoEvrakDurum();
        existingEntity.setDurum(0);
        KoOtoEvrakDurum updateData = new KoOtoEvrakDurum();
        updateData.setDurum(2);

        when(repository.findByKrediNumarasiAndEvrakKodu(krediNumarasi, evrakKodu))
                .thenReturn(Optional.of(existingEntity));
        when(repository.save(any(KoOtoEvrakDurum.class))).thenReturn(existingEntity);

        // Çalıştırma
        KoOtoEvrakDurum updatedEntity = service.updateKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);

        // Kontrol
        assertNotNull(updatedEntity);
        assertEquals(2, updatedEntity.getDurum()); // Durumun güncellendiğini kontrol et
        verify(repository, times(1)).findByKrediNumarasiAndEvrakKodu(krediNumarasi, evrakKodu);
        verify(repository, times(1)).save(existingEntity);
    }

    @Test
    void updateKoOtoEvrakDurumByKrediAndEvrakKodu_shouldReturnNull_whenRecordDoesNotExist() {
        // Hazırlık
        String krediNumarasi = "12345";
        String evrakKodu = "E1";
        KoOtoEvrakDurum updateData = new KoOtoEvrakDurum();
        updateData.setDurum(2);

        when(repository.findByKrediNumarasiAndEvrakKodu(krediNumarasi, evrakKodu))
                .thenReturn(Optional.empty());

        // Çalıştırma
        KoOtoEvrakDurum updatedEntity = service.updateKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);

        // Kontrol
        assertNull(updatedEntity);
        verify(repository, times(1)).findByKrediNumarasiAndEvrakKodu(krediNumarasi, evrakKodu);
        verify(repository, never()).save(any(KoOtoEvrakDurum.class)); // Kayıt bulunamadığı için save metodu çağrılmamalı
    }
}