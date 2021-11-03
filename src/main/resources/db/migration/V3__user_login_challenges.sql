CREATE TABLE user_login_challenge
(
    id                 BIGSERIAL PRIMARY KEY,
    address            TEXT      NOT NULL UNIQUE,
    challenge          TEXT      NOT NULL,
    password_initiated TIMESTAMP,
    created            TIMESTAMP NOT NULL DEFAULT now()
);