create table pessoa
(
    id         uuid primary key,
    apelido    varchar(32) unique not null,
    nascimento date               not null,
    nome       varchar(100)       not null,
    stack      jsonb              null,
    termo tsvector generated always as (
                   to_tsvector('english', nome || ' ' || apelido || ' ' || stack::text)
                   ) stored
);
CREATE INDEX index_termo ON pessoa USING gin(termo);