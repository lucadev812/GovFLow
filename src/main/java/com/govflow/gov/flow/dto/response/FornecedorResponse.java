package com.govflow.gov.flow.dto.response;

import com.govflow.gov.flow.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorResponse {

    private Long id;

    private String nome;

    private String cnpj;

    private String email;

    private Status status;
}
