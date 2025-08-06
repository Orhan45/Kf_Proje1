// Java
package com.example.demo.controller;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.entity.UrunBilgileri;
import com.example.demo.service.KoOtoEvrakDurumService;
import com.example.demo.service.UrunBilgileriService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrunBilgileriControllerTest {

    @Mock
    private UrunBilgileriService service;

    @Mock
    private KoOtoEvrakDurumService koOtoService;

    @InjectMocks
    private UrunBilgileriController controller;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProductionInformation_shouldReturnList() {
        UrunBilgileri urun1 = new UrunBilgileri();
        UrunBilgileri urun2 = new UrunBilgileri();
        when(service.getAllUrunler()).thenReturn(Arrays.asList(urun1, urun2));

        List<UrunBilgileri> result = controller.getProductionInformation();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(service, times(1)).getAllUrunler();
    }

    @Test
    void getUrunlerByKrediNumarasi_whenEmpty_shouldReturnNotFound() {
        when(service.getUrunlerByKrediNumarasi("123")).thenReturn(Collections.emptyList());

        ResponseEntity<List<UrunBilgileri>> response = controller.getUrunlerByKrediNumarasi("123");

        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.hasBody());
        verify(service, times(1)).getUrunlerByKrediNumarasi("123");
    }

    @Test
    void getUrunlerByKrediNumarasi_whenFound_shouldReturnOk() {
        UrunBilgileri urun = new UrunBilgileri();
        when(service.getUrunlerByKrediNumarasi("123")).thenReturn(List.of(urun));

        ResponseEntity<List<UrunBilgileri>> response = controller.getUrunlerByKrediNumarasi("123");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).getUrunlerByKrediNumarasi("123");
    }

    @Test
    void getSiralarByKrediNumarasi_whenEmpty_shouldReturnNotFound() {
        when(service.getSiralarByKrediNumarasi("123")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Integer>> response = controller.getSiralarByKrediNumarasi("123");

        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.hasBody());
        verify(service, times(1)).getSiralarByKrediNumarasi("123");
    }

    @Test
    void getSiralarByKrediNumarasi_whenFound_shouldReturnOk() {
        when(service.getSiralarByKrediNumarasi("123")).thenReturn(List.of(1, 2, 3));

        ResponseEntity<List<Integer>> response = controller.getSiralarByKrediNumarasi("123");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(service, times(1)).getSiralarByKrediNumarasi("123");
    }

    @Test
    void updateUrunBilgileri_whenUpdated_shouldReturnOk() {
        UrunBilgileri urun = new UrunBilgileri();
        when(service.updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class))).thenReturn(urun);

        ResponseEntity<UrunBilgileri> response = controller.updateUrunBilgileri("123", 1, new UrunBilgileri());

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(service, times(1)).updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class));
    }

    @Test
    void updateUrunBilgileri_whenNotFound_shouldReturnNotFound() {
        when(service.updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class))).thenReturn(null);

        ResponseEntity<UrunBilgileri> response = controller.updateUrunBilgileri("123", 1, new UrunBilgileri());

        assertEquals(404, response.getStatusCode().value());
        verify(service, times(1)).updateUrunBilgileri(eq("123"), eq(1), any(UrunBilgileri.class));
    }

    @Test
    void deleteAndReinsertEgmStateInformationByKrediNumarasi_whenNotFound_shouldReturn404() {
        when(service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", null))
                .thenReturn("Kredi numarası bulunamadı");

        ResponseEntity<String> response = controller.deleteAndReinsertEgmStateInformationByKrediNumarasi("123", null);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("bulunamadı"));
        verify(service, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", null);
    }

    @Test
    void deleteAndReinsertEgmStateInformationByKrediNumarasi_whenSuccess_shouldReturnOk() {
        when(service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", 1))
                .thenReturn("Silme ve yeniden ekleme işlemi tamamlandı");

        ResponseEntity<String> response = controller.deleteAndReinsertEgmStateInformationByKrediNumarasi("123", 1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("tamamlandı"));
        verify(service, times(1)).deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira("123", 1);
    }

    @Test
    void getAllKoOtoEvrakDurum_whenEmpty_shouldReturnNotFound() {
        when(koOtoService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getAllKoOtoEvrakDurum();

        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.hasBody());
        verify(koOtoService, times(1)).findAll();
    }

    @Test
    void getAllKoOtoEvrakDurum_whenFound_shouldReturnOk() {
        KoOtoEvrakDurum evrak1 = new KoOtoEvrakDurum();
        KoOtoEvrakDurum evrak2 = new KoOtoEvrakDurum();
        when(koOtoService.findAll()).thenReturn(List.of(evrak1, evrak2));

        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getAllKoOtoEvrakDurum();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(koOtoService, times(1)).findAll();
    }

    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_whenEmpty_shouldReturnNotFound() {
        when(koOtoService.getKoOtoEvrakDurumByKrediNumarasi("123")).thenReturn(Collections.emptyList());

        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getKoOtoEvrakDurumByKrediNumarasi("123");

        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.hasBody());
        verify(koOtoService, times(1)).getKoOtoEvrakDurumByKrediNumarasi("123");
    }

    @Test
    void getKoOtoEvrakDurumByKrediNumarasi_whenFound_shouldReturnOk() {
        KoOtoEvrakDurum evrak = new KoOtoEvrakDurum();
        when(koOtoService.getKoOtoEvrakDurumByKrediNumarasi("123")).thenReturn(List.of(evrak));

        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getKoOtoEvrakDurumByKrediNumarasi("123");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(koOtoService, times(1)).getKoOtoEvrakDurumByKrediNumarasi("123");
    }

    @Test
    void getKoOtoEvrakDurumByKrediAndEvrakKodu_whenNotFound_shouldReturnNotFound() {
        when(koOtoService.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1"))
                .thenReturn(Optional.empty());

        ResponseEntity<KoOtoEvrakDurum> response = controller.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");

        assertEquals(404, response.getStatusCode().value());
        verify(koOtoService, times(1)).getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");
    }

    @Test
    void getKoOtoEvrakDurumByKrediAndEvrakKodu_whenFound_shouldReturnOk() {
        KoOtoEvrakDurum evrak = new KoOtoEvrakDurum();
        when(koOtoService.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1"))
                .thenReturn(Optional.of(evrak));

        ResponseEntity<KoOtoEvrakDurum> response = controller.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(koOtoService, times(1)).getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");
    }

    @Test
    void updateKoOtoEvrakDurumByKrediAndEvrakKodu_whenNotFound_shouldReturnNotFound() {
        when(koOtoService.updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class)))
                .thenReturn(null);

        ResponseEntity<KoOtoEvrakDurum> response = controller.updateKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1", new KoOtoEvrakDurum());

        assertEquals(404, response.getStatusCode().value());
        verify(koOtoService, times(1)).updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class));
    }

    @Test
    void updateKoOtoEvrakDurumByKrediAndEvrakKodu_whenUpdated_shouldReturnOk() {
        KoOtoEvrakDurum evrak = new KoOtoEvrakDurum();
        when(koOtoService.updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class)))
                .thenReturn(evrak);

        ResponseEntity<KoOtoEvrakDurum> response = controller.updateKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1", new KoOtoEvrakDurum());

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(koOtoService, times(1)).updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class));
    }
}