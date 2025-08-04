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

    // ARTIK SADECE BU DELETE ENDPOINT'İ KULLANILACAK: Kredi numarasına göre işlem yapacak
    @DeleteMapping("/delete-and-reinsert-state-info-by-kredi/{krediNumarasi}")
    public ResponseEntity<String> deleteAndReinsertEgmStateInformationByKrediNumarasi(@PathVariable String krediNumarasi) {
        boolean processed = service.deleteAndReinsertEgmStateInformationByKrediNumarasi(krediNumarasi);
        if (processed) {
            return ResponseEntity.ok("Kredi numarası: " + krediNumarasi + " ile eşleşen kaydın EgmStateInformation'ı başarıyla güncellendi.");
        } else {
            return ResponseEntity.status(404).body("Kredi numarası: " + krediNumarasi + " ile eşleşen kayıt bulunamadı veya işlem gerçekleştirilemedi.");
        }
    }
}