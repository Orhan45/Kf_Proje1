package com.example.demo.controller;

import com.example.demo.entity.UrunBilgileri; // Import güncellendi
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
    public List<UrunBilgileri> getProductionInformation() { // Metot imzası güncellendi
        return service.getAllUrunler();
    }

    @GetMapping("/kredi/{krediNumarasi}")
    public ResponseEntity<List<UrunBilgileri>> getUrunlerByKrediNumarasi(@PathVariable String krediNumarasi) { // Metot imzası güncellendi
        List<UrunBilgileri> urunler = service.getUrunlerByKrediNumarasi(krediNumarasi); // Tip güncellendi
        if (urunler.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(urunler);
    }

    @PutMapping("/{krediNumarasi}/{sira}")
    public ResponseEntity<UrunBilgileri> updateUrunBilgileri( // Metot imzası güncellendi
                                                              @PathVariable String krediNumarasi,
                                                              @PathVariable Integer sira,
                                                              @RequestBody UrunBilgileri urunBilgileri) { // Parametre tipi güncellendi
        UrunBilgileri updated = service.updateUrunBilgileri(krediNumarasi, sira, urunBilgileri); // Tip güncellendi
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete-and-reinsert-state-info/{productLineId}")
    public ResponseEntity<String> deleteAndReinsertEgmStateInformation(@PathVariable Long productLineId) {
        boolean processed = service.deleteAndReinsertEgmStateInformation(productLineId);
        if (processed) {
            return ResponseEntity.ok("EgmStateInformation tablosundan product_line_id: " + productLineId + " ile eşleşen kayıt(lar) başarıyla silindi ve yeni bir kayıt eklendi.");
        } else {
            return ResponseEntity.status(404).body("EgmStateInformation tablosunda product_line_id: " + productLineId + " ile eşleşen kayıt bulunamadı veya işlem gerçekleştirilemedi.");
        }
    }
}