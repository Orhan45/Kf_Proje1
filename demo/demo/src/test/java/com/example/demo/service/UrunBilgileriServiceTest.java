package com.example.demo.service;

import com.example.demo.entity.EgmStateInformation;
import com.example.demo.entity.UrunBilgileri;
import com.example.demo.entity.UrunBilgileriId;
import com.example.demo.repository.EgmStateInformationRepository;
import com.example.demo.repository.UrunBilgileriRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrunBilgileriServiceTest {

    @InjectMocks
    private UrunBilgileriService service;

    @Mock
    private UrunBilgileriRepository urunBilgileriRepository;

    @Mock
    private EgmStateInformationRepository egmStateInformationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- getUrunlerByKrediNumarasi Metodu İçin Testler ---

    @Test
    void getUrunlerByKrediNumarasi_shouldReturnList_whenRecordsExist() {
        String krediNumarasi = "kredi1";
        List<UrunBilgileri> mockList = Arrays.asList(new UrunBilgileri(), new UrunBilgileri());
        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(mockList);

        List<UrunBilgileri> result = service.getUrunlerByKrediNumarasi(krediNumarasi);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
    }

    @Test
    void getUrunlerByKrediNumarasi_shouldReturnEmptyList_whenNoRecordsExist() {
        String krediNumarasi = "kredi1";
        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        List<UrunBilgileri> result = service.getUrunlerByKrediNumarasi(krediNumarasi);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
    }

    // --- updateUrunBilgileri Metodu İçin Testler ---

    @Test
    void updateUrunBilgileri_shouldUpdateAndReturnEntity_whenRecordExists() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        UrunBilgileri existingEntity = new UrunBilgileri();
        existingEntity.setRehinDurum(0);
        UrunBilgileri updateData = new UrunBilgileri();
        updateData.setRehinDurum(1);

        when(urunBilgileriRepository.findById(any(UrunBilgileriId.class))).thenReturn(Optional.of(existingEntity));
        when(urunBilgileriRepository.save(any(UrunBilgileri.class))).thenReturn(existingEntity);

        UrunBilgileri updated = service.updateUrunBilgileri(krediNumarasi, sira, updateData);

        assertNotNull(updated);
        assertEquals(1, updated.getRehinDurum());
        verify(urunBilgileriRepository).findById(any(UrunBilgileriId.class));
        verify(urunBilgileriRepository).save(any(UrunBilgileri.class));
    }

    @Test
    void updateUrunBilgileri_shouldReturnNull_whenRecordDoesNotExist() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        UrunBilgileri updateData = new UrunBilgileri();

        when(urunBilgileriRepository.findById(any(UrunBilgileriId.class))).thenReturn(Optional.empty());

        UrunBilgileri updated = service.updateUrunBilgileri(krediNumarasi, sira, updateData);

        assertNull(updated);
        verify(urunBilgileriRepository).findById(any(UrunBilgileriId.class));
        verify(urunBilgileriRepository, never()).save(any(UrunBilgileri.class));
    }

    // --- getSiralarByKrediNumarasi Metodu İçin Testler ---

    @Test
    void getSiralarByKrediNumarasi_shouldReturnDistinctSortedSiralar() {
        String krediNumarasi = "kredi1";
        UrunBilgileri urun1 = new UrunBilgileri(); urun1.setSira(3);
        UrunBilgileri urun2 = new UrunBilgileri(); urun2.setSira(1);
        UrunBilgileri urun3 = new UrunBilgileri(); urun3.setSira(2);
        UrunBilgileri urun4 = new UrunBilgileri(); urun4.setSira(1); // tekrar eden
        List<UrunBilgileri> mockList = Arrays.asList(urun1, urun2, urun3, urun4);

        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(mockList);

        List<Integer> result = service.getSiralarByKrediNumarasi(krediNumarasi);

        assertEquals(Arrays.asList(1, 2, 3), result);
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
    }

    @Test
    void getSiralarByKrediNumarasi_shouldReturnEmptyList_whenNoUrunlerExist() {
        String krediNumarasi = "kredi1";
        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        List<Integer> result = service.getSiralarByKrediNumarasi(krediNumarasi);

        assertTrue(result.isEmpty());
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
    }

    // --- processEgmStateInformationForProductLine Metodu İçin Testler ---

    @Test
    void processEgmStateInformationForProductLine_shouldProcessSuccessfully_whenRecordsExist() {
        Long productLineId = 1L;
        List<EgmStateInformation> existingRecords = Collections.singletonList(new EgmStateInformation());
        when(egmStateInformationRepository.findByProductLineId(productLineId)).thenReturn(existingRecords);

        boolean result = service.processEgmStateInformationForProductLine(productLineId);

        assertTrue(result);
        verify(egmStateInformationRepository).findByProductLineId(productLineId);
        verify(egmStateInformationRepository).deleteByProductLineId(productLineId);
        verify(egmStateInformationRepository).save(any(EgmStateInformation.class));
    }

    @Test
    void processEgmStateInformationForProductLine_shouldReturnFalse_whenNoRecordsExist() {
        Long productLineId = 1L;
        when(egmStateInformationRepository.findByProductLineId(productLineId)).thenReturn(Collections.emptyList());

        boolean result = service.processEgmStateInformationForProductLine(productLineId);

        assertFalse(result);
        verify(egmStateInformationRepository).findByProductLineId(productLineId);
        verify(egmStateInformationRepository, never()).deleteByProductLineId(anyLong());
        verify(egmStateInformationRepository, never()).save(any(EgmStateInformation.class));
    }

    // --- deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira Metodu İçin Testler ---

    @Test
    void deleteAndReinsert_shouldReturnSuccess_whenSiraProvidedAndProcessedSuccessfully() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        UrunBilgileri urun = new UrunBilgileri();
        urun.setProductLineId(1L);
        when(urunBilgileriRepository.findByKrediNumarasiAndSira(krediNumarasi, sira)).thenReturn(Optional.of(urun));
        when(egmStateInformationRepository.findByProductLineId(1L)).thenReturn(Collections.singletonList(new EgmStateInformation()));

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);

        assertTrue(result.contains("başarıyla güncellendi"));
        verify(urunBilgileriRepository).findByKrediNumarasiAndSira(krediNumarasi, sira);
        verify(egmStateInformationRepository).deleteByProductLineId(1L);
        verify(egmStateInformationRepository).save(any(EgmStateInformation.class));
    }

    @Test
    void deleteAndReinsert_shouldReturnNotFound_whenSiraProvidedAndRecordDoesNotExist() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        when(urunBilgileriRepository.findByKrediNumarasiAndSira(krediNumarasi, sira)).thenReturn(Optional.empty());

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);

        assertTrue(result.contains("bulunamadı"));
        verify(urunBilgileriRepository).findByKrediNumarasiAndSira(krediNumarasi, sira);
        verify(egmStateInformationRepository, never()).deleteByProductLineId(anyLong());
    }

    @Test
    void deleteAndReinsert_shouldReturnPartialFailure_whenSiraProvidedAndProcessingFails() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        UrunBilgileri urun = new UrunBilgileri();
        urun.setProductLineId(1L);
        when(urunBilgileriRepository.findByKrediNumarasiAndSira(krediNumarasi, sira)).thenReturn(Optional.of(urun));
        when(egmStateInformationRepository.findByProductLineId(1L)).thenReturn(Collections.emptyList()); // processing fail

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);

        assertTrue(result.contains("bazı işlemler tamamlanamadı"));
        verify(urunBilgileriRepository).findByKrediNumarasiAndSira(krediNumarasi, sira);
        verify(egmStateInformationRepository, never()).deleteByProductLineId(anyLong());
        verify(egmStateInformationRepository, never()).save(any(EgmStateInformation.class));
    }

    @Test
    void deleteAndReinsert_shouldReturnSuccess_whenSiraIsNotProvidedAndAllProcessed() {
        String krediNumarasi = "kredi1";
        UrunBilgileri urun1 = new UrunBilgileri(); urun1.setProductLineId(1L);
        UrunBilgileri urun2 = new UrunBilgileri(); urun2.setProductLineId(2L);
        List<UrunBilgileri> urunler = Arrays.asList(urun1, urun2);

        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(urunler);
        when(egmStateInformationRepository.findByProductLineId(anyLong())).thenReturn(Collections.singletonList(new EgmStateInformation()));

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, null);

        assertTrue(result.contains("başarıyla güncellendi"));
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
        verify(egmStateInformationRepository, times(2)).deleteByProductLineId(anyLong());
        verify(egmStateInformationRepository, times(2)).save(any(EgmStateInformation.class));
    }

    @Test
    void deleteAndReinsert_shouldReturnNotFound_whenSiraIsNotProvidedAndNoRecordsExist() {
        String krediNumarasi = "kredi1";
        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(Collections.emptyList());

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, null);

        assertTrue(result.contains("bulunamadı"));
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
        verify(egmStateInformationRepository, never()).deleteByProductLineId(anyLong());
    }

    @Test
    void deleteAndReinsert_shouldReturnPartialFailure_whenSiraIsNotProvidedAndSomeProcessingFails() {
        String krediNumarasi = "kredi1";
        UrunBilgileri urun1 = new UrunBilgileri(); urun1.setProductLineId(1L);
        UrunBilgileri urun2 = new UrunBilgileri(); urun2.setProductLineId(2L);
        List<UrunBilgileri> urunler = Arrays.asList(urun1, urun2);

        when(urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)).thenReturn(urunler);
        // İlk ürün için kayıt var, ikincisi için yok.
        when(egmStateInformationRepository.findByProductLineId(1L)).thenReturn(Collections.singletonList(new EgmStateInformation()));
        when(egmStateInformationRepository.findByProductLineId(2L)).thenReturn(Collections.emptyList());

        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, null);

        assertTrue(result.contains("bazı işlemler tamamlanamadı"));
        verify(urunBilgileriRepository).findByKrediNumarasi(krediNumarasi);
        verify(egmStateInformationRepository, times(1)).deleteByProductLineId(1L);
        verify(egmStateInformationRepository, times(1)).save(any(EgmStateInformation.class));
    }
}