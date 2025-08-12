// dosya: demo/demo/src/test/java/com/example/demo/controller/KoOtoEvrakDurumControllerTest.java
package com.example.demo.controller;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.service.KoOtoEvrakDurumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KoOtoEvrakDurumControllerTest {

    @InjectMocks
    private KoOtoEvrakDurumController controller;

    @Mock
    private KoOtoEvrakDurumService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllKoOtoEvrakDurum_NotFound() {
        when(service.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getAllKoOtoEvrakDurum();
        assertEquals(404, response.getStatusCode().value());
        verify(service).findAll();
    }

    @Test
    void testGetAllKoOtoEvrakDurum_Found() {
        List<KoOtoEvrakDurum> list = Arrays.asList(new KoOtoEvrakDurum(), new KoOtoEvrakDurum());
        when(service.findAll()).thenReturn(list);
        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getAllKoOtoEvrakDurum();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(service).findAll();
    }

    @Test
    void testGetKoOtoEvrakDurumByKrediNumarasi_NotFound() {
        when(service.getKoOtoEvrakDurumByKrediNumarasi("123"))
                .thenReturn(Collections.emptyList());
        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getKoOtoEvrakDurumByKrediNumarasi("123");
        assertEquals(404, response.getStatusCode().value());
        verify(service).getKoOtoEvrakDurumByKrediNumarasi("123");
    }

    @Test
    void testGetKoOtoEvrakDurumByKrediNumarasi_Found() {
        KoOtoEvrakDurum evrak = new KoOtoEvrakDurum();
        when(service.getKoOtoEvrakDurumByKrediNumarasi("123"))
                .thenReturn(List.of(evrak));
        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getKoOtoEvrakDurumByKrediNumarasi("123");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(service).getKoOtoEvrakDurumByKrediNumarasi("123");
    }

    @Test
    void testGetKoOtoEvrakDurumByKrediAndEvrakKodu_NotFound() {
        when(service.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1"))
                .thenReturn(Optional.empty());
        ResponseEntity<KoOtoEvrakDurum> response = controller.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");
        assertEquals(404, response.getStatusCode().value());
        verify(service).getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");
    }

    @Test
    void testGetKoOtoEvrakDurumByKrediAndEvrakKodu_Found() {
        KoOtoEvrakDurum evrak = new KoOtoEvrakDurum();
        when(service.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1"))
                .thenReturn(Optional.of(evrak));
        ResponseEntity<KoOtoEvrakDurum> response = controller.getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(evrak, response.getBody());
        verify(service).getKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1");
    }

    @Test
    void testUpdateKoOtoEvrakDurumByKrediAndEvrakKodu_NotFound() {
        when(service.updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class)))
                .thenReturn(null);
        ResponseEntity<KoOtoEvrakDurum> response = controller.updateKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1", new KoOtoEvrakDurum());
        assertEquals(404, response.getStatusCode().value());
        verify(service).updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class));
    }

    @Test
    void testUpdateKoOtoEvrakDurumByKrediAndEvrakKodu_Found() {
        KoOtoEvrakDurum evrak = new KoOtoEvrakDurum();
        when(service.updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class)))
                .thenReturn(evrak);
        ResponseEntity<KoOtoEvrakDurum> response = controller.updateKoOtoEvrakDurumByKrediAndEvrakKodu("123", "E1", new KoOtoEvrakDurum());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(evrak, response.getBody());
        verify(service).updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq("123"), eq("E1"), any(KoOtoEvrakDurum.class));
    }
}