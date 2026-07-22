package com.govflow.gov.flow.dto.request;

import com.govflow.gov.flow.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {

    @NotBlank(message = "O nome e obrigatorio")
    private String nome;

    @NotBlank(message = "o e-mail e obrigatorio")
    @Email(message = "E-mail invalido ")
    private String email;

    @NotBlank(message = "A senha e obrigatoria ")
    private String senha;

    @NotNull(message = " A role e obrigatorio")
    private Role role;
}
