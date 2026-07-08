package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.CategoriaRequest;
import com.govflow.gov.flow.dto.response.CategoriaResponse;
import com.govflow.gov.flow.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> salvar(@Valid @RequestBody CategoriaRequest request){
        CategoriaResponse response = categoriaService.salvar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar(){
        return ResponseEntity.ok(categoriaService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request){
        return ResponseEntity.ok(categoriaService.atualizar(id,request));

    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(
            @PathVariable Long id){

        return ResponseEntity.ok(categoriaService.buscarPorId(id));

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        categoriaService.deletar(id);

        return ResponseEntity.noContent().build();
    }





    }

