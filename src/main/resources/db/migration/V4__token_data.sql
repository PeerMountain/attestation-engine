CREATE TABLE token_data
(
    id               BIGSERIAL PRIMARY KEY,
    holder           TEXT NOT NULL,
    token_id         BIGINT,
    token_uri        TEXT,
    keys             TEXT,
    settings         TEXT,
    provider         TEXT,
    data             TEXT
);