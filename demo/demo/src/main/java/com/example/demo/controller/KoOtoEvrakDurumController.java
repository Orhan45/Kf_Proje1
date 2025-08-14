package com.example.demo.controller;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.service.KoOtoEvrakDurumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kootoevrakdurum")
@RequiredArgsConstructor
public class KoOtoEvrakDurumController {

    private final KoOtoEvrakDurumService koOtoService;

    @GetMapping("/kredi/{krediNumarasi}")
    public ResponseEntity<List<KoOtoEvrakDurum>> getKoOtoEvrakDurumByKrediNumarasi(@PathVariable String krediNumarasi) {
        List<KoOtoEvrakDurum> list = koOtoService.getKoOtoEvrakDurumByKrediNumarasi(krediNumarasi);
        if (list == null || list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
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