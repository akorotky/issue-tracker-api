DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS project_user CASCADE;
DROP TABLE IF EXISTS bug CASCADE;
DROP TABLE IF EXISTS bug_comment CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS user_role CASCADE;

CREATE TABLE "user" (
    id BIGSERIAL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    account_locked BOOLEAN,
    account_enabled BOOLEAN,
    account_expired BOOLEAN,
    credentials_expired BOOLEAN,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE project (
    id BIGSERIAL,
    title varchar(255) NOT NULL,
    description TEXT NOT NULL,
    private BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    CONSTRAINT project_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE project_user (
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT project_user_pkey PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

CREATE TABLE bug (
    id BIGSERIAL,
    title varchar(255) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    modified_by BIGINT NOT NULL,
    CONSTRAINT bug_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE bug_comment (
    id BIGSERIAL,
    body TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    bug_id BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    CONSTRAINT bug_comment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_bug FOREIGN KEY (bug_id) REFERENCES bug(id)
);

CREATE TABLE role (
    id BIGSERIAL,
    name varchar(255) NOT NULL,
    CONSTRAINT role_pkey PRIMARY KEY (id)
);

CREATE TABLE user_role (
    id BIGSERIAL,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    CONSTRAINT role_user_pkey PRIMARY KEY (role_id, user_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id)
);

