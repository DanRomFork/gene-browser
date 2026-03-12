CREATE TABLE species (
    id              BIGSERIAL PRIMARY KEY,
    scientific_name Varchar(255) NOT NULL,
    tsn             Integer      NOT NULL UNIQUE
);

CREATE TABLE chromosomes (
    id         BIGSERIAL PRIMARY KEY,
    species_id BIGINT  NOT NULL REFERENCES species (id),
    index      Integer NOT NULL
);

CREATE TABLE base_pairs (
    id               BIGSERIAL PRIMARY KEY,
    chromosome_id    BIGINT  NOT NULL REFERENCES chromosomes (id),
    position         BIGINT  NOT NULL,
    left_nucleotide  Char(1) NOT NULL,
    right_nucleotide Char(1) NOT NULL
);

CREATE TABLE genes (
    id    BIGSERIAL PRIMARY KEY,
    start BIGINT NOT NULL REFERENCES base_pairs (id),
    stop  BIGINT NOT NULL REFERENCES base_pairs (id)
);
