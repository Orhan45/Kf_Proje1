package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EgmStateInformationId implements Serializable {
    private Long productLineId;
    private Integer stateId;
    private Integer stateStatus;
}