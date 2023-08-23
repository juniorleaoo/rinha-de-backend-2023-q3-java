package com.rinhabackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private RedisTemplate<String, String> template;
    @Autowired
    private ObjectMapper objectMapper;

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

        var apelidoExiste = template.opsForValue().get(pessoaRequest.getApelido());

        if (apelidoExiste != null) {
            return ResponseEntity.unprocessableEntity().build();
        }

        var uuid = UUID.randomUUID();

        try {
            pessoaRequest.setId(uuid);
            pessoaService.inserir(pessoaRequest);
            template.opsForValue().set(pessoaRequest.getId().toString(), objectMapper.writeValueAsString(pessoaRequest));
            template.opsForValue().set(pessoaRequest.getApelido(), ".");

            return ResponseEntity.created(URI.create("/pessoas/" + pessoaRequest.getId())).body(pessoaRequest);
        } catch (DataIntegrityViolationException exception) {
            template.opsForValue().set(pessoaRequest.getApelido(), ".");
            return ResponseEntity.unprocessableEntity().build();
        } catch (JsonProcessingException exception) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }


    @GetMapping("/pessoas/{id}")
    public ResponseEntity<Pessoa> findById(@PathVariable UUID id) throws JsonProcessingException {
        String pessoaString = template.opsForValue().get(id.toString());
        if (pessoaString == null || pessoaString.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(objectMapper.readValue(pessoaString, Pessoa.class));
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<Pessoa>> findById(@RequestParam(value = "t") String t) {
        if (t == null || t.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var pessoas = pessoaRepository.listarTodosPorTermo("%" + t + "%");
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/contagem-pessoas")
    public ResponseEntity<Long> contagemPessoas() {
        return ResponseEntity.ok(pessoaRepository.count());
    }


}
