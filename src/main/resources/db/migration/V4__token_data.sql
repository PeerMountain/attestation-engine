CREATE TABLE token_data
(
    id            BIGSERIAL PRIMARY KEY,
    holder        TEXT   NOT NULL,
    token_id      BIGINT NOT NULL,
    nft_type      BIGINT NOT NULL,
    token_uri     TEXT,
    keys          TEXT,
    settings_hash TEXT,
    settings      TEXT,
    provider      TEXT,
    data          TEXT
);