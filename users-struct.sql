USE `renren`;
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`(
`id` int(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
`nickName` varchar(20) NOT NULL,
`birth` INT(4) NOT NULL,
`home` VARCHAR(50) NOT NULL
)