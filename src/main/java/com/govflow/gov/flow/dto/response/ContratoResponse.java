package com.govflow.gov.flow.dto.response;


import com.govflow.gov.flow.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContratoResponse {

    private Long id;

    private String numero;

    private BigDecimal valor;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Status status;

    private Long categoriaId;

    private String categoriaNome;

    private Long fornecedorId;

    private String fornecedorNome;



}
