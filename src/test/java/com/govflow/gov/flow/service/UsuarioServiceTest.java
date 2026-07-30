package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.UsuarioRequest;
import com.govflow.gov.flow.dto.response.UsuarioResponse;
import com.govflow.gov.flow.entity.Usuario;
import com.govflow.gov.flow.enums.Role;
import com.govflow.gov.flow.exception.DuplicateResourceException;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.UsuarioMapper;
import com.govflow.gov.flow.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequest request;
    private UsuarioResponse response;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Administrador");
        usuario.setEmail("admin@govflow.com");
        usuario.setSenha("senha-criptografada");
        usuario.setRole(Role.ADMIN);

        request = UsuarioRequest.builder()
                .nome("Administrador")
                .email("admin@govflow.com")
                .senha("12345678")
                .role(Role.ADMIN)
                .build();

        response = UsuarioResponse.builder()
                .id(1L)
                .nome("Administrador")
                .email("admin@govflow.com")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(usuarioMapper.toEntity(request))
                .thenReturn(usuario);

        when(passwordEncoder.encode(request.getSenha()))
                .thenReturn("senha-criptografada");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(response);

        UsuarioResponse resultado = usuarioService.salvar(request);

        assertEquals(1L, resultado.getId());
        assertEquals("Administrador", resultado.getNome());
        assertEquals("admin@govflow.com", resultado.getEmail());
        assertEquals(Role.ADMIN, resultado.getRole());
        assertEquals("senha-criptografada", usuario.getSenha());

        verify(usuarioRepository).existsByEmail(request.getEmail());
        verify(usuarioMapper).toEntity(request);
        verify(passwordEncoder).encode(request.getSenha());
        verify(usuarioRepository).save(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void deveLancarExcecaoAoSalvarUsuarioComEmailDuplicado() {
        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.salvar(request)
        );

        assertEquals(
                "ja existe um usuario cadastrado com este email",
                exception.getMessage()
        );

        verify(usuarioRepository).existsByEmail(request.getEmail());
        verifyNoInteractions(usuarioMapper);
        verifyNoInteractions(passwordEncoder);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveListarUsuariosComSucesso() {
        List<Usuario> usuarios = List.of(usuario);
        List<UsuarioResponse> responses = List.of(response);

        when(usuarioRepository.findAll())
                .thenReturn(usuarios);

        when(usuarioMapper.toResponseList(usuarios))
                .thenReturn(responses);

        List<UsuarioResponse> resultado = usuarioService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Administrador", resultado.getFirst().getNome());

        verify(usuarioRepository).findAll();
        verify(usuarioMapper).toResponseList(usuarios);
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(response);

        UsuarioResponse resultado = usuarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("admin@govflow.com", resultado.getEmail());

        verify(usuarioRepository).findById(1L);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99L)
        );

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(usuarioRepository).findById(99L);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void deveAtualizarUsuarioComSucessoEAlterarSenha() {
        UsuarioRequest requestAtualizacao = UsuarioRequest.builder()
                .nome("Gestor Atualizado")
                .email("gestor@govflow.com")
                .senha("novaSenha123")
                .role(Role.GESTOR)
                .build();

        UsuarioResponse responseAtualizada = UsuarioResponse.builder()
                .id(1L)
                .nome("Gestor Atualizado")
                .email("gestor@govflow.com")
                .role(Role.GESTOR)
                .build();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.existsByEmail("gestor@govflow.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("novaSenha123"))
                .thenReturn("nova-senha-criptografada");

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(responseAtualizada);

        UsuarioResponse resultado =
                usuarioService.atualizar(1L, requestAtualizacao);

        assertEquals("Gestor Atualizado", usuario.getNome());
        assertEquals("gestor@govflow.com", usuario.getEmail());
        assertEquals(Role.GESTOR, usuario.getRole());
        assertEquals("nova-senha-criptografada", usuario.getSenha());
        assertEquals("Gestor Atualizado", resultado.getNome());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).existsByEmail("gestor@govflow.com");
        verify(passwordEncoder).encode("novaSenha123");
        verify(usuarioRepository).save(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void deveAtualizarUsuarioSemAlterarSenhaQuandoSenhaForVazia() {
        String senhaAntiga = usuario.getSenha();

        UsuarioRequest requestAtualizacao = UsuarioRequest.builder()
                .nome("Administrador Atualizado")
                .email("admin@govflow.com")
                .senha("")
                .role(Role.ADMIN)
                .build();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(response);

        usuarioService.atualizar(1L, requestAtualizacao);

        assertEquals(senhaAntiga, usuario.getSenha());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository, never()).existsByEmail(anyString());
        verifyNoInteractions(passwordEncoder);
        verify(usuarioRepository).save(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void deveAtualizarUsuarioSemAlterarSenhaQuandoSenhaForNula() {
        String senhaAntiga = usuario.getSenha();

        UsuarioRequest requestAtualizacao = UsuarioRequest.builder()
                .nome("Administrador Atualizado")
                .email("admin@govflow.com")
                .senha(null)
                .role(Role.ADMIN)
                .build();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(response);

        usuarioService.atualizar(1L, requestAtualizacao);

        assertEquals(senhaAntiga, usuario.getSenha());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository, never()).existsByEmail(anyString());
        verifyNoInteractions(passwordEncoder);
        verify(usuarioRepository).save(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioComEmailDuplicado() {
        UsuarioRequest requestAtualizacao = UsuarioRequest.builder()
                .nome("Outro usuário")
                .email("duplicado@govflow.com")
                .senha("12345678")
                .role(Role.GESTOR)
                .build();

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.existsByEmail("duplicado@govflow.com"))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> usuarioService.atualizar(1L, requestAtualizacao)
        );

        assertEquals(
                "ja existe um usuario cadastrado com este email",
                exception.getMessage()
        );

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).existsByEmail("duplicado@govflow.com");
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.atualizar(99L, request)
        );

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        usuarioService.deletar(1L);

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.deletar(99L)
        );

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).delete(any());
    }
}