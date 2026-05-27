package com.govflow.gov.flow.entity;

import com.govflow.gov.flow.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cnpj;
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;


    @OneToMany(mappedBy = "fornecedor")
    private List<Contrato> contratoes = new ArrayList<>();
}
