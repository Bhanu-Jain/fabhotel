package com.fabhotel.Eras.applicationlayer;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class EndorsementResponse {
    private double adjustedScore;
    private String adjustmentReason;
}

