
        package com.example.demo.service;

import com.example.demo.entity.UrunBilgileri;
import com.example.demo.entity.UrunBilgileriId; // Burayı ekledik
import com.example.demo.repository.UrunBilgileriRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // Burayı ekledik

@Service
@RequiredArgsConstructor
public class UrunBilgileriService {

    private final UrunBilgileriRepository repository;

    public List<UrunBilgileri> getAllUrunler() {
        return repository.findAll();
    }

    // Kredi numarasına göre ürünleri getiren yeni metod
    public List<UrunBilgileri> getUrunlerByKrediNumarasi(String krediNumarasi) {
        return repository.findByKrediNumarasi(krediNumarasi);
    }

    // Belirli bir ürün bilgisini güncelleyen yeni metod
    public UrunBilgileri updateUrunBilgileri(String krediNumarasi, Integer sira, UrunBilgileri updatedUrunBilgileri) {
        UrunBilgileriId id = new UrunBilgileriId(krediNumarasi, sira);
        Optional<UrunBilgileri> existingUrunOptional = repository.findById(id);

        if (existingUrunOptional.isPresent()) {
            UrunBilgileri existingUrun = existingUrunOptional.get();
            // Sadece güncellenebilecek alanları ayarla
            existingUrun.setRehinDurum(updatedUrunBilgileri.getRehinDurum());
            existingUrun.setProductLineId(updatedUrunBilgileri.getProductLineId());
            // Diğer alanlar (krediNumarasi, sira) birincil anahtar olduğu için güncellenmemeli
            return repository.save(existingUrun);
        } else {
            // Belirtilen kredi numarası ve sıraya sahip ürün bulunamazsa null veya hata döndürebilirsin
            return null; // Veya bir exception fırlatabilirsin
        }
    }
}