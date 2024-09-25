CREATE TABLE `t_users`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `email` varchar(255) DEFAULT NULL,
    `password`  varchar(255),
    `username`    varchar(255),
    `bio` varchar(255),
    `profileImageUrl`  varchar(255),
    PRIMARY KEY (`id`)
);