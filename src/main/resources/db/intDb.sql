DROP TABLE IF EXISTS url;

CREATE TABLE url
(
    id              INTEGER NOT NULL,
    original_url    VARCHAR NOT NULL,
    short_url       VARCHAR NOT NULL UNIQUE,
    created_time    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_url PRIMARY KEY (id)
);
