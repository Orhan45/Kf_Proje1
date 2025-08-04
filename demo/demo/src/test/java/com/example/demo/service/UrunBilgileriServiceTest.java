package com.example.demo.service;

import com.example.demo.entity.EgmStateInformation;
import com.example.demo.entity.UrunBilgileri;
import com.example.demo.entity.UrunBilgileriId;
import com.example.demo.repository.EgmStateInformationRepository;
import com.example.demo.repository.UrunBilgileriRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrunBilgileriServiceTest {

    @Mock
    private UrunBilgileriRepository urunBilgileriRepository;

    @Mock
    private EgmStateInformationRepository egmStateInformationRepository;

    @InjectMocks
    private UrunBilgileriService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUrunler_shouldReturnList() {
        when(urunBilgileriRepository.findAll()).thenReturn(Arrays.asList(new UrunBilgileri(), new UrunBilgileri()));

        var result = service.getAllUrunler();

        assertEquals(2, result.size());
        verify(urunBilgileriRepository).findAll();
    }

    @Test
    void getUrunlerByKrediNumarasi_shouldReturnList() {
        when(urunBilgileriRepository.findByKrediNumarasi("123")).thenReturn(List.of(new UrunBilgileri()));

        var result = service.getUrunlerByKrediNumarasi("123");

        assertEquals(1, result.size());
        verify(urunBilgileriRepository).findByKrediNumarasi("123");
    }

    @Test
    void updateUrunBilgileri_whenExists_shouldUpdateRehinDurum() {
        var id = new UrunBilgileriId("123", 1);
        UrunBilgileri existing = new UrunBilgileri();
        existing.setRehinDurum(3);
        UrunBilgileri update = new UrunBilgileri();
        update.setRehinDurum(5);

        when(urunBilgileriRepository.findById(id)).thenReturn(Optional.of(existing));
        when(urunBilgileriRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.updateUrunBilgileri("123", 1, update);

        assertNotNull(result);
        assertEquals(5, result.getRehinDurum());
        verify(urunBilgileriRepository).findById(id);
        verify(urunBilgileriRepository).save(existing);
    }

    @Test
    void updateUrunBilgileri_whenNotExists_shouldReturnNull() {
        var id = new UrunBilgileriId("123", 1);
        when(urunBilgileriRepository.findById(id)).thenReturn(Optional.empty());

        var result = service.updateUrunBilgileri("123", 1, new UrunBilgileri());

        assertNull(result);
        verify(urunBilgileriRepository).findById(id);
        verify(urunBilgileriRepository, never()).save(any());
    }

    @Test
    void processEgmStateInformationForProductLine_whenNoRecords_shouldReturnFalse() {
        when(egmStateInformationRepository.findByProductLineId(1L)).thenReturn(Collections.emptyList());

        boolean result = service.processEgmStateInformationForProductLine(1L);

        assertFalse(result);
        verify(egmStateInformationRepository).findByProductLineId(1L);
        verify(egmStateInformationRepository, never()).deleteByProductLineId(anyLong());
    }

    @Test
    void processEgmStateInformationForProductLine_whenRecordsExist_shouldDeleteAndSaveNew() {
        when(egmStateInformationRepository.findByProductLineId(1L)).thenReturn(List.of(new EgmStateInformation()));

        boolean result = service.processEgmStateInformationForProductLine(1L);

        assertTrue(result);
        verify(egmStateInformationRepository).deleteByProductLineId(1L);
        verify(egmStateInformationRepository).save(any(EgmStateInformation.class));
    }

    @Test
    void deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira_whenNoRecords_shouldReturnNotFound() {
        when(urunBilgileriRepository.findByKrediNumarasi("123")).thenReturn(Collections.emptyList());

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", null);

        assertTrue(result.contains("bulunamadı"));
        verify(urunBilgileriRepository).findByKrediNumarasi("123");
        verify(egmStateInformationRepository, never()).deleteByProductLineId(anyLong());
    }

    @Test
    void deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira_whenSiraProvided_shouldProcess() {
        UrunBilgileri urun = new UrunBilgileri();
        urun.setProductLineId(10L);

        when(urunBilgileriRepository.findByKrediNumarasiAndSira("123", 1)).thenReturn(Optional.of(urun));
        when(egmStateInformationRepository.findByProductLineId(10L)).thenReturn(List.of(new EgmStateInformation()));

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", 1);

        assertTrue(result.contains("başarıyla güncellendi"));
        verify(urunBilgileriRepository).findByKrediNumarasiAndSira("123", 1);
        verify(egmStateInformationRepository).deleteByProductLineId(10L);
        verify(egmStateInformationRepository).save(any());
    }

    @Test
    void getSiralarByKrediNumarasi_shouldReturnDistinctSortedList() {
        UrunBilgileri u1 = new UrunBilgileri();
        u1.setSira(3);
        UrunBilgileri u2 = new UrunBilgileri();
        u2.setSira(1);
        UrunBilgileri u3 = new UrunBilgileri();
        u3.setSira(2);
        UrunBilgileri u4 = new UrunBilgileri();
        u4.setSira(2);

        when(urunBilgileriRepository.findByKrediNumarasi("123")).thenReturn(List.of(u1, u2, u3, u4));

        var siralar = service.getSiralarByKrediNumarasi("123");

        assertEquals(List.of(1, 2, 3), siralar);
        verify(urunBilgileriRepository).findByKrediNumarasi("123");
    }
}
