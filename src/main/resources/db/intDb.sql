DROP TABLE IF EXISTS url;

CREATE TABLE url
(
    id              SERIAL NOT NULL,
    original_url    VARCHAR NOT NULL,
    short_url       VARCHAR NOT NULL,
    created_time    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    expires_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_url PRIMARY KEY (id)
);
