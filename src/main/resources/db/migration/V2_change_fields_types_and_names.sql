ALTER TABLE accounts MODIFY percent_per_month FLOAT;
ALTER TABLE cards MODIFY card_name VARCHAR(255);
ALTER TABLE cards MODIFY cashback_percent FLOAT;
ALTER TABLE user_card RENAME COLUMN hash_card_number to cipher_card_number;
ALTER TABLE user_card RENAME COLUMN hash_card_three_numbers to cipher_card_three_numbers;
ALTER TABLE user_card RENAME COLUMN hash_card_expiration_date to cipher_card_expiration_date;
ALTER TABLE user_card MODIFY is_active BOOLEAN;
ALTER TABLE user_contact RENAME COLUMN cipher_contact_indentifier to cipher_contact_identifier;
