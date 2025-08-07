package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "KO_GUN_KAPAMA")
public class KoGunKapama {

    @Id
    @Column(name = "KAPANAN_GUN", nullable = false)
    private LocalDate tarih;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PROCESS_DATE")
    private LocalDateTime processDate;

    public LocalDate getTarih() {
        return tarih;
    }
    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public LocalDateTime getProcessDate() {
        return processDate;
    }
    public void setProcessDate(LocalDateTime processDate) {
        this.processDate = processDate;
    }
}