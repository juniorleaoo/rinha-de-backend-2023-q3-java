package com.rinhabackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private PessoaCache pessoaCache;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/pessoas")
    public ResponseEntity<Pessoa> incluirPessoa(@RequestBody Pessoa pessoaRequest) {
        if (!pessoaRequest.valido()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        if (pessoaCache.hasApelidoCache(pessoaRequest.getApelido())) {
            return ResponseEntity.unprocessableEntity().build();
        }

        try {
            pessoaRequest.setId(UUID.randomUUID());
            pessoaService.inserir(pessoaRequest);
            pessoaCache.addPessoaCache(pessoaRequest);
            pessoaCache.addApelidoCache(pessoaRequest.getApelido());

            return ResponseEntity.created(URI.create("/pessoas/" + pessoaRequest.getId())).body(pessoaRequest);
        } catch (DataIntegrityViolationException exception) {
            pessoaCache.addApelidoCache(pessoaRequest.getApelido());
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @GetMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable UUID id) {
        Pessoa pessoa = pessoaCache.getPessoaCache(id);
        if (pessoa == null) {
            Optional<Pessoa> pessoaOptional = pessoaService.findById(id);
            if (pessoaOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            pessoa = pessoaOptional.get();
            pessoaCache.addPessoaCache(pessoa);
        }
        return ResponseEntity.ok(pessoa);
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<Pessoa>> findById(@RequestParam(value = "t") String t) {
        if (t == null || t.isEmpty() || t.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        var pessoas = pessoaService.listarTodosPorTermo("%" + t + "%");
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/contagem-pessoas")
    public ResponseEntity<Long> contagemPessoas() {
        return ResponseEntity.ok(pessoaService.count());
    }

}
