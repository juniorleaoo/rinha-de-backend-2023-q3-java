CREATE EXTENSION btree_gin;

-- for debug slow queries https://www.postgresql.org/docs/current/pgstatstatements.html
-- CREATE EXTENSION pg_stat_statements;
-- ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';

create table pessoa
(
    id         uuid primary key,
    apelido    varchar(32) unique not null,
    nascimento varchar(10) not null,
    nome       varchar(100)       not null,
    stack      text              null
);
CREATE INDEX index_termo ON pessoa USING gin(apelido, nome, stack);