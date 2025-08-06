package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Entity
@Table(name = "KO_OTO_EVRAK_DURUM")
@IdClass(KoOtoEvrakDurumId.class)
@NoArgsConstructor
@AllArgsConstructor
public class KoOtoEvrakDurum {

    @Id
    @Column(name = "TALEP_NO")
    private String krediNumarasi;

    @Id
    @Column(name = "EVRAK_KODU")
    private String evrakKodu;

    @Column(name = "DURUM")
    private Integer rehinDurum;
}