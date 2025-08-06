package com.example.demo.service;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.repository.KoOtoEvrakDurumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KoOtoEvrakDurumService {

    private final KoOtoEvrakDurumRepository repository;

    public List<KoOtoEvrakDurum> findAll() {
        List<KoOtoEvrakDurum> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    public KoOtoEvrakDurum updateKoOtoEvrakDurumByKrediAndEvrakKodu(String krediNumarasi, String evrakKodu, KoOtoEvrakDurum updateData) {
        Optional<KoOtoEvrakDurum> optionalEntity = repository.findByKrediNumarasiAndEvrakKodu(krediNumarasi, evrakKodu);
        if (optionalEntity.isPresent()) {
            KoOtoEvrakDurum entity = optionalEntity.get();
            entity.setRehinDurum(updateData.getRehinDurum());
            return repository.save(entity);
        }
        return null;
    }
}