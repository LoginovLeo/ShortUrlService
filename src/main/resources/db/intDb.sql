DROP TABLE IF EXISTS url;
DROP TABLE IF EXISTS request;

CREATE TABLE url
(
    id              SERIAL NOT NULL,
    original_url    VARCHAR NOT NULL,
    short_url       VARCHAR NOT NULL,
    created_time    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    expires_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_url PRIMARY KEY (id)
);

CREATE TABLE request
(
    id              		SERIAL NOT NULL,
    available_connections	INTEGER NOT NULL,
    locked_until    		TIMESTAMP WITH TIME ZONE NOT NULL,
    last_request    		TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id)
);