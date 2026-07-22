package com.govflow.gov.flow.mapper;

import com.govflow.gov.flow.dto.request.ContratoRequest;
import com.govflow.gov.flow.dto.response.ContratoResponse;
import com.govflow.gov.flow.entity.Contrato;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContratoMapper {

    public Contrato toEntity(ContratoRequest request) {

        Contrato contrato = new Contrato();

        contrato.setNumero(request.getNumero());
        contrato.setValor(request.getValor());
        contrato.setDataInicio(request.getDataIinicio());
        contrato.setDataFim(request.getDataFim());
        contrato.setStatus(request.getStatus());

        return contrato;

    }


    public ContratoResponse toResponse(Contrato contrato) {
        return ContratoResponse.builder()
                .id(contrato.getId())
                .numero(contrato.getNumero())
                .valor(contrato.getValor())
                .dataInicio(contrato.getDataInicio())
                .dataFim(contrato.getDataFim())
                .status(contrato.getStatus())

                .categoriaId(contrato.getCategoria().getId())
                .categoriaNome(contrato.getCategoria().getNome())

                .fornecedorId(contrato.getFornecedor().getId())
                .fornecedorNome(contrato.getFornecedor().getNome())
                .build();

    }

    public List<ContratoResponse>  toResponseList(List<Contrato> contratos){
        return contratos.stream()
                .map(this::toResponse)
                .toList();

    }

}
