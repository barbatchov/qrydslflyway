CREATE TABLE user (
    id INTEGER NOT NULL IDENTITY,
    name VARCHAR NOT NULL,
    password VARCHAR NULL,
    CONSTRAINT PK_user PRIMARY KEY (id)
);