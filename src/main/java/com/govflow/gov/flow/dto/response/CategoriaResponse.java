package com.govflow.gov.flow.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaResponse {
    private Long id;
    private String nome;
    private String descricao;


}
