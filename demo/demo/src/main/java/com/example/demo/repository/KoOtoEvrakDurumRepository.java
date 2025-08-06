package com.example.demo.repository;

import com.example.demo.entity.KoOtoEvrakDurum;
import com.example.demo.entity.KoOtoEvrakDurumId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KoOtoEvrakDurumRepository extends CrudRepository<KoOtoEvrakDurum, KoOtoEvrakDurumId> {
    Optional<KoOtoEvrakDurum> findByKrediNumarasiAndEvrakKodu(String krediNumarasi, String evrakKodu);
    List<KoOtoEvrakDurum> findAllByKrediNumarasi(String krediNumarasi);
}