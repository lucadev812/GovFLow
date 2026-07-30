package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.FornecedorRequest;
import com.govflow.gov.flow.dto.response.FornecedorResponse;
import com.govflow.gov.flow.entity.Fornecedor;
import com.govflow.gov.flow.enums.Status;
import com.govflow.gov.flow.exception.DuplicateResourceException;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.FornecedorMapper;
import com.govflow.gov.flow.repository.FornecedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository repository;

    @Mock
    private FornecedorMapper mapper;

    @InjectMocks
    private FornecedorService fornecedorService;

    private Fornecedor fornecedor;
    private FornecedorRequest request;
    private FornecedorResponse response;

    @BeforeEach
    void setUp() {
        fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNome("Fornecedor Teste");
        fornecedor.setCnpj("12345678000199");
        fornecedor.setEmail("fornecedor@email.com");
        fornecedor.setStatus(Status.ATIVO);

        request = new FornecedorRequest();
        request.setNome("Fornecedor Teste");
        request.setCnpj("12345678000199");
        request.setEmail("fornecedor@email.com");
        request.setStatus(Status.ATIVO);

        response = new FornecedorResponse();
        response.setId(1L);
        response.setNome("Fornecedor Teste");
        response.setCnpj("12345678000199");
        response.setEmail("fornecedor@email.com");
        response.setStatus(Status.ATIVO);
    }

    @Test
    void deveSalvarFornecedorComSucesso() {
        when(repository.existsByCnpj(request.getCnpj())).thenReturn(false);
        when(repository.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(fornecedor);
        when(repository.save(fornecedor)).thenReturn(fornecedor);
        when(mapper.toResponse(fornecedor)).thenReturn(response);

        FornecedorResponse resultado = fornecedorService.salvar(request);

        assertEquals(1L, resultado.getId());
        assertEquals("Fornecedor Teste", resultado.getNome());
        assertEquals("12345678000199", resultado.getCnpj());

        verify(repository).existsByCnpj(request.getCnpj());
        verify(repository).existsByEmail(request.getEmail());
        verify(mapper).toEntity(request);
        verify(repository).save(fornecedor);
        verify(mapper).toResponse(fornecedor);
    }

    @Test
    void deveLancarExcecaoAoSalvarFornecedorComCnpjDuplicado() {
        when(repository.existsByCnpj(request.getCnpj())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> fornecedorService.salvar(request)
        );

        assertEquals(
                "ja existe um fornecedor com este CNPJ",
                exception.getMessage()
        );

        verify(repository).existsByCnpj(request.getCnpj());
        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoAoSalvarFornecedorComEmailDuplicado() {
        when(repository.existsByCnpj(request.getCnpj())).thenReturn(false);
        when(repository.existsByEmail(request.getEmail())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> fornecedorService.salvar(request)
        );

        assertEquals(
                "ja existe um fornecedor com este e-mail",
                exception.getMessage()
        );

        verify(repository).existsByCnpj(request.getCnpj());
        verify(repository).existsByEmail(request.getEmail());
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveListarFornecedoresComSucesso() {
        List<Fornecedor> fornecedores = List.of(fornecedor);
        List<FornecedorResponse> responses = List.of(response);

        when(repository.findAll()).thenReturn(fornecedores);
        when(mapper.toResponseList(fornecedores)).thenReturn(responses);

        List<FornecedorResponse> resultado = fornecedorService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Fornecedor Teste", resultado.getFirst().getNome());

        verify(repository).findAll();
        verify(mapper).toResponseList(fornecedores);
    }

    @Test
    void deveBuscarFornecedorPorIdComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(mapper.toResponse(fornecedor)).thenReturn(response);

        FornecedorResponse resultado =
                fornecedorService.buscarFornecedorPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("fornecedor@email.com", resultado.getEmail());

        verify(repository).findById(1L);
        verify(mapper).toResponse(fornecedor);
    }

    @Test
    void deveLancarExcecaoAoBuscarFornecedorInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> fornecedorService.buscarFornecedorPorId(99L)
        );

        assertEquals(
                "fornecedor nao encontrado",
                exception.getMessage()
        );

        verify(repository).findById(99L);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveAtualizarFornecedorComSucesso() {
        FornecedorRequest requestAtualizacao = new FornecedorRequest();
        requestAtualizacao.setNome("Fornecedor Atualizado");
        requestAtualizacao.setCnpj("98765432000188");
        requestAtualizacao.setEmail("atualizado@email.com");
        requestAtualizacao.setStatus(Status.INATIVO);

        FornecedorResponse responseAtualizada = new FornecedorResponse();
        responseAtualizada.setId(1L);
        responseAtualizada.setNome("Fornecedor Atualizado");
        responseAtualizada.setCnpj("98765432000188");
        responseAtualizada.setEmail("atualizado@email.com");
        responseAtualizada.setStatus(Status.INATIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(repository.existsByCnpj("98765432000188")).thenReturn(false);
        when(repository.existsByEmail("atualizado@email.com")).thenReturn(false);
        when(repository.save(fornecedor)).thenReturn(fornecedor);
        when(mapper.toResponse(fornecedor)).thenReturn(responseAtualizada);

        FornecedorResponse resultado =
                fornecedorService.atualizar(1L, requestAtualizacao);

        assertEquals("Fornecedor Atualizado", fornecedor.getNome());
        assertEquals("98765432000188", fornecedor.getCnpj());
        assertEquals("atualizado@email.com", fornecedor.getEmail());
        assertEquals(Status.INATIVO, fornecedor.getStatus());
        assertEquals("Fornecedor Atualizado", resultado.getNome());

        verify(repository).findById(1L);
        verify(repository).existsByCnpj("98765432000188");
        verify(repository).existsByEmail("atualizado@email.com");
        verify(repository).save(fornecedor);
        verify(mapper).toResponse(fornecedor);
    }

    @Test
    void naoDeveValidarCnpjNemEmailQuandoNaoForemAlterados() {
        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(repository.save(fornecedor)).thenReturn(fornecedor);
        when(mapper.toResponse(fornecedor)).thenReturn(response);

        fornecedorService.atualizar(1L, request);

        verify(repository).findById(1L);
        verify(repository, never()).existsByCnpj(anyString());
        verify(repository, never()).existsByEmail(anyString());
        verify(repository).save(fornecedor);
        verify(mapper).toResponse(fornecedor);
    }

    @Test
    void deveLancarExcecaoAoAtualizarComCnpjDuplicado() {
        FornecedorRequest requestAtualizacao = new FornecedorRequest();
        requestAtualizacao.setNome("Outro fornecedor");
        requestAtualizacao.setCnpj("98765432000188");
        requestAtualizacao.setEmail("fornecedor@email.com");
        requestAtualizacao.setStatus(Status.ATIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(repository.existsByCnpj("98765432000188")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> fornecedorService.atualizar(1L, requestAtualizacao)
        );

        assertEquals(
                "ja existe um fornecedor com este CNPJ",
                exception.getMessage()
        );

        verify(repository).findById(1L);
        verify(repository).existsByCnpj("98765432000188");
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoAoAtualizarComEmailDuplicado() {
        FornecedorRequest requestAtualizacao = new FornecedorRequest();
        requestAtualizacao.setNome("Outro fornecedor");
        requestAtualizacao.setCnpj("12345678000199");
        requestAtualizacao.setEmail("duplicado@email.com");
        requestAtualizacao.setStatus(Status.ATIVO);

        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(repository.existsByEmail("duplicado@email.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> fornecedorService.atualizar(1L, requestAtualizacao)
        );

        assertEquals(
                "ja existe um fornecedor com este e-mail",
                exception.getMessage()
        );

        verify(repository).findById(1L);
        verify(repository).existsByEmail("duplicado@email.com");
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoAoAtualizarFornecedorInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> fornecedorService.atualizar(99L, request)
        );

        assertEquals(
                "fornecedor nao encontrado",
                exception.getMessage()
        );

        verify(repository).findById(99L);
        verify(repository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveDeletarFornecedorComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(fornecedor));

        fornecedorService.deletar(1L);

        verify(repository).findById(1L);
        verify(repository).delete(fornecedor);
    }

    @Test
    void deveLancarExcecaoAoDeletarFornecedorInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> fornecedorService.deletar(99L)
        );

        assertEquals(
                "fornecedor nao encontrado",
                exception.getMessage()
        );

        verify(repository).findById(99L);
        verify(repository, never()).delete(any());
    }
}