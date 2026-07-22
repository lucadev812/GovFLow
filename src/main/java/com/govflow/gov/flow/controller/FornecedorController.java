package com.govflow.gov.flow.controller;

import com.govflow.gov.flow.dto.request.FornecedorRequest;
import com.govflow.gov.flow.dto.response.FornecedorResponse;
import com.govflow.gov.flow.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;


    @PostMapping
    public ResponseEntity<FornecedorResponse> salvar(@Valid @RequestBody FornecedorRequest request) {

        FornecedorResponse response =  fornecedorService.salvar(request);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponse>> listar(){
        return ResponseEntity.ok(fornecedorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId( @PathVariable Long id){

        return ResponseEntity.ok(fornecedorService.buscarFornecedorPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorRequest request ) {
        return ResponseEntity.ok(fornecedorService.atualizar(id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar (@PathVariable Long id){
        fornecedorService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
