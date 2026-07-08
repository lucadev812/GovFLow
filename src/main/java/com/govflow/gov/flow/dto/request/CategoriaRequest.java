package com.govflow.gov.flow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "descricao ")
    @Size(max = 250)
    private String descricao;



}
