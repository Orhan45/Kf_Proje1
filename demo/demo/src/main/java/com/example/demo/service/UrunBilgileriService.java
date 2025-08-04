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
import java.util.stream.Collectors; // Stream API için eklendi

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
    public boolean processEgmStateInformationForProductLine(Long productLineId) {
        List<EgmStateInformation> existingRecords = egmStateInformationRepository.findByProductLineId(productLineId);

        if (existingRecords.isEmpty()) {
            // Eğer kayıt yoksa, sadece ekleme yapabiliriz veya false dönebiliriz.
            // Bu senaryoda "Hasar sorgu atlat" işlemi genellikle mevcut bir kaydı güncellemeyi/tekrarı içerir.
            // Ancak, yeni bir product_line_id için de eklenebilir. Mevcut durumda false dönsün.
            return false;
        }

        // Mevcut kayıtları sil
        egmStateInformationRepository.deleteByProductLineId(productLineId);

        // Yeni bir kayıt ekle - BELİRTİLEN SABİT DEĞERLERLE
        EgmStateInformation newRecord = EgmStateInformation.builder()
                .productLineId(productLineId)
                .stateId(7)           // Belirtilen sabit değer
                .stateStatus(1)       // Belirtilen sabit değer
                .message("BAŞARILI")  // Belirtilen sabit değer
                .levelInfo("INFO")    // Belirtilen sabit değer
                .isDeleted(false)     // Belirtilen sabit değer (0 yerine false)
                .processDate(LocalDateTime.now()) // MSSQL için GETDATE() veya SYSDATETIME() karşılığı
                .build();

        egmStateInformationRepository.save(newRecord);
        return true;
    }

    // YENİ METOT: Kredi numarası ve isteğe bağlı sıra numarasına göre işlem yapacak
    @Transactional
    public String deleteAndReinsertEgmStateInformationByKrediNumarasiAndSira(String krediNumarasi, Integer sira) {
        List<UrunBilgileri> urunler;

        if (sira != null && sira != -1) { // -1 "Tümünü Seç" anlamına gelsin
            // Belirli bir sıra numarasına göre filtrele
            urunler = urunBilgileriRepository.findByIdKrediNumarasiAndIdSira(krediNumarasi, sira)
                    .map(List::of) // Optional'ı List'e çevir
                    .orElse(Collections.emptyList());
        } else {
            // Kredi numarasına ait tüm ürünleri getir ("Tümünü Seç" durumu)
            urunler = urunBilgileriRepository.findByKrediNumarasi(krediNumarasi);
        }

        if (urunler.isEmpty()) {
            return "Kredi numarası: " + krediNumarasi + (sira != null ? " ve sıra: " + sira : "") + " ile eşleşen kayıt bulunamadı.";
        }

        // Bulunan her bir product_line_id için işlemi tekrarla
        boolean allProcessed = true;
        for (UrunBilgileri urun : urunler) {
            boolean processed = processEgmStateInformationForProductLine(urun.getProductLineId());
            if (!processed) {
                allProcessed = false; // Bir tanesi bile başarısız olursa genel durumu false yap
                // İstersen burada daha detaylı hata loglayabilirsin
            }
        }

        if (allProcessed) {
            return "Kredi numarası: " + krediNumarasi + (sira != null ? " ve sıra: " + sira : "") + " için EgmStateInformation başarıyla güncellendi.";
        } else {
            return "Kredi numarası: " + krediNumarasi + (sira != null ? " ve sıra: " + sira : "") + " için bazı işlemler tamamlanamadı.";
        }
    }

    // Yeni metot: Frontend'e sıra numaralarını döndürmek için
    public List<Integer> getSiralarByKrediNumarasi(String krediNumarasi) {
        return urunBilgileriRepository.findByKrediNumarasi(krediNumarasi)
                .stream()
                .map(UrunBilgileri::getSira)
                .distinct() // Tekrar eden sıra numaralarını önle
                .sorted() // Sıralı olsun
                .collect(Collectors.toList());
    }
}