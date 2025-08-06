package com.example.demo.controller;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.entity.UrunBilgileri;
import com.example.demo.service.KoOtoEvrakDurumService;
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
    private final KoOtoEvrakDurumService koOtoService;

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

    @DeleteMapping("/delete-and-reinsert-state-info-by-kredi")
    public ResponseEntity<String> deleteAndReinsertEgmStateInformationByKrediNumarasi(
            @RequestParam String krediNumarasi,
            @RequestParam(required = false) Integer sira) {
        String responseMessage = service.deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(krediNumarasi, sira);
        if (responseMessage.contains("bulunamadı")) {
            return ResponseEntity.status(404).body(responseMessage);
        }
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/kootoevrakdurum")
    public ResponseEntity<List<KoOtoEvrakDurum>> getAllKoOtoEvrakDurum() {
        List<KoOtoEvrakDurum> list = koOtoService.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/kootoevrakdurum/kredi/{krediNumarasi}")
    public ResponseEntity<List<KoOtoEvrakDurum>> getKoOtoEvrakDurumByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<KoOtoEvrakDurum> list = koOtoService.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }


    @PutMapping("/kootoevrakdurum/update/{krediNumarasi}/{evrakKodu}")
    public ResponseEntity<KoOtoEvrakDurum> updateKoOtoEvrakDurumByKrediAndEvrakKodu(
            @PathVariable String krediNumarasi,
            @PathVariable("evrakKodu") String evrakKodu,
            @RequestBody KoOtoEvrakDurum updateData) {
        KoOtoEvrakDurum updated = koOtoService.updateKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu, updateData);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}