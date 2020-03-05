ALTER TABLE `user` ADD `finger_print` TEXT  NULL AFTER `password`;


CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `module_id` int(11) NOT NULL,
  `module_activity_id` int(11) NOT NULL,
  `module_schedule_id` int(11) NOT NULL,
  `finger_print` text NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
  
  
ALTER TABLE `user` CHANGE `finger_print` `finger_print` MEDIUMTEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL;

ALTER TABLE `attendance` CHANGE `finger_print` `finger_print` MEDIUMTEXT CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL;


COMMIT;