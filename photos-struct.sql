USE `renren`;
DROP TABLE IF EXISTS `photos`;
CREATE TABLE `photos`(
`id` int(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
`nickName` varchar(20) NOT NULL,
`age` int(3) NOT NULL,
`avatarUrl` VARCHAR(100) NOT NULL,
`isFetch` BOOLEAN NOT NULL DEFAULT FALSE
)