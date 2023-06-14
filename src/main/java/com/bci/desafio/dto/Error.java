package com.bci.desafio.dto;

import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Error {

    private Timestamp timestamp;
    private Integer code;
    private String detail;
}
