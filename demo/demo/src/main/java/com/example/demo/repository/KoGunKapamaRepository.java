// dosya: `demo/demo/src/main/java/com/example/demo/repository/KoGunKapamaRepository.java`
package com.example.demo.repository;

import com.example.demo.entity.KoGunKapama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

public interface KoGunKapamaRepository extends JpaRepository<KoGunKapama, LocalDate> {

    @Modifying
    @Transactional
    @Query("delete from KoGunKapama k where k.tarih > :date")
    void deleteRecordsAfterDay(@Param("date") LocalDate date);

    boolean existsByTarih(LocalDate tarih);
}