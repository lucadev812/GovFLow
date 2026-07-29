package com.govflow.gov.flow.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @Email(message = "Email invalido")
    @NotBlank(message = "o Email e obrigatorio")
    private String email;

    @NotBlank(message = "A senha e obrigatoria")
    private String senha;

}
