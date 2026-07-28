package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.ContratoRequest;
import com.govflow.gov.flow.dto.response.ContratoResponse;
import com.govflow.gov.flow.service.ContratoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contratos" )
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    public ResponseEntity<ContratoResponse> salvar (@Valid @RequestBody ContratoRequest contratoRequest){
        ContratoResponse response = contratoService.salvar(contratoRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ContratoResponse>> listar(){
        return ResponseEntity.ok(contratoService.listar());
    }

    public ResponseEntity<ContratoResponse> buscarPorId(@PathVariable Long id){

        return ResponseEntity.ok(contratoService.buscarPorId(id));


    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponse> buscarContratoPorId(@PathVariable Long id){

        return ResponseEntity.ok(contratoService.buscarPorId(id));


    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponse> atualizar( @PathVariable Long id, @Valid @RequestBody ContratoRequest contratoRequest){
        return ResponseEntity.ok(contratoService.atualizar(id,contratoRequest));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        contratoService.deletar(id);

        return ResponseEntity.noContent().build();
    }










}
