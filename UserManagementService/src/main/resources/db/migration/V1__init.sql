CREATE TABLE roles (
                       id bigserial NOT NULL,
                       title varchar(255) NULL,
                       CONSTRAINT roles_pkey PRIMARY KEY (id),
                       CONSTRAINT roles_un UNIQUE (title)
);


CREATE TABLE user_details (
                              id bigserial NOT NULL,
                              "attributes" varchar(255) NULL,
                              email varchar(255) NULL,
                              family_name varchar(255) NULL,
                              given_name varchar(255) NULL,
                              CONSTRAINT user_details_pkey PRIMARY KEY (id)
);


CREATE TABLE users (
                       id bigserial NOT NULL,
                       username varchar(255) NULL,
                       uuid uuid NULL,
                       user_details int8 NULL,
                       CONSTRAINT users_pkey PRIMARY KEY (id),
                       CONSTRAINT fkthl02rp4acq48myaq38jhjo3b FOREIGN KEY (user_details) REFERENCES user_details(id)
);


CREATE TABLE users_roles (
                             user_id int8 NOT NULL,
                             role_id int8 NOT NULL,
                             CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles (title) VALUES('ROLE_ADMIN');
INSERT INTO roles (title) VALUES('ROLE_USER');