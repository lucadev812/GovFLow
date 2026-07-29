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
    @Size(max = 100, message = "o nome de ter no maximo 100 caracteres")
    private String nome;

    @NotBlank(message = "descricao obrigatoria")
    @Size(max = 250, message = "a descricao deve ter no maximo 250 carcteres ")
    private String descricao;


}
