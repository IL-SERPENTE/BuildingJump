CREATE TABLE bj_jumps
(
  `jump_id` BINARY(16) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `owner` BINARY(16) NOT NULL,
  `published` TINYINT(1) NOT NULL DEFAULT 0,
  `difficulty` ENUM('easy', 'normal', 'hard') NOT NULL DEFAULT 'normal',
  `duration` ENUM('very_short', 'short', 'long', 'very_long') DEFAULT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `published_at` DATETIME DEFAULT NULL,
  `version` INTEGER NOT NULL DEFAULT 0,
  `size` INTEGER NOT NULL,
  `location_spawn` VARCHAR(50) NOT NULL,
  `location_begin` VARCHAR(50) DEFAULT NULL,
  `location_end` VARCHAR(50) DEFAULT NULL,
  `map_time` INTEGER NOT NULL DEFAULT 6000,   -- INTEGER et pas SMALLINT (qui suffit pour l'intervalle 0-24000) pour permettre la modification à terme des phases de la Lune
  `map_weather` ENUM('clear', 'rain', 'thunder', 'clear_thunder') DEFAULT 'clear',
  `mean_rating` TINYINT(1) DEFAULT -1,
  `mean_rating_last_version` TINYINT(1) DEFAULT -1,
  `data` LONGBLOB NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE bj_jumps_checkpoints
(
  `jump_id` BINARY(16) NOT NULL,
  `name` VARCHAR(256) DEFAULT NULL,
  `description` VARCHAR(256) DEFAULT NULL,
  `location` VARCHAR(50) NOT NULL,
  `order` INTEGER
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

CREATE TABLE bj_jumps_games
(
  `jump_id` BINARY(16) NOT NULL,
  `jump_version` INTEGER NOT NULL,
  `player` BINARY(16) NOT NULL,
  `started_at` DATETIME NOT NULL,
  `finished_at` DATETIME NOT NULL,
  `duration` INTEGER NOT NULL,
  `rating` TINYINT(1) DEFAULT -1
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
