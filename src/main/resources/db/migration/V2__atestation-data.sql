CREATE TABLE attestation_data
(

    id               BIGSERIAL PRIMARY KEY,
    nft_id           BIGINT NOT NULL REFERENCES nft_settings (id),
    customer_address TEXT   NOT NULL,
    data             TEXT   NOT NULL,
    hash_key_array   TEXT   NOT NULL,
    token_uri        TEXT   NOT NULL,
    hashed_data      TEXT   NOT NULL,
    signed_data      TEXT,
    UNIQUE (nft_id, customer_address)
)