package com.govflow.gov.flow.dto.request;

import com.govflow.gov.flow.enums.Status;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorRequest {

    @NotBlank(message = "O nome e obrigatorio")
    @Size(max = 100, message = "o nome deve ter no maximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "o CNPJ e obrigatorio")
    @Pattern(
            regexp = "\\d{14}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
            message = "CNPJ obrigatorio."

    )
    private String cnpj;

    @NotBlank(message = "o e-mail e obrigatorio")
    @Email(message = "e-mail invalido")
    @Size(max = 100)
    private String email;

    @NotNull(message = "status obriagtorio")
    private Status status;

}
