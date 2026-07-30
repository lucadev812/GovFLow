package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.CategoriaRequest;
import com.govflow.gov.flow.dto.response.CategoriaResponse;
import com.govflow.gov.flow.service.CategoriaService;
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
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(
        name = "Categorias",
        description = "Gerenciamento das categorias dos contratos"
)
public class CategoriaController {

    private final CategoriaService categoriaService;


    @Operation(
            summary = "Cadastrar categoria",
            description = "Cadastra uma nova categoria"
    )
    @PostMapping
    public ResponseEntity<CategoriaResponse> salvar(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse response = categoriaService.salvar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Listar categorias")
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(summary = "Atualizar categoria")
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.atualizar(id, request));

    }
    @Operation(summary = "Buscar categoria por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(categoriaService.buscarPorId(id));

    }

    @Operation(summary = "Excluir categoria")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoria excluída"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);

        return ResponseEntity.noContent().build();
    }


}

