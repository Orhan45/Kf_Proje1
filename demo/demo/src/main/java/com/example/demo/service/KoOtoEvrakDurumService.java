package com.example.demo.service;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.repository.KoOtoEvrakDurumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KoOtoEvrakDurumService {

    private final KoOtoEvrakDurumRepository repository;

    public KoOtoEvrakDurum updateKoOtoEvrakDurumByKrediAndEvrakKodu(String krediNumarasi, String evrakKodu, KoOtoEvrakDurum updateData) {
        Optional<KoOtoEvrakDurum> optionalEntity = repository.findByKrediNumarasiAndEvrakKodu(krediNumarasi, evrakKodu);
        if (optionalEntity.isPresent()) {
            KoOtoEvrakDurum entity = optionalEntity.get();
            entity.setDurum(updateData.getDurum());
            return repository.save(entity);
        }
        return null;
    }

    public List<KoOtoEvrakDurum> getKoOtoEvrakDurumByKrediNumarasi(String krediNumarasi) {
        return repository.findAllByKrediNumarasi(krediNumarasi);
    }
}