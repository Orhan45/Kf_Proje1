package com.example.demo.repository;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.entity.UrunBilgileriId; // Burayı ekledik
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrunBilgileriRepository extends JpaRepository<UrunBilgileri, UrunBilgileriId> {

    List<UrunBilgileri> findByKrediNumarasi(String krediNumarasi);
}