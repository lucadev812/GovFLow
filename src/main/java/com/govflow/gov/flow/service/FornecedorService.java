package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.FornecedorRequest;
import com.govflow.gov.flow.dto.response.FornecedorResponse;
import com.govflow.gov.flow.entity.Fornecedor;
import com.govflow.gov.flow.exception.BusinessException;
import com.govflow.gov.flow.exception.DuplicateResourceException;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.FornecedorMapper;
import com.govflow.gov.flow.repository.ContratoRepository;
import com.govflow.gov.flow.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository repository;
    private final FornecedorMapper mapper;
    private final ContratoRepository contratoRepository;

    public FornecedorResponse salvar(FornecedorRequest request){

        validarCnpj(request.getCnpj());
        validaEmail(request.getEmail());

        Fornecedor  fornecedor = mapper.toEntity(request);
        fornecedor = repository.save(fornecedor);

        return mapper.toResponse(fornecedor);
    }

    public List<FornecedorResponse> listar(){
        return mapper.toResponseList(repository.findAll());
    }

    public FornecedorResponse buscarFornecedorPorId(Long id){
        return mapper.toResponse(buscarFornecedor(id));
    }


    public FornecedorResponse atualizar(Long id, FornecedorRequest request){
        Fornecedor fornecedor = buscarFornecedor(id);

        if (!fornecedor.getCnpj().equals(request.getCnpj())) {
            validarCnpj(request.getCnpj());
        }

        if(!fornecedor.getEmail().equals(request.getEmail())){
            validaEmail(request.getEmail());
        }

        fornecedor.setNome(request.getNome());
        fornecedor.setCnpj(request.getCnpj());
        fornecedor.setEmail(request.getEmail());
        fornecedor.setStatus(request.getStatus());

        fornecedor = repository.save(fornecedor);

        return mapper.toResponse(fornecedor);
    }

    public void deletar(Long id) {
        Fornecedor fornecedor = buscarFornecedor(id);

        if (contratoRepository.existsByFornecedor_Id(id)) {
            throw new BusinessException(
                    "O fornecedor não pode ser excluído porque possui contratos vinculados"
            );
        }

        repository.delete(fornecedor);
    }



    private Fornecedor buscarFornecedor(Long id){
            return repository.findById(id)
                    .orElseThrow(()->
                            new ResourceNotFoundException("fornecedor nao encontrado"));
    }


    private void validarCnpj(String cnpj){
        if (repository.existsByCnpj(cnpj)){
            throw new DuplicateResourceException("ja existe um fornecedor com este CNPJ");
        }
    }


    private void validaEmail(String email){
        if (repository.existsByEmail(email)){
            throw new DuplicateResourceException("ja existe um fornecedor com este e-mail");
        }
    }
}
