-- 新数据库建表
create table if not exists T_CONFIG
(
    CODE VARCHAR(32),
    V VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS T_PAGE
(
    ID VARCHAR(32),
    TITLE VARCHAR(256),
    CONTENT TEXT,
    CREATE_AT TIMESTAMP,
    UPDATE_AT TIMESTAMP,
    STATUS TINYINT DEFAULT 1,
    `DESC` VARCHAR(256)
);


CREATE TABLE IF NOT EXISTS T_REPLY
(
    ID VARCHAR(32),
    PAGE_ID VARCHAR(32),
    IP VARCHAR(256),
    CONTENT TEXT,
    CREATE_AT TIMESTAMP,
    STATUS TINYINT default 0
);

CREATE TABLE IF NOT EXISTS T_TAG
(
    ID VARCHAR(32),
    TAG VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS T_PAGE_TAG
(
    PAGE_ID VARCHAR(32),
    TAG_ID VARCHAR(32)
);