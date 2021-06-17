CREATE TABLE attestation_data
(

    id             BIGSERIAL PRIMARY KEY,
    provider_id    BIGINT NOT NULL REFERENCES attestation_provider (id),
    hash_key_array TEXT   NOT NULL,
    token_uri      TEXT   NOT NULL,
    hashed_data    TEXT   NOT NULL,
    signed_data    TEXT
)