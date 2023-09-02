-- CREATE EXTENSION btree_gin;

CREATE EXTENSION pg_trgm; --https://www.postgresql.org/docs/current/pgtrgm.html

-- for debug slow queries https://www.postgresql.org/docs/current/pgstatstatements.html
-- CREATE EXTENSION pg_stat_statements;
-- ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';

create table pessoa
(
    id         uuid primary key,
    apelido    varchar(32) unique not null,
    nascimento varchar(10) not null,
    nome       varchar(100)       not null,
    stack      text              null,
    termo TEXT GENERATED ALWAYS AS (
            nome || ' ' || apelido || ' ' || stack
    ) STORED
);

CREATE INDEX index_termo ON pessoa USING gin(termo gin_trgm_ops);