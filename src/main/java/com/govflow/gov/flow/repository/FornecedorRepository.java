package com.govflow.gov.flow.repository;

import com.govflow.gov.flow.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository  extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findByCnpj(String cnpj);
    Optional<Fornecedor> findByEmail(String email);

    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);

}
