package com.example.demo.controller;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.service.KoOtoEvrakDurumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kootoevrakdurum")
@RequiredArgsConstructor
public class KoOtoEvrakDurumController {

    private final KoOtoEvrakDurumService koOtoService;

    @GetMapping
    public ResponseEntity<List<KoOtoEvrakDurum>> getAllKoOtoEvrakDurum() {
        List<KoOtoEvrakDurum> list = koOtoService.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/kredi/{krediNumarasi}")
    public ResponseEntity<List<KoOtoEvrakDurum>> getKoOtoEvrakDurumByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<KoOtoEvrakDurum> list = koOtoService.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{krediNumarasi}/{evrakKodu}")
    public ResponseEntity<KoOtoEvrakDurum> getKoOtoEvrakDurumByKrediAndEvrakKodu(
            @PathVariable String krediNumarasi,
            @PathVariable String evrakKodu) {
        Optional<KoOtoEvrakDurum> optionalEvrak = koOtoService.getKoOtoEvrakDurumByKrediAndEvrakKodu(krediNumarasi, evrakKodu);
        return optionalEvrak.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{krediNumarasi}/{evrakKodu}")
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
