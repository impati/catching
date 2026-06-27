CREATE TABLE datasource
(
    name       VARCHAR(255) NOT NULL,
    url        VARCHAR(255) NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE field
(
    name            VARCHAR(255) NOT NULL,
    field_type      VARCHAR(255) NOT NULL,
    required        BIT          NOT NULL,
    datasource_name VARCHAR(255) NULL,
    first_come_id   VARCHAR(255) NULL,
    domain          TEXT         NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE first_come
(
    id            VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    capacity      INT          NOT NULL,
    approved      BIT          NOT NULL,
    start_at      DATETIME(6)  NOT NULL,
    end_at        DATETIME(6)  NOT NULL,
    display_at    DATETIME(6)  NOT NULL,
    eligibility   VARCHAR(255) NOT NULL,
    duplicable    BIT          NOT NULL,
    join_method   VARCHAR(255) NOT NULL,
    wait_type     VARCHAR(255) NOT NULL,
    wait_capacity INT          NULL,
    organizer     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- join_method enum values: IMMEDIATELY
-- wait_type enum values: WAITLIST

CREATE TABLE applied_event
(
    id            VARCHAR(255) NOT NULL,
    first_come_id VARCHAR(255) NOT NULL,
    member_id     VARCHAR(255) NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_applied_event_first_come_member
        UNIQUE (first_come_id, member_id)
);

CREATE TABLE alternate_event
(
    id            VARCHAR(255) NOT NULL,
    first_come_id VARCHAR(255) NOT NULL,
    member_id     VARCHAR(255) NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_alternate_event_first_come_member
        UNIQUE (first_come_id, member_id)
);

CREATE TABLE applied_member
(
    id            VARCHAR(255) NOT NULL,
    first_come_id VARCHAR(255) NOT NULL,
    member_id     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_applied_member_first_come_member
        UNIQUE (first_come_id, member_id)
);

CREATE TABLE applied_member_information
(
    id                 VARCHAR(255) NOT NULL,
    applied_member_id  VARCHAR(255) NULL,
    name               VARCHAR(255) NOT NULL,
    information_values TEXT         NOT NULL,
    PRIMARY KEY (id)
);
