package com.govflow.gov.flow.dto.response;

import com.govflow.gov.flow.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private Role role;


}
