package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.UsuarioRequest;
import com.govflow.gov.flow.dto.response.UsuarioResponse;
import com.govflow.gov.flow.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(
        name = "Usuários",
        description = "Gerenciamento dos usuários do sistema"
)
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cadastra um novo usuário com perfil ADMIN ou GESTOR"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso"
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
                    description = "Acesso permitido somente para administradores"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado"
            )
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioRequest usuarioRequest){

        UsuarioResponse response = usuarioService.salvar(usuarioRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários cadastrados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuários listados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso permitido somente para administradores"
            )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar(){
        return ResponseEntity.ok(usuarioService.listar());

    }
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso permitido somente para administradores"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id){

        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso"
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
                    description = "Acesso permitido somente para administradores"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id,@Valid @RequestBody UsuarioRequest usuarioRequest){

        return ResponseEntity.ok(usuarioService.atualizar(id, usuarioRequest));
    }
    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso permitido somente para administradores"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
