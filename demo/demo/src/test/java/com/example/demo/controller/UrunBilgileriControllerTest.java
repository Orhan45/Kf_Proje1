// dosya: demo/demo/src/test/java/com/example/demo/controller/UrunBilgileriControllerTest.java
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

public class UrunBilgileriControllerTest {

    @InjectMocks
    private UrunBilgileriController controller;

    @Mock
    private UrunBilgileriService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductionInformation() {
        List<UrunBilgileri> list = Arrays.asList(new UrunBilgileri(), new UrunBilgileri());
        when(service.getAllUrunler()).thenReturn(list);
        List<UrunBilgileri> result = controller.getProductionInformation();
        verify(service).getAllUrunler();
        assertEquals(2, result.size());
    }

    @Test
    void testGetUrunlerByKrediNumarasi_NotFound() {
        when(service.getUrunlerByKrediNumarasi("123")).thenReturn(Collections.emptyList());
        ResponseEntity<List<UrunBilgileri>> response = controller.getUrunlerByKrediNumarasi("123");
        assertEquals(404, response.getStatusCode().value());
        verify(service).getUrunlerByKrediNumarasi("123");
    }

    @Test
    void testGetUrunlerByKrediNumarasi_Found() {
        UrunBilgileri urun = new UrunBilgileri();
        when(service.getUrunlerByKrediNumarasi("123")).thenReturn(List.of(urun));
        ResponseEntity<List<UrunBilgileri>> response = controller.getUrunlerByKrediNumarasi("123");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(service).getUrunlerByKrediNumarasi("123");
    }

    @Test
    void testGetSiralarByKrediNumarasi_NotFound() {
        when(service.getSiralarByKrediNumarasi("123")).thenReturn(Collections.emptyList());
        ResponseEntity<List<Integer>> response = controller.getSiralarByKrediNumarasi("123");
        assertEquals(404, response.getStatusCode().value());
        verify(service).getSiralarByKrediNumarasi("123");
    }

    @Test
    void testGetSiralarByKrediNumarasi_Found() {
        when(service.getSiralarByKrediNumarasi("123")).thenReturn(List.of(1, 2, 3));
        ResponseEntity<List<Integer>> response = controller.getSiralarByKrediNumarasi("123");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(3, response.getBody().size());
        verify(service).getSiralarByKrediNumarasi("123");
    }

    @Test
    void testUpdateUrunBilgileri_NotFound() {
        when(service.updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class))).thenReturn(null);
        ResponseEntity<UrunBilgileri> response = controller.updateUrunBilgileri("123", 1, new UrunBilgileri());
        assertEquals(404, response.getStatusCode().value());
        verify(service).updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class));
    }

    @Test
    void testUpdateUrunBilgileri_Success() {
        UrunBilgileri urun = new UrunBilgileri();
        when(service.updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class))).thenReturn(urun);
        ResponseEntity<UrunBilgileri> response = controller.updateUrunBilgileri("123", 1, new UrunBilgileri());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(urun, response.getBody());
        verify(service).updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class));
    }

    @Test
    void testDeleteAndReinsertEgmStateInformation_NotFound() {
        when(service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", null))
                .thenReturn("Kredi numarası bulunamadı");
        ResponseEntity<String> response = controller.deleteAndReinsertEgmStateInformationByKrediNumarasi("123", null);
        assertEquals(404, response.getStatusCode().value());
        assertTrue(response.getBody().contains("bulunamadı"));
        verify(service).deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", null);
    }

    @Test
    void testDeleteAndReinsertEgmStateInformation_Success() {
        when(service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", 1))
                .thenReturn("Silme ve yeniden ekleme işlemi tamamlandı");
        ResponseEntity<String> response = controller.deleteAndReinsertEgmStateInformationByKrediNumarasi("123", 1);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().contains("tamamlandı"));
        verify(service).deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", 1);
    }
}