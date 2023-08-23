package com.rinhabackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Async
    public void inserir(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

}
