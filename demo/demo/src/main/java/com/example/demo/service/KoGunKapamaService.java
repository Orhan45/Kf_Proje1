package com.example.demo.service;

import com.example.demo.entity.KoGunKapama;
import com.example.demo.repository.KoGunKapamaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class KoGunKapamaService {

    private final KoGunKapamaRepository repository;

    public KoGunKapamaService(KoGunKapamaRepository repository) {
        this.repository = repository;
    }

    public List<KoGunKapama> findAll() {
        return repository.findAll();
    }

    public KoGunKapama save(KoGunKapama entity) {
        return repository.save(entity);
    }

    // Eğer tabloda girilen tarih bulunmuyorsa önce ekler, ardından o tarihten sonrakileri siler.
    public void processRecordsForDate(LocalDate day) {
        if (!repository.existsByTarih(day)) {
            KoGunKapama record = new KoGunKapama();
            record.setTarih(day);
            record.setUserName("FINANS"); // userName "fınans"
            record.setProcessDate(LocalDateTime.now());
            repository.save(record);
        }
        repository.deleteRecordsAfterDay(day);
    }
}