package com.rinhabackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PessoaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final ConcurrentLinkedQueue<Pessoa> pessoas = new ConcurrentLinkedQueue<>();

    private static final RowMapper<Pessoa> MAPPER_PESSOA = (row, i) -> {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(row.getObject("id", UUID.class));
        pessoa.setApelido(row.getString("apelido"));
        pessoa.setNome(row.getString("nome"));
        pessoa.setNascimento(row.getString("nascimento"));
        pessoa.setStack(new StringListConverter().convertToEntityAttribute(row.getString("stack")));
        return pessoa;
    };

    public Long count() {
        return jdbcTemplate.queryForObject("select count(*) from pessoa", Long.class);
    }

    public Optional<Pessoa> findById(UUID id) {
        return jdbcTemplate.query("select id, apelido, nome, nascimento, stack from pessoa where id = ?",
                rs -> rs.next() ? Optional.ofNullable(MAPPER_PESSOA.mapRow(rs, 1)) : Optional.empty(),
                id
        );
    }

    public List<Pessoa> listarTodosPorTermo(String termo) {
        return jdbcTemplate.query("SELECT id, apelido, nome, nascimento, stack FROM pessoa p WHERE termo ILIKE ? LIMIT 50",
                MAPPER_PESSOA,
                termo);
    }

    public void inserir(Pessoa pessoa) {
        pessoas.add(pessoa);
    }

    @Scheduled(fixedDelay = 2000)
    public void batchInsertScheduled() {
        final Queue<Pessoa> pessoasQueue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < pessoas.size(); i++) {
            pessoasQueue.add(pessoas.poll());
        }
        batchInsert(pessoasQueue);
    }

    public void batchInsert(final Queue<Pessoa> pessoasQueue) {
        if (pessoasQueue.isEmpty()) return;

        jdbcTemplate.batchUpdate("insert into pessoa (id, apelido, nome, nascimento, stack) values(?,?,?,?,?)", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pessoa p = pessoasQueue.poll();
                ps.setObject(1, p.getId());
                ps.setString(2, p.getApelido());
                ps.setString(3, p.getNome());
                ps.setString(4, p.getNascimento());
                ps.setString(5, new StringListConverter().convertToDatabaseColumn(p.getStack()));
            }

            public int getBatchSize() {
                return pessoasQueue.size();
            }

        });
    }

}
