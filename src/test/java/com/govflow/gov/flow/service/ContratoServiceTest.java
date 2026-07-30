package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.ContratoRequest;
import com.govflow.gov.flow.dto.response.ContratoResponse;
import com.govflow.gov.flow.entity.Categoria;
import com.govflow.gov.flow.entity.Contrato;
import com.govflow.gov.flow.entity.Fornecedor;
import com.govflow.gov.flow.enums.Status;
import com.govflow.gov.flow.exception.BusinessException;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.ContratoMapper;
import com.govflow.gov.flow.repository.CategoriaRepository;
import com.govflow.gov.flow.repository.ContratoRepository;
import com.govflow.gov.flow.repository.FornecedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratoServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ContratoMapper mapper;

    @InjectMocks
    private ContratoService contratoService;

    private Contrato contrato;
    private ContratoRequest request;
    private ContratoResponse response;
    private Categoria categoria;
    private Fornecedor fornecedor;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Tecnologia");
        categoria.setDescricao("Contratos de tecnologia");

        fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNome("Fornecedor XPTO");
        fornecedor.setCnpj("12345678000199");
        fornecedor.setEmail("fornecedor@email.com");
        fornecedor.setStatus(Status.ATIVO);

        contrato = new Contrato();
        contrato.setId(1L);
        contrato.setNumero("CT-001");
        contrato.setValor(new BigDecimal("10000.00"));
        contrato.setDataInicio(LocalDate.of(2026, 7, 1));
        contrato.setDataFim(LocalDate.of(2026, 12, 31));
        contrato.setStatus(Status.ATIVO);
        contrato.setCategoria(categoria);
        contrato.setFornecedor(fornecedor);

        request = ContratoRequest.builder()
                .numero("CT-001")
                .valor(new BigDecimal("10000.00"))
                .dataInicio(LocalDate.of(2026, 7, 1))
                .dataFim(LocalDate.of(2026, 12, 31))
                .status(Status.ATIVO)
                .categoriaId(1L)
                .fornecedorId(1L)
                .build();

        response = ContratoResponse.builder()
                .id(1L)
                .numero("CT-001")
                .valor(new BigDecimal("10000.00"))
                .dataInicio(LocalDate.of(2026, 7, 1))
                .dataFim(LocalDate.of(2026, 12, 31))
                .status(Status.ATIVO)
                .categoriaId(1L)
                .categoriaNome("Tecnologia")
                .fornecedorId(1L)
                .fornecedorNome("Fornecedor XPTO")
                .build();
    }

    @Test
    void deveSalvarContratoComSucesso() {
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.of(fornecedor));

        when(mapper.toEntity(request))
                .thenReturn(contrato);

        when(contratoRepository.save(contrato))
                .thenReturn(contrato);

        when(mapper.toResponse(contrato))
                .thenReturn(response);

        ContratoResponse resultado = contratoService.salvar(request);

        assertEquals(1L, resultado.getId());
        assertEquals("CT-001", resultado.getNumero());
        assertEquals(new BigDecimal("10000.00"), resultado.getValor());
        assertEquals("Tecnologia", resultado.getCategoriaNome());
        assertEquals("Fornecedor XPTO", resultado.getFornecedorNome());

        verify(categoriaRepository).findById(1L);
        verify(fornecedorRepository).findById(1L);
        verify(mapper).toEntity(request);
        verify(contratoRepository).save(contrato);
        verify(mapper).toResponse(contrato);
    }

    @Test
    void deveListarContratosComSucesso() {
        List<Contrato> contratos = List.of(contrato);
        List<ContratoResponse> responses = List.of(response);

        when(contratoRepository.findAll())
                .thenReturn(contratos);

        when(mapper.toResponseList(contratos))
                .thenReturn(responses);

        List<ContratoResponse> resultado = contratoService.listar();

        assertEquals(1, resultado.size());
        assertEquals("CT-001", resultado.getFirst().getNumero());

        verify(contratoRepository).findAll();
        verify(mapper).toResponseList(contratos);
    }

    @Test
    void deveBuscarContratoPorIdComSucesso() {
        when(contratoRepository.findById(1L))
                .thenReturn(Optional.of(contrato));

        when(mapper.toResponse(contrato))
                .thenReturn(response);

        ContratoResponse resultado = contratoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("CT-001", resultado.getNumero());

        verify(contratoRepository).findById(1L);
        verify(mapper).toResponse(contrato);
    }

    @Test
    void deveLancarExcecaoAoBuscarContratoInexistente() {
        when(contratoRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> contratoService.buscarPorId(99L)
        );

        assertEquals("contrato nao encontrado", exception.getMessage());

        verify(contratoRepository).findById(99L);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveAtualizarContratoComSucesso() {
        ContratoRequest requestAtualizacao = ContratoRequest.builder()
                .numero("CT-002")
                .valor(new BigDecimal("20000.00"))
                .dataInicio(LocalDate.of(2026, 8, 1))
                .dataFim(LocalDate.of(2027, 1, 31))
                .status(Status.SUSPENSO)
                .categoriaId(1L)
                .fornecedorId(1L)
                .build();

        ContratoResponse responseAtualizada = ContratoResponse.builder()
                .id(1L)
                .numero("CT-002")
                .valor(new BigDecimal("20000.00"))
                .dataInicio(LocalDate.of(2026, 8, 1))
                .dataFim(LocalDate.of(2027, 1, 31))
                .status(Status.SUSPENSO)
                .categoriaId(1L)
                .categoriaNome("Tecnologia")
                .fornecedorId(1L)
                .fornecedorNome("Fornecedor XPTO")
                .build();

        when(contratoRepository.findById(1L))
                .thenReturn(Optional.of(contrato));

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.of(fornecedor));

        when(contratoRepository.save(contrato))
                .thenReturn(contrato);

        when(mapper.toResponse(contrato))
                .thenReturn(responseAtualizada);

        ContratoResponse resultado =
                contratoService.atualizar(1L, requestAtualizacao);

        assertEquals("CT-002", contrato.getNumero());
        assertEquals(new BigDecimal("20000.00"), contrato.getValor());
        assertEquals(LocalDate.of(2026, 8, 1), contrato.getDataInicio());
        assertEquals(LocalDate.of(2027, 1, 31), contrato.getDataFim());
        assertEquals(Status.SUSPENSO, contrato.getStatus());
        assertEquals(categoria, contrato.getCategoria());
        assertEquals(fornecedor, contrato.getFornecedor());
        assertEquals("CT-002", resultado.getNumero());

        verify(contratoRepository).findById(1L);
        verify(categoriaRepository).findById(1L);
        verify(fornecedorRepository).findById(1L);
        verify(contratoRepository).save(contrato);
        verify(mapper).toResponse(contrato);
    }

    @Test
    void deveDeletarContratoComSucesso() {
        when(contratoRepository.findById(1L))
                .thenReturn(Optional.of(contrato));

        contratoService.deletar(1L);

        verify(contratoRepository).findById(1L);
        verify(contratoRepository).delete(contrato);
    }

    @Test
    void deveLancarExcecaoAoDeletarContratoInexistente() {
        when(contratoRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> contratoService.deletar(99L)
        );

        assertEquals("contrato nao encontrado", exception.getMessage());

        verify(contratoRepository).findById(99L);
        verify(contratoRepository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoAoSalvarComCategoriaInexistente() {
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> contratoService.salvar(request)
        );

        assertEquals("categoria nao encontrada", exception.getMessage());

        verify(categoriaRepository).findById(1L);
        verifyNoInteractions(fornecedorRepository);
        verify(contratoRepository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoAoSalvarComFornecedorInexistente() {
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> contratoService.salvar(request)
        );

        assertEquals("fornecedor nao encontrado", exception.getMessage());

        verify(categoriaRepository).findById(1L);
        verify(fornecedorRepository).findById(1L);
        verify(contratoRepository, never()).save(any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoQuandoDataFimForAnteriorADataInicio() {
        request.setDataInicio(LocalDate.of(2026, 12, 31));
        request.setDataFim(LocalDate.of(2026, 7, 1));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> contratoService.salvar(request)
        );

        assertEquals(
                "A data de termino nao pode ser anterior a data de inicio",
                exception.getMessage().trim()
        );

        verifyNoInteractions(categoriaRepository);
        verifyNoInteractions(fornecedorRepository);
        verifyNoInteractions(contratoRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoQuandoValorForZero() {
        request.setValor(BigDecimal.ZERO);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> contratoService.salvar(request)
        );

        assertEquals(
                "O valor deve ser maior que zero",
                exception.getMessage().trim()
        );

        verifyNoInteractions(categoriaRepository);
        verifyNoInteractions(fornecedorRepository);
        verifyNoInteractions(contratoRepository);
        verifyNoInteractions(mapper);
    }

    @Test
    void deveLancarExcecaoQuandoValorForNegativo() {
        request.setValor(new BigDecimal("-100.00"));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> contratoService.salvar(request)
        );

        assertEquals(
                "O valor deve ser maior que zero",
                exception.getMessage().trim()
        );

        verifyNoInteractions(categoriaRepository);
        verifyNoInteractions(fornecedorRepository);
        verifyNoInteractions(contratoRepository);
        verifyNoInteractions(mapper);
    }
}