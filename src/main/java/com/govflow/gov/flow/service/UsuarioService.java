package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.UsuarioRequest;
import com.govflow.gov.flow.dto.response.UsuarioResponse;
import com.govflow.gov.flow.entity.Usuario;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.UsuarioMapper;
import com.govflow.gov.flow.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;


    public UsuarioResponse salvar(UsuarioRequest usuarioRequest) {
        validarEmail(usuarioRequest.getEmail());
        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuario);

    }

    public List<UsuarioResponse> listar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toResponseList(usuarios);
    }

    private Usuario buscarUsuario(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException(
                                "Usuario nao encontrado"
                        ));
    }

    public UsuarioResponse atualizar(Long id,UsuarioRequest usuarioRequest) {

        Usuario usuario = buscarUsuario(id);

        if (!usuario.getEmail().equals(usuarioRequest.getEmail())) {
            validarEmail(usuarioRequest.getEmail());
        }

        usuario.setNome(usuarioRequest.getNome());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setRole(usuarioRequest.getRole());

        if (usuarioRequest.getSenha() != null && !usuarioRequest.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        }

        usuario =usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuarioRepository.delete(usuario);

    }





    private void validarEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw  new IllegalArgumentException("ja existe um usuario cadastrado com este email");
        }
    }


}
