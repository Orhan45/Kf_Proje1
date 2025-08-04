package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Gerekli constructor'lar için eklendi
import lombok.AllArgsConstructor; // Gerekli constructor'lar için eklendi

@Getter
@Setter
@Entity
@Table(schema = "dbo", name = "URUN_BILGILERI")
@IdClass(UrunBilgileriId.class)
@NoArgsConstructor // JPA için varsayılan constructor
@AllArgsConstructor // Tüm alanları içeren constructor (isteğe bağlı ama genellikle faydalı)
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
    private Long productLineId; // Burası Long olarak güncellendi!
}