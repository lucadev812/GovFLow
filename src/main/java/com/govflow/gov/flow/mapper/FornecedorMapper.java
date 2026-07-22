package com.govflow.gov.flow.mapper;

import com.govflow.gov.flow.dto.request.FornecedorRequest;
import com.govflow.gov.flow.dto.response.FornecedorResponse;
import com.govflow.gov.flow.entity.Fornecedor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FornecedorMapper {

    public Fornecedor toEntity( FornecedorRequest request) {
        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setNome(request.getNome());
        fornecedor.setCnpj(request.getCnpj());
        fornecedor.setEmail(request.getEmail());
        fornecedor.setStatus(request.getStatus());

        return fornecedor;
    }

    public FornecedorResponse toResponse(Fornecedor fornecedor) {
        return FornecedorResponse.builder()
                .id(fornecedor.getId())
                .nome(fornecedor.getNome())
                .cnpj(fornecedor.getCnpj())
                .email(fornecedor.getEmail())
                .status(fornecedor.getStatus())
                .build();
    }


    public List<FornecedorResponse> toResponseList(List<Fornecedor> fornecedores) {

        return  fornecedores.stream()
                .map(this::toResponse)
                .toList();
    }
}
