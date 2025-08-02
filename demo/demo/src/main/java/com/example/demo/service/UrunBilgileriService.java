package com.example.demo.service;

import com.example.demo.entity.UrunBilgileri; // Import güncellendi
import com.example.demo.entity.UrunBilgileriId;
import com.example.demo.entity.EgmStateInformation; // Import güncellendi
import com.example.demo.repository.UrunBilgileriRepository;
import com.example.demo.repository.EgmStateInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrunBilgileriService {

    private final UrunBilgileriRepository urunBilgileriRepository;
    private final EgmStateInformationRepository egmStateInformationRepository;

    public List<UrunBilgileri> getAllUrunler() { // Metot imzası güncellendi
        return urunBilgileriRepository.findAll();
    }

    public List<UrunBilgileri> getUrunlerByKrediNumarasi(String krediNumarasi) { // Metot imzası güncellendi
        return urunBilgileriRepository.findByKrediNumarasi(krediNumarasi);
    }

    public UrunBilgileri updateUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileri updatedUrunBilgileri) { // Metot imzası güncellendi
        UrunBilgileriId id = new UrunBilgileriId(krediNumarasi, sira);
        Optional<UrunBilgileri> existingUrunOptional = urunBilgileriRepository.findById(id); // Tip güncellendi

        if (existingUrunOptional.isPresent()) {
            UrunBilgileri existingUrun = existingUrunOptional.get(); // Tip güncellendi
            existingUrun.setRehinDurum(updatedUrunBilgileri.getRehinDurum());
            existingUrun.setProductLineId(updatedUrunBilgileri.getProductLineId());
            return urunBilgileriRepository.save(existingUrun);
        } else {
            return null;
        }
    }

    // Yeni metod: EgmStateInformation'a yeni bir kayıt ekle
    @Transactional
    public EgmStateInformation createNewEgmStateInformation(Long productLineId) { // Tip güncellendi
        EgmStateInformation newEntry = EgmStateInformation.builder() // Builder kullanımı güncellendi
                .productLineId(productLineId)
                .stateId(7)
                .stateStatus(1)
                .message("BAŞARILI")
                .levelInfo("info")
                .isDeleted(false)
                .processDate(LocalDateTime.now())
                .build();

        return egmStateInformationRepository.save(newEntry);
    }

    // Belirli bir ürün bilgisini silen metod (güncellendi)
    @Transactional
    public boolean deleteAndReinsertEgmStateInformation(Long productLineId) {
        List<EgmStateInformation> recordsToDelete = egmStateInformationRepository.findByProductLineId(productLineId); // Tip güncellendi

        if (recordsToDelete.isEmpty()) {
            return false;
        } else {
            egmStateInformationRepository.deleteByProductLineId(productLineId);
            createNewEgmStateInformation(productLineId);
            return true;
        }
    }
}