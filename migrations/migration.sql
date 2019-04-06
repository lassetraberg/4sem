DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS connects CASCADE;
DROP TABLE IF EXISTS device CASCADE;
DROP TABLE IF EXISTS vehicle CASCADE;

CREATE TABLE account (
    account_id BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    created TIMESTAMP DEFAULT NOW(),
    last_login TIMESTAMP,
    login_attempts INT DEFAULT 0,
    last_login_attempt TIMESTAMP,
    role TEXT
);

CREATE TABLE device (
    device_id UUID PRIMARY KEY,
    last_active TIMESTAMP,
    license_plate VARCHAR(7) -- 2-7 characters
);

CREATE TABLE vehicle (
    device_id UUID REFERENCES device(device_id),
    speed SMALLINT NOT NULL,
    timestamp TIMESTAMP DEFAULT NOW(),
    acceleration REAL NOT NULL,
    speed_limit SMALLINT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

CREATE TABLE connects (
    account_id BIGINT NOT NULL REFERENCES account(account_id),
    device_id UUID NOT NULL REFERENCES device(device_id),
    PRIMARY KEY(account_id, device_id)
);

