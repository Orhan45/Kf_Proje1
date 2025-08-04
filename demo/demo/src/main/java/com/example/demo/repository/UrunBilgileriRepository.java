package com.example.demo.repository;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.entity.UrunBilgileriId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrunBilgileriRepository extends JpaRepository<UrunBilgileri, UrunBilgileriId> {

    List<UrunBilgileri> findByKrediNumarasi(String krediNumarasi);

    // DÜZELTME: Metot adı "findByKrediNumarasiAndSira" olarak değiştirildi.
    // Spring Data JPA'da bileşik anahtar için doğrudan entity'deki field isimleri kullanılır.
    Optional<UrunBilgileri> findByKrediNumarasiAndSira(String krediNumarasi, Integer sira);
}
