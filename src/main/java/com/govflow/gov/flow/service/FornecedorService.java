package com.govflow.gov.flow.service;

import com.govflow.gov.flow.dto.request.FornecedorRequest;
import com.govflow.gov.flow.dto.response.FornecedorResponse;
import com.govflow.gov.flow.entity.Fornecedor;
import com.govflow.gov.flow.exception.DuplicateResourceException;
import com.govflow.gov.flow.exception.ResourceNotFoundException;
import com.govflow.gov.flow.mapper.FornecedorMapper;
import com.govflow.gov.flow.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository repository;
    private final FornecedorMapper mapper;

    public FornecedorResponse salvar(FornecedorRequest request){

        validarCnpj(request.getCnpj());
        validarCnpj(request.getEmail());

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

        return mapper.toResponse(fornecedor);
    }

    public void  deletar(@PathVariable Long id){
        repository.delete(buscarFornecedor(id));
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
