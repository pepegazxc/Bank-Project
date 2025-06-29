CREATE TABLE accounts
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    account           VARCHAR(255) NULL,
    percent_per_month VARCHAR(100) NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE cards
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    card_name        LONGTEXT     NULL,
    law_per_month    VARCHAR(100) NULL,
    cashback_percent VARCHAR(100) NULL,
    card_info        LONGTEXT     NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE goal_templates
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    goal_name VARCHAR(255) NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE user
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    name                VARCHAR(200) NOT NULL,
    surname             VARCHAR(200) NOT NULL,
    patronymic          VARCHAR(200) NULL,
    user_name           LONGTEXT     NOT NULL,
    cipher_phone_number VARCHAR(200) NOT NULL,
    cipher_email        LONGTEXT     NOT NULL,
    cipher_passport     VARCHAR(200) NULL,
    hash_password       LONGTEXT     NOT NULL,
    hash_token          LONGTEXT     NOT NULL,
    postal_code         VARCHAR(20)  NOT NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE user_account
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    user_id       BIGINT         NULL,
    account_id    BIGINT         NULL,
    balance       DECIMAL(15, 2) NULL,
    custom_goal   VARCHAR(255)   NULL,
    goal_temp_id  BIGINT         NULL,
    cipher_number VARCHAR(255)   NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE user_card
(
    id                        BIGINT AUTO_INCREMENT NOT NULL,
    user_id                   BIGINT         NULL,
    card_id                   BIGINT         NULL,
    hash_card_number          VARCHAR(255)   NULL,
    hash_card_three_numbers   VARCHAR(20)    NULL,
    hash_card_expiration_date VARCHAR(100)   NULL,
    balance                   DECIMAL(15, 2) NULL,
    cashback                  DECIMAL(15, 2) NULL,
    is_active                 TINYINT(1)     NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE user_contact
(
    id                         BIGINT AUTO_INCREMENT NOT NULL,
    user_id                    BIGINT       NULL,
    contact_name               VARCHAR(255) NULL,
    cipher_contact_indentifier VARCHAR(255) NULL,
    contact_type               VARCHAR(50)  NULL,
    last_interaction           timestamp    NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

CREATE TABLE user_operations_history
(
    id             BIGINT AUTO_INCREMENT   NOT NULL,
    user_id        BIGINT         NULL,
    contact_id     BIGINT         NULL,
    operation_type VARCHAR(255)   NULL,
    amount         DECIMAL(15, 2) NULL,
    time           timestamp DEFAULT NOW() NULL,
    CONSTRAINT ` PRIMARY ` PRIMARY KEY (id)
);

ALTER TABLE user_contact
    ADD CONSTRAINT cipher_contact_indentifier UNIQUE (cipher_contact_indentifier);

ALTER TABLE user_account
    ADD CONSTRAINT cipher_number UNIQUE (cipher_number);

ALTER TABLE user
    ADD CONSTRAINT cipher_passport UNIQUE (cipher_passport);

ALTER TABLE user
    ADD CONSTRAINT cipher_phone_number UNIQUE (cipher_phone_number);

ALTER TABLE goal_templates
    ADD CONSTRAINT goal_name UNIQUE (goal_name);

ALTER TABLE user_card
    ADD CONSTRAINT hash_card_expiration_date UNIQUE (hash_card_expiration_date);

ALTER TABLE user_card
    ADD CONSTRAINT hash_card_number UNIQUE (hash_card_number);

ALTER TABLE user_card
    ADD CONSTRAINT hash_card_three_numbers UNIQUE (hash_card_three_numbers);

ALTER TABLE user
    ADD CONSTRAINT unique_email UNIQUE (cipher_email);

ALTER TABLE user
    ADD CONSTRAINT unique_tokenn UNIQUE (hash_token);

ALTER TABLE user
    ADD CONSTRAINT unique_user_name UNIQUE (user_name);

ALTER TABLE user_account
    ADD CONSTRAINT user_account_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX user_id ON user_operations_history (user_id);

ALTER TABLE user_account
    ADD CONSTRAINT user_account_ibfk_2 FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE NO ACTION;

CREATE INDEX account_id ON user_account (account_id);

ALTER TABLE user_account
    ADD CONSTRAINT user_account_ibfk_3 FOREIGN KEY (goal_temp_id) REFERENCES goal_templates (id) ON DELETE NO ACTION;

CREATE INDEX goal_temp_id ON user_account (goal_temp_id);

ALTER TABLE user_card
    ADD CONSTRAINT user_card_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX user_id ON user_operations_history (user_id);

ALTER TABLE user_card
    ADD CONSTRAINT user_card_ibfk_2 FOREIGN KEY (card_id) REFERENCES cards (id) ON DELETE NO ACTION;

CREATE INDEX card_id ON user_card (card_id);

ALTER TABLE user_contact
    ADD CONSTRAINT user_contact_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX user_id ON user_operations_history (user_id);

ALTER TABLE user_operations_history
    ADD CONSTRAINT user_operations_history_ibfk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX user_id ON user_operations_history (user_id);

ALTER TABLE user_operations_history
    ADD CONSTRAINT user_operations_history_ibfk_2 FOREIGN KEY (contact_id) REFERENCES user_contact (id) ON DELETE NO ACTION;

CREATE INDEX contact_id ON user_operations_history (contact_id);