CREATE TABLE attestation_provider
(
    id                  BIGSERIAL PRIMARY KEY,
    name                TEXT UNIQUE,
    address             TEXT UNIQUE,
    initial_transaction TEXT UNIQUE,
    status              TEXT
);

CREATE TABLE nft_settings
(
    id                   BIGSERIAL PRIMARY KEY,
    ap_id                BIGINT    NOT NULL REFERENCES attestation_provider (id),
    type                 INTEGER   NOT NULL UNIQUE,
    price                INTEGER   NOT NULL,
    expiration           TIMESTAMP NOT NULL,
    signature            TEXT      NOT NULL,
    status               BOOL      NOT NULL DEFAULT TRUE,
    name                 TEXT      NOT NULL,
    description          TEXT,
    image_url            TEXT      NOT NULL
);