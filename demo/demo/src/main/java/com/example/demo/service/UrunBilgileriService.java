package com.example.demo.service;

import com.example.demo.entity.EgmStateInformation;
import com.example.demo.entity.UrunBilgileri;
import com.example.demo.repository.EgmStateInformationRepository;
import com.example.demo.repository.UrunBilgileriRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UrunBilgileriService {

    private final UrunBilgileriRepository urunBilgileriRepository;
    private final EgmStateInformationRepository egmStateInformationRepository;

    public List<UrunBilgileri> getAllUrunler() {
        return urunBilgileriRepository.findAll();
    }

    public List<UrunBilgileri> getUrunlerByKrediNumarasi(String krediNumarasi) {
        return urunBilgileriRepository.findByKrediNumarasi(krediNumarasi);
    }

    @Transactional
    public UrunBilgileri updateUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileri urunBilgileri) {
        Optional<UrunBilgileri> existingUrunOptional = urunBilgileriRepository.findById(new com.example.demo.entity.UrunBilgileriId(krediNumarasi, sira));

        if (existingUrunOptional.isPresent()) {
            UrunBilgileri existingUrun = existingUrunOptional.get();
            // Yalnızca rehinDurum alanını güncelliyoruz
            existingUrun.setRehinDurum(urunBilgileri.getRehinDurum());
            // ProductLineId'yi güncelleme satırı kaldırıldı.
            return urunBilgileriRepository.save(existingUrun);
        }
        return null;
    }

    @Transactional
    public boolean processEgmStateInformationForProductLine(Long productLineId) {
        List<EgmStateInformation> existingRecords = egmStateInformationRepository.findByProductLineId(productLineId);

        if (existingRecords.isEmpty()) {
            // Eğer kayıt yoksa, false dönebiliriz.
            return false;
        }

        // Mevcut kayıtları sil
        egmStateInformationRepository.deleteByProductLineId(productLineId);

        // Yeni bir kayıt ekle
        EgmStateInformation newRecord = EgmStateInformation.builder()
                .productLineId(productLineId)
                .stateId(7)
                .stateStatus(1)
                .message("BAŞARILI")
                .levelInfo("INFO")
                .isDeleted(false)
                .processDate(LocalDateTime.now())
                .build();

        egmStateInformationRepository.save(newRecord);
        return true;
    }

    @Transactional
    public String deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(String krediNumarasi, Integer sira) {
        List<UrunBilgileri> urunler;

        if (sira != null && sira != -1) {
            urunler = urunBilgileriRepository.findByKrediNumarasiAndSira(krediNumarasi, sira)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        } else {
            // Kredi numarasına ait tüm ürünleri getir
            urunler = urunBilgileriRepository.findByKrediNumarasi(krediNumarasi);
        }

        if (urunler.isEmpty()) {
            return "Kredi numarası: " + krediNumarasi + (sira != null ? " ve sıra: " + sira : "") + " ile eşleşen kayıt bulunamadı.";
        }

        boolean allProcessed = true;
        for (UrunBilgileri urun : urunler) {
            boolean processed = processEgmStateInformationForProductLine(urun.getProductLineId());
            if (!processed) {
                allProcessed = false;
            }
        }

        if (allProcessed) {
            return "Kredi numarası: " + krediNumarasi + (sira != null ? " ve sıra: " + sira : "") + " için EgmStateInformation başarıyla güncellendi.";
        } else {
            return "Kredi numarası: " + krediNumarasi + (sira != null ? " ve sıra: " + sira : "") + " için bazı işlemler tamamlanamadı.";
        }
    }

    public List<Integer> getSiralarByKrediNumarasi(String krediNumarasi) {
        return urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)
                .stream()
                .map(UrunBilgileri::getSira)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
