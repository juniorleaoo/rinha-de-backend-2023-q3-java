package com.rinhabackend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {

    @Query(value = "SELECT * FROM pessoa p WHERE to_tsquery('english', :busca) @@ termo LIMIT 50", nativeQuery = true)
    List<Pessoa> listarTodosPorTermo(@Param("busca") String termo);

}
