
 package com.example.demo.controller;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.service.UrunBilgileriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // Burayı ekledik
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

    // Kredi numarasına göre arama endpoint'i
    // GET http://localhost:8081/api/urunler/kredi/{krediNumarasi}
    @GetMapping("/kredi/{krediNumarasi}")
    public ResponseEntity<List<UrunBilgileri>> getUrunlerByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<UrunBilgileri> urunler = service.getUrunlerByKrediNumarasi(krediNumarasi);
        if (urunler.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(urunler);
    }

    // Kredi numarası ve sıraya göre güncelleme endpoint'i
    // PUT http://localhost:8081/api/urunler/{krediNumarasi}/{sira}
    // Request Body: { "rehinDurum": 1, "productLineId": 123 }
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
}