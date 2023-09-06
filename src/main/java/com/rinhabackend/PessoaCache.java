package com.rinhabackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PessoaCache {

    @Autowired
    private RedisTemplate<String, String> template;
    @Autowired
    private ObjectMapper objectMapper;

    public Pessoa getPessoaCache(UUID id) {
        String pessoaString = template.opsForValue().get(id.toString());
        if (pessoaString == null || pessoaString.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(pessoaString, Pessoa.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void addPessoaCache(Pessoa pessoa) {
        try {
            template.opsForValue().set(pessoa.getId().toString(), objectMapper.writeValueAsString(pessoa));
        } catch (JsonProcessingException ignored) {
        }
    }

    public boolean hasApelidoCache(String apelido) {
        var apelidoExiste = template.opsForValue().get(apelido);
        return apelidoExiste != null;
    }

    public void addApelidoCache(String apelido) {
        template.opsForValue().set(apelido, ".");
    }

}
