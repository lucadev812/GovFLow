package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.ContratoRequest;
import com.govflow.gov.flow.dto.response.ContratoResponse;
import com.govflow.gov.flow.entity.Categoria;
import com.govflow.gov.flow.entity.Contrato;
import com.govflow.gov.flow.entity.Fornecedor;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.ContratoMapper;
import com.govflow.gov.flow.repository.CategoriaRepository;
import com.govflow.gov.flow.repository.ContratoRepository;
import com.govflow.gov.flow.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ContratoMapper mapper;

    public ContratoResponse salvar (ContratoRequest request) {

        validarDatas(request);
        validarValor(request);

        Categoria categoria = buscarCategoria(request.getCategoriaId());

        Fornecedor fornecedor = buscarFornecedor(request.getFornecedorId());

        Contrato contrato = mapper.toEntity(request);

        contrato.setCategoria(categoria);
        contrato.setFornecedor(fornecedor);
        contrato = contratoRepository.save(contrato);
        return mapper.toResponse(contrato);

    }



    public List<ContratoResponse> listar(){
        List<Contrato> contratos = contratoRepository.findAll();
        return mapper.toResponseList(contratos);
    }

    private Contrato buscarContrato(Long id){
        return contratoRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("contrato nao encontrado"));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("categoria nao encontrada"));
    }


    public  ContratoResponse atualizar (Long id,  ContratoRequest request){
        validarDatas(request);
        validarValor(request);

        Contrato contrato = buscarContrato(id);

        Categoria categoria = buscarCategoria(request.getCategoriaId());

        Fornecedor fornecedor = buscarFornecedor(request.getFornecedorId());

        contrato.setNumero(request.getNumero());
        contrato.setValor(request.getValor());
        contrato.setDataInicio(request.getDataIinicio());
        contrato.setDataFim(request.getDataFim());
        contrato.setStatus(request.getStatus());
        contrato.setCategoria(categoria);
        contrato.setFornecedor(fornecedor);

        contrato =  contratoRepository.save(contrato);

        return mapper.toResponse(contrato);
    }



    public void deletar(Long id){
        Contrato contrato = buscarContrato(id);

        contratoRepository.delete(contrato);
    }


    private Fornecedor buscarFornecedor(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("fornecedor nao encontrada"));
    }

    private  void validarDatas(ContratoRequest request) {
        if (request.getDataFim().isBefore(request.getDataIinicio())){
            throw  new IllegalArgumentException( " A data de termino nao pode ser anterior a data de inicio");
        }
    }

    private void validarValor(ContratoRequest request) {
        if (request.getValor().compareTo(BigDecimal.ZERO)<= 0 ){
            throw  new IllegalArgumentException( " Valor nao pode ser negativo");
        }
    }

}




