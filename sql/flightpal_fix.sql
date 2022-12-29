-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 29, 2022 at 04:13 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flightpal_fix`
--

-- --------------------------------------------------------

--
-- Table structure for table `airlines`
--

CREATE TABLE `airlines` (
  `airline_id` varchar(5) NOT NULL,
  `airline_name` varchar(40) NOT NULL,
  `website` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `airlines`
--

INSERT INTO `airlines` (`airline_id`, `airline_name`, `website`) VALUES
('AI001', 'batik air', 'batikair.com'),
('AI002', 'Aviastar', 'aviastar.com'),
('AI003', 'Lion Air', 'lionair.com'),
('AI004', 'Sriwijaya Air', 'sriwijaya.com'),
('AI005', 'Citilink', 'citilink.com'),
('AI006', 'Wings Air', 'wings.com'),
('AI186', 'Garuda Indonesia', 'garuda.com');

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `ticket_id` varchar(5) NOT NULL,
  `airline` varchar(5) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `hours_of_departure` time DEFAULT NULL,
  `hour_to_destination` time DEFAULT NULL,
  `class_type` varchar(20) DEFAULT NULL,
  `origin` varchar(40) DEFAULT NULL,
  `destination` varchar(40) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `user_id` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`ticket_id`, `airline`, `date`, `hours_of_departure`, `hour_to_destination`, `class_type`, `origin`, `destination`, `price`, `user_id`) VALUES
('TI001', 'AI001', '2022-12-31', '10:00:00', '12:00:00', 'Economy', 'Denpasar', 'Jakarta', 1000000, NULL),
('TI002', 'AI005', '2023-01-03', '14:00:00', '18:00:00', 'Bussiness', 'Denpasar', 'Palembang', 1500000, 'US005'),
('TI003', 'AI003', '2023-01-12', '08:00:00', '12:00:00', 'Economy', 'Denpasar', 'Pontianak', 1500000, NULL),
('TI004', 'AI003', '2024-01-10', '13:00:00', '19:00:00', 'Economy', 'Denpasar', 'Sorong', 2000000, NULL),
('TI005', 'AI003', '2023-01-20', '09:30:00', '11:00:00', 'Bussiness', 'Denpasar', 'Kupang', 3000000, NULL),
('TI006', 'AI002', '2023-01-10', '08:30:00', '12:00:00', 'Economy', 'Surabaya', 'Yogyakarta', 2000000, NULL),
('TI007', 'AI005', '2022-12-30', '19:00:00', '21:25:00', 'Economy', 'Jakarta', 'Makassar', 1200000, NULL),
('TI008', 'AI006', '2022-12-30', '10:50:00', '12:10:00', 'Economy', 'Yogyakarta', 'Denpasar', 760000, 'US005'),
('TI009', 'AI004', '2023-03-09', '13:20:00', '14:40:00', 'Economy', 'Denpasar', 'Yogyakarta', 830000, NULL),
('TI010', 'AI004', '2023-02-02', '15:10:00', '17:20:00', 'Economy', 'Surabaya', 'Batam', 1270000, NULL),
('TI011', 'AI005', '2023-01-25', '16:25:00', '17:40:00', 'Business', 'Medan', 'Batam', 4000000, NULL),
('TI012', 'AI003', '2023-01-28', '08:45:00', '11:25:00', 'Economy', 'Jakarta', 'Makasar', 2350000, NULL),
('TI013', 'AI002', '2023-01-28', '08:45:00', '14:00:00', 'Economy', 'Gorontalo', 'Manado', 2780000, NULL),
('TI014', 'AI001', '2023-01-10', '14:55:00', '17:55:00', 'Business', 'Semarang', 'Jakarta', 1600000, NULL),
('TI015', 'AI002', '2023-10-10', '10:00:00', '12:00:00', 'Bussiness', 'Jakarta', 'Denpasar', 2000000, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` varchar(5) NOT NULL,
  `user_id` varchar(5) NOT NULL,
  `ticket_id` varchar(5) NOT NULL,
  `full_name` varchar(40) NOT NULL,
  `card_id` varchar(40) NOT NULL,
  `passport` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `user_id`, `ticket_id`, `full_name`, `card_id`, `passport`, `email`) VALUES
('TR651', 'US005', 'TI002', 'Apriana', '36215362', '27367217', 'apariana@gmail.com'),
('TR927', 'US005', 'TI008', 'apriana', '12731', '12730', 'apriana@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(5) NOT NULL,
  `username` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `level` int(11) NOT NULL,
  `fullname` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `balance` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `level`, `fullname`, `email`, `balance`) VALUES
('US001', 'apriana', '11111111', 1, 'Apriana', 'gedeapriana36@gmail.com', 1000000),
('US002', 'wisny', '11111111', 1, 'Wisnu', 'wisnu@gmail.com', 1000000),
('US003', 'kenny', '11111111', 1, 'Kenny', 'kenny@gmail.com', 1000000),
('US004', 'hanna', '11111111', 1, 'Hanna', 'hanna@gmail.com', 1000000),
('US005', 'user', '11111111', 2, 'user', 'user@gmail.com', 240000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `airlines`
--
ALTER TABLE `airlines`
  ADD PRIMARY KEY (`airline_id`);

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`ticket_id`),
  ADD KEY `fk_tickets_airline` (`airline`),
  ADD KEY `fk_tickets_user` (`user_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `fk_transaction_ticket_id` (`ticket_id`),
  ADD KEY `fk_transaction_user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `fk_tickets_airline` FOREIGN KEY (`airline`) REFERENCES `airlines` (`airline_id`),
  ADD CONSTRAINT `fk_tickets_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `fk_transaction_ticket_id` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`ticket_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transaction_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
