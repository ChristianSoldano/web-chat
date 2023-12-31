CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.users
(
    id         UUID                     NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    username   VARCHAR(255)             NOT NULL,
    password   VARCHAR(255)             NOT NULL,
    name       VARCHAR(255)             NOT NULL,
    lastname   VARCHAR(255)             NOT NULL,
    email      VARCHAR(255)             NOT NULL,
    enabled    BOOLEAN                  NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UNQ_USERS_EMAIL UNIQUE (email),
    CONSTRAINT UNQ_USERS_USERNAME UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS public.roles
(
    id   UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT UNQ_ROLES_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public.users_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT FK_USER_ID FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_ROLE_ID FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles ("name")
VALUES ('ADMIN'),
       ('COMMON')
ON CONFLICT (name) DO NOTHING;

INSERT INTO users ("username", "password", "name", "lastname", "email")
VALUES ('admin', '$2a$10$KJWj1or7ZDh5f8aPNKAPlen06llsC3c4UUS4p2JiMD6u3Zz9gUyiW', 'administrator', 'administrator',
        'administrator@christiansoldano.com.ar')
ON CONFLICT (email) DO NOTHING;

INSERT INTO users_roles ("user_id", "role_id")
VALUES ((SELECT "id" FROM "users" WHERE "username" = 'admin'), (SELECT "id" FROM "roles" WHERE "name" = 'ADMIN'));