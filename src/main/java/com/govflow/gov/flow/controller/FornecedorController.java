package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.FornecedorRequest;
import com.govflow.gov.flow.dto.response.FornecedorResponse;
import com.govflow.gov.flow.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
@Tag(
        name = "Fornecedores",
        description = "Gerenciamento dos fornecedores"
)
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @Operation(
            summary = "Cadastrar fornecedor",
            description = "Cadastra um novo fornecedor utilizando nome, email e CNPJ"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Fornecedor cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ou CNPJ já cadastrado"
            )
    })
    @PostMapping
    public ResponseEntity<FornecedorResponse> salvar(@Valid @RequestBody FornecedorRequest request) {

        FornecedorResponse response = fornecedorService.salvar(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @Operation(
            summary = "Listar fornecedores",
            description = "Retorna todos os fornecedores cadastrados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Fornecedores listados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão"
            )
    })
    @GetMapping
    public ResponseEntity<List<FornecedorResponse>> listar() {
        return ResponseEntity.ok(fornecedorService.listar());
    }


    @Operation(
            summary = "Buscar fornecedor por ID",
            description = "Retorna os dados de um fornecedor específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Fornecedor encontrado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fornecedor não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(fornecedorService.buscarFornecedorPorId(id));
    }


    @Operation(
            summary = "Atualizar fornecedor",
            description = "Atualiza os dados de um fornecedor existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Fornecedor atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fornecedor não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ou CNPJ já cadastrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorRequest request) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, request));
    }
    @Operation(
            summary = "Excluir fornecedor",
            description = "Remove um fornecedor pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Fornecedor excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fornecedor não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Fornecedor vinculado a um contrato"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
