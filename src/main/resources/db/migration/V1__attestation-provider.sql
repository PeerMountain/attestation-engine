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
    id             BIGSERIAL PRIMARY KEY,
    ap_id          BIGINT    NOT NULL REFERENCES attestation_provider (id),
    type           INTEGER   NOT NULL UNIQUE,
    perpetuity     BOOL      NOT NULL,
    price          INTEGER   NOT NULL,
    expiration     TIMESTAMP NOT NULL,
    signed_message TEXT      NOT NULL
);