package com.example.demo.controller;

import com.example.demo.service.KoGunKapamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/kogunkapama")
@RequiredArgsConstructor
public class KoGunKapamaController {

    private final KoGunKapamaService koGunKapamaService;

    @DeleteMapping("/process/{date}")
    public ResponseEntity<Void> processKoGunKapamaByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        koGunKapamaService.processRecordsForDate(date);
        return ResponseEntity.ok().build();
    }
}
