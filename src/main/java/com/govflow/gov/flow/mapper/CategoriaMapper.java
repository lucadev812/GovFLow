package com.govflow.gov.flow.mapper;

import com.govflow.gov.flow.dto.request.CategoriaRequest;
import com.govflow.gov.flow.dto.response.CategoriaResponse;
import com.govflow.gov.flow.entity.Categoria;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaMapper {


  public  Categoria toEntity(CategoriaRequest categoriaRequest) {

        Categoria categoria = new Categoria();
        categoria.setNome(categoriaRequest.getNome());
        categoria.setDescricao(categoriaRequest.getDescricao());

        return categoria;

    }

  public  CategoriaResponse toResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .build();
    }
    public List<CategoriaResponse> toResponseList(List<Categoria> categorias) {
        return categorias.stream()
                .map(this::toResponse)
                .toList();
    }
}

