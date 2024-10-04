ALTER TABLE t_users
    ADD CONSTRAINT unique_email UNIQUE (email),
    ADD CONSTRAINT unique_username UNIQUE (username);