package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Builder; // Builder için import
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // NoArgsConstructor ve AllArgsConstructor da builder ile uyumlu olması için
import lombok.AllArgsConstructor; // eklendi/doğrulandı

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(schema = "dbo", name = "egm_state_information")
@IdClass(EgmStateInformationId.class)
@NoArgsConstructor // Lombok tarafından otomatik constructor oluşturulması için
@AllArgsConstructor // Lombok tarafından otomatik constructor oluşturulması için
@Builder // Yeni! Builder desenini etkinleştirir
public class EgmStateInformation {

    @Id
    @Column(name = "product_line_id")
    private Long productLineId;

    @Id
    @Column(name = "state_id")
    private Integer stateId;

    @Id
    @Column(name = "state_status")
    private Integer stateStatus;

    @Column(name = "message")
    private String message;

    @Column(name = "level_info")
    private String levelInfo;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "process_date")
    private LocalDateTime processDate;
}