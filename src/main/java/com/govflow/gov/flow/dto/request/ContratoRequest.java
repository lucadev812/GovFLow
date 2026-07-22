package com.govflow.gov.flow.dto.request;

import com.govflow.gov.flow.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoRequest {


    @NotBlank(message = "o numero do contrato e obrigatorio")
    private String numero;

    @NotNull(message = "o valor e obirgatorio")
    @Positive(message = "o valor deve ser mauir que zero")
    private BigDecimal valor;

    @NotNull(message = "a data de inicio e obrigatoria")
    private LocalDate dataIinicio;

    @NotNull(message = "a data final e obrigatoria")
    private LocalDate dataFim;

    @NotNull(message = "o status e obrigatorio")
    private Status status;


    @NotNull(message="a categoria e obrigatoria")
    private Long categoriaId;

    @NotNull(message = " O fornecedor e obrigatorio")
    private Long fornecedorId;

}
