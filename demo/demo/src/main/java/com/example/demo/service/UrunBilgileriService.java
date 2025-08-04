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
            existingUrun.setRehinDurum(urunBilgileri.getRehinDurum());
            existingUrun.setProductLineId(urunBilgileri.getProductLineId());
            return urunBilgileriRepository.save(existingUrun);
        }
        return null;
    }

    @Transactional
    public boolean deleteAndReinsertEgmStateInformation(Long productLineId) {
        List<EgmStateInformation> existingRecords = egmStateInformationRepository.findByProductLineId(productLineId);

        if (existingRecords.isEmpty()) {
            return false;
        }

        egmStateInformationRepository.deleteByProductLineId(productLineId);

        EgmStateInformation newRecord = EgmStateInformation.builder()
                .productLineId(productLineId)
                .stateId(99)
                .stateStatus(1)
                .message("Hasar sorgu atlatma işlemi tekrarlandı.")
                .levelInfo("INFO")
                .isDeleted(false)
                .processDate(LocalDateTime.now())
                .build();

        egmStateInformationRepository.save(newRecord);
        return true;
    }

    // SADECE BU METOT KULLANILACAK: Kredi numarasına göre productLineId'yi bulup işlem yapacak
    @Transactional
    public boolean deleteAndReinsertEgmStateInformationByKrediNumarasi(String krediNumarasi) {
        List<UrunBilgileri> urunler = urunBilgileriRepository.findByKrediNumarasi(krediNumarasi);

        if (urunler.isEmpty()) {
            return false; // Bu kredi numarasına ait ürün bulunamadı
        }

        // Kredi numarasına ait ilk productLineId'yi kullanıyoruz.
        // Eğer bir kredi numarasına birden fazla productLineId eşleşiyorsa
        // ve hepsini işlemeliysek, buradaki mantığı döngüye almalıyız.
        // Veya sadece tek bir eşleşme beklentisi varsa, bu haliyle yeterli.
        Long productLineIdToProcess = urunler.get(0).getProductLineId();

        // productLineId'ye göre silme ve yeniden ekleme mantığını çağırıyoruz.
        return deleteAndReinsertEgmStateInformation(productLineIdToProcess);
    }
}