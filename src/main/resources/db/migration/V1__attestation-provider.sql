CREATE TABLE attestation_provider
(
    id                  BIGSERIAL PRIMARY KEY,
    name                TEXT UNIQUE,
    address             TEXT UNIQUE,
    initial_transaction TEXT UNIQUE,
    status              TEXT
);