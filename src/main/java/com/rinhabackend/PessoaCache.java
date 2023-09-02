package com.rinhabackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PessoaCache {

    @Autowired
    private Map<String, Boolean> cacheApelidos;
    @Autowired
    private Map<String, Pessoa> cachePessoas;

    public Pessoa getPessoaCache(UUID id) {
        return cachePessoas.get(id.toString());
    }

    public void addPessoaCache(Pessoa pessoa) {
        cachePessoas.put(pessoa.getId().toString(), pessoa);
    }

    public boolean hasApelidoCache(String apelido) {
        var apelidoExiste = cacheApelidos.get(apelido);
        return apelidoExiste != null && apelidoExiste;
    }

    public void addApelidoCache(String apelido) {
        cacheApelidos.put(apelido, true);
    }

}
