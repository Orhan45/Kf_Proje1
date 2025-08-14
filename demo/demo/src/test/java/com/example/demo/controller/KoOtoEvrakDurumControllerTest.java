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

class KoOtoEvrakDurumControllerTest {

    @InjectMocks
    private KoOtoEvrakDurumController controller;

    @Mock
    private KoOtoEvrakDurumService service;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    // --- getKoOtoEvrakDurumByKrediNumarasi Metodu İçin Testler ---

    @Test
    void testGetKoOtoEvrakDurumByKrediNumarasi_shouldReturnNotFound_whenNoRecordsExist() {
        // Hazırlık: Servis metodu boş bir liste döndürdüğünde ne olacağını test ediyoruz.
        String krediNumarasi = "123";
        when(service.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi))
                .thenReturn(Collections.emptyList());

        // Çalıştırma
        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);

        // Kontrol: Dönüş kodunun 404 (Not Found) olduğunu doğrula
        assertEquals(404, response.getStatusCode().value());

        // Kontrol: Servis metodunun doğru parametreyle çağrıldığını doğrula
        verify(service).getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
    }

    @Test
    void testGetKoOtoEvrakDurumByKrediNumarasi_shouldReturnOk_whenRecordsExist() {
        // Hazırlık: Servis metodu dolu bir liste döndürdüğünde ne olacağını test ediyoruz.
        String krediNumarasi = "123";
        List<KoOtoEvrakDurum> mockList = Arrays.asList(new KoOtoEvrakDurum(), new KoOtoEvrakDurum());
        when(service.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi))
                .thenReturn(mockList);

        // Çalıştırma
        ResponseEntity<List<KoOtoEvrakDurum>> response = controller.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);

        // Kontrol: Dönüş kodunun 200 (OK) olduğunu doğrula
        assertEquals(200, response.getStatusCode().value());

        // Kontrol: Dönüş gövdesinin beklenen listeyle aynı olduğunu doğrula
        assertEquals(mockList, response.getBody());

        // Kontrol: Servis metodunun doğru parametreyle çağrıldığını doğrula
        verify(service).getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
    }

    // --- updateKoOtoEvrakDurumByKrediAndEvrakKodu Metodu İçin Testler ---

    @Test
    void testUpdateKoOtoEvrakDurumByKrediAndEvrakKodu_shouldReturnNotFound_whenRecordDoesNotExist() {
        // Hazırlık: Servis metodu null döndürdüğünde (kayıt bulunamadığında) ne olacağını test ediyoruz.
        String krediNumarasi = "123";
        String evrakKodu = "E1";
        KoOtoEvrakDurum updateData = new KoOtoEvrakDurum();

        when(service.updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq(krediNumarasi), eq(evrakKodu), any(KoOtoEvrakDurum.class)))
                .thenReturn(null);

        // Çalıştırma
        ResponseEntity<KoOtoEvrakDurum> response = controller.updateKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);

        // Kontrol: Dönüş kodunun 404 (Not Found) olduğunu doğrula
        assertEquals(404, response.getStatusCode().value());

        // Kontrol: Servis metodunun doğru parametrelerle çağrıldığını doğrula
        verify(service).updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq(krediNumarasi), eq(evrakKodu), any(KoOtoEvrakDurum.class));
    }

    @Test
    void testUpdateKoOtoEvrakDurumByKrediAndEvrakKodu_shouldReturnOk_whenRecordIsUpdated() {
        // Hazırlık: Servis metodu güncellenmiş bir nesne döndürdüğünde ne olacağını test ediyoruz.
        String krediNumarasi = "123";
        String evrakKodu = "E1";
        KoOtoEvrakDurum updateData = new KoOtoEvrakDurum();
        KoOtoEvrakDurum updatedEntity = new KoOtoEvrakDurum();

        when(service.updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq(krediNumarasi), eq(evrakKodu), any(KoOtoEvrakDurum.class)))
                .thenReturn(updatedEntity);

        // Çalıştırma
        ResponseEntity<KoOtoEvrakDurum> response = controller.updateKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);

        // Kontrol: Dönüş kodunun 200 (OK) olduğunu doğrula
        assertEquals(200, response.getStatusCode().value());

        // Kontrol: Dönüş gövdesinin beklenen nesneyle aynı olduğunu doğrula
        assertEquals(updatedEntity, response.getBody());

        // Kontrol: Servis metodunun doğru parametrelerle çağrıldığını doğrula
        verify(service).updateKoOtoEvrakDurumByKrediAndEvrakKodu(eq(krediNumarasi), eq(evrakKodu), any(KoOtoEvrakDurum.class));
    }
}