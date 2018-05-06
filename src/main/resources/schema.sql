DROP TABLE IF EXISTS `habicus`.`goal`;
DROP TABLE IF EXISTS `habicus`.`user`;

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `dob` datetime DEFAULT NULL,
  `gender` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS  `goal` (
  `goal_id` int(11) NOT NULL AUTO_INCREMENT,
  `users_user_id` int(11) unsigned NOT NULL,
  `due_date` decimal(10,0) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `goal_complete` int(11) DEFAULT '0',
  `goal_interval` varchar(255) DEFAULT NULL,
  `label_color` varchar(255) DEFAULT NULL,
  `pledge_amount` double DEFAULT NULL,
  `task_unit_count` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`goal_id`),
  UNIQUE KEY `goal_id_UNIQUE` (`goal_id`),
  KEY `user_id_idx` (`label_color`),
  KEY `user_id_idx1` (`users_user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`users_user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1

