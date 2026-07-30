package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.ContratoRequest;
import com.govflow.gov.flow.dto.response.ContratoResponse;
import com.govflow.gov.flow.service.ContratoService;
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
@RequestMapping("/contratos" )
@RequiredArgsConstructor
@Tag(
        name = "Contratos",
        description = "Gerenciamento dos contratos"
)
public class ContratoController {

    private final ContratoService contratoService;

    @Operation(
            summary = "Cadastrar contrato",
            description = "Cadastra um novo contrato vinculado a uma categoria e a um fornecedor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Contrato cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou regra de negócio violada"
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
                    description = "Categoria ou fornecedor não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Número de contrato já cadastrado"
            )
    })
    @PostMapping
    public ResponseEntity<ContratoResponse> salvar (@Valid @RequestBody ContratoRequest contratoRequest){
        ContratoResponse response = contratoService.salvar(contratoRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Listar contratos",
            description = "Retorna todos os contratos cadastrados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contratos listados com sucesso"
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
    public ResponseEntity<List<ContratoResponse>> listar(){
        return ResponseEntity.ok(contratoService.listar());
    }

    public ResponseEntity<ContratoResponse> buscarPorId(@PathVariable Long id){

        return ResponseEntity.ok(contratoService.buscarPorId(id));


    }
    @Operation(
            summary = "Buscar contrato por ID",
            description = "Retorna os dados de um contrato específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contrato encontrado"
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
                    description = "Contrato não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponse> buscarContratoPorId(@PathVariable Long id){

        return ResponseEntity.ok(contratoService.buscarPorId(id));


    }
    @Operation(
            summary = "Atualizar contrato",
            description = "Atualiza os dados de um contrato existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contrato atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou regra de negócio violada"
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
                    description = "Contrato, categoria ou fornecedor não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Número de contrato já cadastrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponse> atualizar( @PathVariable Long id, @Valid @RequestBody ContratoRequest contratoRequest){
        return ResponseEntity.ok(contratoService.atualizar(id,contratoRequest));

    }
    @Operation(
            summary = "Excluir contrato",
            description = "Remove um contrato pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Contrato excluído com sucesso"
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
                    description = "Contrato não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        contratoService.deletar(id);

        return ResponseEntity.noContent().build();
    }










}
