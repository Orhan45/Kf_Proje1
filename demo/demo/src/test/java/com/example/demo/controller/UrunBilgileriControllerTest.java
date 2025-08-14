package com.example.demo.controller;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.service.UrunBilgileriService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrunBilgileriControllerTest {

    @InjectMocks
    private UrunBilgileriController controller;

    @Mock
    private UrunBilgileriService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // --- getUrunlerByKrediNumarasi Metodu İçin Testler ---

    @Test
    void testGetUrunlerByKrediNumarasi_shouldReturnNotFound_whenNoRecordsExist() {
        String krediNumarasi = "kredi1";
        when(service.getUrunlerByKrediNumarasi(krediNumarasi))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<UrunBilgileri>> response = controller.getUrunlerByKrediNumarasi(krediNumarasi);

        assertEquals(404, response.getStatusCode().value());
        verify(service).getUrunlerByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testGetUrunlerByKrediNumarasi_shouldReturnOk_whenRecordsExist() {
        String krediNumarasi = "kredi1";
        List<UrunBilgileri> mockList = Arrays.asList(new UrunBilgileri(), new UrunBilgileri());
        when(service.getUrunlerByKrediNumarasi(krediNumarasi))
                .thenReturn(mockList);

        ResponseEntity<List<UrunBilgileri>> response = controller.getUrunlerByKrediNumarasi(krediNumarasi);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockList, response.getBody());
        verify(service).getUrunlerByKrediNumarasi(krediNumarasi);
    }

    // --- getSiralarByKrediNumarasi Metodu İçin Testler ---

    @Test
    void testGetSiralarByKrediNumarasi_shouldReturnNotFound_whenNoSiralarExist() {
        String krediNumarasi = "kredi1";
        when(service.getSiralarByKrediNumarasi(krediNumarasi))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<Integer>> response = controller.getSiralarByKrediNumarasi(krediNumarasi);

        assertEquals(404, response.getStatusCode().value());
        verify(service).getSiralarByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testGetSiralarByKrediNumarasi_shouldReturnOk_whenSiralarExist() {
        String krediNumarasi = "kredi1";
        List<Integer> mockList = Arrays.asList(1, 2, 3);
        when(service.getSiralarByKrediNumarasi(krediNumarasi))
                .thenReturn(mockList);

        ResponseEntity<List<Integer>> response = controller.getSiralarByKrediNumarasi(krediNumarasi);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockList, response.getBody());
        verify(service).getSiralarByKrediNumarasi(krediNumarasi);
    }

    // --- updateUrunBilgileri Metodu İçin Testler ---

    @Test
    void testUpdateUrunBilgileri_shouldReturnNotFound_whenRecordDoesNotExist() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        UrunBilgileri updateData = new UrunBilgileri();

        when(service.updateUrunBilgileri(eq(krediNumarasi), eq(sira), any(UrunBilgileri.class)))
                .thenReturn(null);

        ResponseEntity<UrunBilgileri> response = controller.updateUrunBilgileri(krediNumarasi, sira, updateData);

        assertEquals(404, response.getStatusCode().value());
        verify(service).updateUrunBilgileri(eq(krediNumarasi), eq(sira), any(UrunBilgileri.class));
    }

    @Test
    void testUpdateUrunBilgileri_shouldReturnOk_whenRecordIsUpdated() {
        String krediNumarasi = "kredi1";
        Integer sira = 1;
        UrunBilgileri updateData = new UrunBilgileri();
        UrunBilgileri updatedEntity = new UrunBilgileri();

        when(service.updateUrunBilgileri(eq(krediNumarasi), eq(sira), any(UrunBilgileri.class)))
                .thenReturn(updatedEntity);

        ResponseEntity<UrunBilgileri> response = controller.updateUrunBilgileri(krediNumarasi, sira, updateData);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedEntity, response.getBody());
        verify(service).updateUrunBilgileri(eq(krediNumarasi), eq(sira), any(UrunBilgileri.class));
    }

    // --- deleteAndReinsertEgmStateInformationByKrediNumarasi Metodu İçin Testler ---

    @Test
    void testDeleteAndReinsertEgmStateInformationByKrediNumarasi_shouldReturnNotFound_whenRecordsDoNotExist() {
        String krediNumarasi = "kredi1";
        String responseMessage = "Kredi numarası: kredi1 ile eşleşen kayıt bulunamadı.";

        when(service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), any()))
                .thenReturn(responseMessage);

        ResponseEntity<String> response = controller.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, null);

        assertEquals(404, response.getStatusCode().value());
        assertEquals(responseMessage, response.getBody());
        verify(service).deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), any());
    }

    @Test
    void testDeleteAndReinsertEgmStateInformationByKrediNumarasi_shouldReturnOk_whenRecordsExist() {
        String krediNumarasi = "kredi1";
        String responseMessage = "Kredi numarası: kredi1 için EgmStateInformation başarıyla güncellendi.";

        when(service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), any()))
                .thenReturn(responseMessage);

        ResponseEntity<String> response = controller.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseMessage, response.getBody());
        verify(service).deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(eq(krediNumarasi), any());
    }
}