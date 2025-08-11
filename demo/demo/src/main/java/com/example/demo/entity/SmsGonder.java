package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity

@Table(name = "SMS_GONDER")
@Getter
@Setter
public class SmsGonder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "MESSAGE_BODY")
    private String messageBody;

    @Column(name = "INSERT_DATE")
    private LocalDateTime insertDate;

    @Column(name = "SMS_KOD")
    private String smsKod;

    @Column(name = "GONDERILEN_PROG")
    private String gonderilenProg;
}