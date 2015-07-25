USE `renren`;
DROP TABLE IF EXISTS `faces`;
CREATE TABLE `faces`(
`id` VARCHAR(20) NOT NULL PRIMARY KEY,
`nickname` varchar(20) NOT NULL,
`age` int(2) NOT NULL,
`sex` int(1) NOT NULL,
`width` int(4),
`height` int(4)
);
CREATE UNIQUE INDEX id_unique ON renren.faces(id);