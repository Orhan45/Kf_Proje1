// dil: java
// Dosya: `demo/src/main/java/com/example/demo/controller/UrunBilgileriController.java`
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

    // Yeni eklenen metot
    @DeleteMapping("/egm/{krediNumarasi}")
    public ResponseEntity<String> deleteAndReinsertEgmStateInformationByKrediNumarasi(
            @PathVariable String krediNumarasi,
            @RequestParam(required = false) Integer sira) {
        String result = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);
        if (result.contains("bulunamadı")) {
            return ResponseEntity.status(404).body(result);
        }
        return ResponseEntity.ok(result);
    }
}