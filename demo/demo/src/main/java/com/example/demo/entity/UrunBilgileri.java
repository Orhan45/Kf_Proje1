package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "dbo", name = "URUN_BILGILERI")
@IdClass(UrunBilgileriId.class) // Burayı ekledik
public class UrunBilgileri {

    @Id
    @Column(name = "kredi_numarasi")
    private String krediNumarasi;

    @Id
    @Column(name = "sira")
    private Integer sira;

    @Column(name = "rehin_durum")
    private Integer rehinDurum;

    @Column(name = "product_line_id")
    private Integer productLineId;
}