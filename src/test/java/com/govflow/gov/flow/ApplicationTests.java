package com.govflow.gov.flow;

import com.govflow.gov.flow.service.CategoriaService;
import org.junit.jupiter.api.Test;
import com.govflow.gov.flow.dto.request.CategoriaRequest;
import com.govflow.gov.flow.dto.response.CategoriaResponse;
import com.govflow.gov.flow.entity.Categoria;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.CategoriaMapper;
import com.govflow.gov.flow.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
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
	class CategoriaServiceTest {

		@Mock
		private CategoriaRepository categoriaRepository;

		@Mock
		private CategoriaMapper categoriaMapper;

		@InjectMocks
		private CategoriaService categoriaService;

		private Categoria categoria;
		private CategoriaRequest categoriaRequest;
		private CategoriaResponse categoriaResponse;

		@BeforeEach
		void setUp() {
			categoria = new Categoria();
			categoria.setId(1L);
			categoria.setNome("Tecnologia");
			categoria.setDescricao("Contratos de tecnologia");

			categoriaRequest = new CategoriaRequest();
			categoriaRequest.setNome("Tecnologia");
			categoriaRequest.setDescricao("Contratos de tecnologia");

			categoriaResponse = new CategoriaResponse();
			categoriaResponse.setId(1L);
			categoriaResponse.setNome("Tecnologia");
			categoriaResponse.setDescricao("Contratos de tecnologia");
		}

		@Test
		void deveSalvarCategoriaComSucesso() {
			when(categoriaMapper.toEntity(categoriaRequest)).thenReturn(categoria);
			when(categoriaRepository.save(categoria)).thenReturn(categoria);
			when(categoriaMapper.toResponse(categoria)).thenReturn(categoriaResponse);

			CategoriaResponse resultado = categoriaService.salvar(categoriaRequest);

			assertEquals(1L, resultado.getId());
			assertEquals("Tecnologia", resultado.getNome());

			verify(categoriaMapper).toEntity(categoriaRequest);
			verify(categoriaRepository).save(categoria);
			verify(categoriaMapper).toResponse(categoria);
		}

		@Test
		void deveListarCategoriasComSucesso() {
			List<Categoria> categorias = List.of(categoria);
			List<CategoriaResponse> respostas = List.of(categoriaResponse);

			when(categoriaRepository.findAll()).thenReturn(categorias);
			when(categoriaMapper.toResponseList(categorias)).thenReturn(respostas);

			List<CategoriaResponse> resultado = categoriaService.listar();

			assertEquals(1, resultado.size());
			assertEquals("Tecnologia", resultado.getFirst().getNome());

			verify(categoriaRepository).findAll();
			verify(categoriaMapper).toResponseList(categorias);
		}

		@Test
		void deveBuscarCategoriaPorIdComSucesso() {
			when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
			when(categoriaMapper.toResponse(categoria)).thenReturn(categoriaResponse);

			CategoriaResponse resultado = categoriaService.buscarPorId(1L);

			assertEquals(1L, resultado.getId());
			assertEquals("Tecnologia", resultado.getNome());

			verify(categoriaRepository).findById(1L);
			verify(categoriaMapper).toResponse(categoria);
		}

		@Test
		void deveLancarExcecaoAoBuscarCategoriaInexistente() {
			when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

			ResourceNotFoundException exception = assertThrows(
					ResourceNotFoundException.class,
					() -> categoriaService.buscarPorId(99L)
			);

			assertEquals("categoria nao encontrada", exception.getMessage());

			verify(categoriaRepository).findById(99L);
			verifyNoInteractions(categoriaMapper);
		}

		@Test
		void deveAtualizarCategoriaComSucesso() {
			CategoriaRequest requestAtualizacao = new CategoriaRequest();
			requestAtualizacao.setNome("Serviços");
			requestAtualizacao.setDescricao("Contratos de serviços");

			CategoriaResponse responseAtualizada = new CategoriaResponse();
			responseAtualizada.setId(1L);
			responseAtualizada.setNome("Serviços");
			responseAtualizada.setDescricao("Contratos de serviços");

			when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
			when(categoriaRepository.save(categoria)).thenReturn(categoria);
			when(categoriaMapper.toResponse(categoria)).thenReturn(responseAtualizada);

			CategoriaResponse resultado = categoriaService.atualizar(1L, requestAtualizacao);

			assertEquals("Serviços", categoria.getNome());
			assertEquals("Contratos de serviços", categoria.getDescricao());
			assertEquals("Serviços", resultado.getNome());

			verify(categoriaRepository).findById(1L);
			verify(categoriaRepository).save(categoria);
			verify(categoriaMapper).toResponse(categoria);
		}

		@Test
		void deveLancarExcecaoAoAtualizarCategoriaInexistente() {
			when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

			ResourceNotFoundException exception = assertThrows(
					ResourceNotFoundException.class,
					() -> categoriaService.atualizar(99L, categoriaRequest)
			);

			assertEquals("categoria nao encontrada", exception.getMessage());

			verify(categoriaRepository).findById(99L);
			verify(categoriaRepository, never()).save(any());
			verifyNoInteractions(categoriaMapper);
		}

		@Test
		void deveDeletarCategoriaComSucesso() {
			when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

			categoriaService.deletar(1L);

			verify(categoriaRepository).findById(1L);
			verify(categoriaRepository).deleteById(1L);
		}

		@Test
		void deveLancarExcecaoAoDeletarCategoriaInexistente() {
			when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

			ResourceNotFoundException exception = assertThrows(
					ResourceNotFoundException.class,
					() -> categoriaService.deletar(99L)
			);

			assertEquals("categoria nao encontrada", exception.getMessage());

			verify(categoriaRepository).findById(99L);
			verify(categoriaRepository, never()).deleteById(anyLong());
		}
	}

