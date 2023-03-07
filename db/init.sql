CREATE TABLE IF NOT EXISTS postgres.public.users
(
    id         serial PRIMARY KEY,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    password   VARCHAR(50)         NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    created_on TIMESTAMP           NOT NULL,
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS postgres.public.files
(
    id         serial PRIMARY KEY,
    file_name  VARCHAR(255) UNIQUE NOT NULL,
    object_id  VARCHAR(255)        NOT NULL,
    created_on TIMESTAMP           NOT NULL
);
