package com.example.demo.repository;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.entity.UrunBilgileriId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Optional için import

@Repository
public interface UrunBilgileriRepository extends JpaRepository<UrunBilgileri, UrunBilgileriId> {

    List<UrunBilgileri> findByKrediNumarasi(String krediNumarasi);

    // Yeni metot: Kredi numarası ve sıra numarasına göre belirli bir kaydı bulmak için
    // Composite ID'yi kullanarak findById ile de yapılabilirdi, ama bu daha okunabilir.
    Optional<UrunBilgileri> findByIdKrediNumarasiAndIdSira(String krediNumarasi, Integer sira);
}