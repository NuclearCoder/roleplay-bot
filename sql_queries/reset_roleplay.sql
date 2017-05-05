/* Drop all */
USE roleplay;
DROP TABLE `characters`;
DROP TABLE `guilds`;
DROP DATABASE roleplay;

/* Create all */
CREATE DATABASE `roleplay` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE roleplay;
CREATE TABLE `characters` (
  `id_guild` bigint(20) NOT NULL,
  `id_user` bigint(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id_guild`,`id_user`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `guilds` (
  `id` bigint(20) NOT NULL,
  `rp_role` bigint(20) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;