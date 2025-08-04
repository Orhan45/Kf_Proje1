package com.example.demo.controller;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.service.UrunBilgileriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urunler")
@RequiredArgsConstructor
public class UrunBilgileriController {

    private final UrunBilgileriService service;

    @GetMapping
    public List<UrunBilgileri> getProductionInformation() {
        return service.getAllUrunler();
    }

    @GetMapping("/kredi/{krediNumarasi}")
    public ResponseEntity<List<UrunBilgileri>> getUrunlerByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<UrunBilgileri> urunler = service.getUrunlerByKrediNumarasi(krediNumarasi);
        if (urunler.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(urunler);
    }

    // YENİ ENDPOINT: Kredi numarasına ait sıra numaralarını döndürür
    @GetMapping("/siralar/{krediNumarasi}")
    public ResponseEntity<List<Integer>> getSiralarByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<Integer> siralar = service.getSiralarByKrediNumarasi(krediNumarasi);
        if (siralar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(siralar);
    }


    @PutMapping("/{krediNumarasi}/{sira}")
    public ResponseEntity<UrunBilgileri> updateUrunBilgileri(
            @PathVariable String krediNumarasi,
            @PathVariable Integer sira,
            @RequestBody UrunBilgileri urunBilgileri) {
        UrunBilgileri updated = service.updateUrunBilgileri(krediNumarasi, sira, urunBilgileri);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // GÜNCELLENMİŞ DELETE ENDPOINT: Kredi numarası ve isteğe bağlı sıra numarası ile işlem yapacak
    // Not: @RequestParam kullanmak, sıra numarasının URL'de zorunlu olmamasını sağlar.
    // Eğer sıra numarası verilmezse (null gelir), servis tüm sıra numaralarını işler.
    @DeleteMapping("/delete-and-reinsert-state-info-by-kredi")
    public ResponseEntity<String> deleteAndReinsertEgmStateInformationByKrediNumarasi(
            @RequestParam String krediNumarasi,
            @RequestParam(required = false) Integer sira) { // Sira opsiyonel
        String responseMessage = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);
        if (responseMessage.contains("bulunamadı")) { // Servis mesajına göre hata kodu dönebiliriz
            return ResponseEntity.status(404).body(responseMessage);
        }
        return ResponseEntity.ok(responseMessage);
    }
}