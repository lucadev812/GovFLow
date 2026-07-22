package com.govflow.gov.flow.mapper;

import com.govflow.gov.flow.dto.request.UsuarioRequest;
import com.govflow.gov.flow.dto.response.UsuarioResponse;
import com.govflow.gov.flow.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request){

        Usuario usuario = new Usuario();

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setRole(request.getRole());

        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario){
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build();

    }

    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios){
        return usuarios.stream()
                .map(this::toResponse)
                .toList();
    }
}
