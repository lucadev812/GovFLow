package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.CategoriaRequest;
import com.govflow.gov.flow.dto.response.CategoriaResponse;
import com.govflow.gov.flow.entity.Categoria;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.CategoriaMapper;
import com.govflow.gov.flow.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;


    public CategoriaResponse salvar(CategoriaRequest request){
        Categoria categoria = categoriaMapper.toEntity(request);

        categoria = categoriaRepository.save(categoria);


        return categoriaMapper.toResponse(categoria);
    }

    public List<CategoriaResponse> listar(CategoriaRequest request){
        return categoriaMapper.toResponseList(categoriaRepository.findAll());
    }

    public CategoriaResponse buscarPorId(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("categoria nao encontrada"));
        return categoriaMapper.toResponse(categoria);
    }

    public CategoriaResponse atualizar(Long id,  CategoriaRequest request){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("categoria nao encontrada"));

        categoria.setNome(request.getNome());
        categoria.setDescricao(request.getDescricao());

        categoriaRepository.save(categoria);

        return categoriaMapper.toResponse(categoria);


        }
    public void deletar(Long id){

        Categoria categoria = categoriaRepository.findById(id)
                        .orElseThrow(()-> new ResourceNotFoundException("categoria nao encontrada"));

        categoriaRepository.deleteById(id);

    }



}
