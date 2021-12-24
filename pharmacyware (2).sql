-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 05, 2021 at 10:48 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 7.3.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pharmacyware`
--

-- --------------------------------------------------------

--
-- Table structure for table `city`
--

CREATE TABLE `city` (
  `code` int(11) NOT NULL,
  `cityname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `code` int(11) NOT NULL,
  `cusname` varchar(50) NOT NULL,
  `cusperson` varchar(50) NOT NULL,
  `cusaddress` varchar(100) NOT NULL,
  `cusemail` varchar(50) NOT NULL,
  `cusphone` varchar(15) NOT NULL,
  `cuslocation` varchar(10) NOT NULL,
  `cuscity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `code` int(11) NOT NULL,
  `deptname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`code`, `deptname`) VALUES
(1, 'Dept1'),
(2, 'Dept2');

-- --------------------------------------------------------

--
-- Table structure for table `drag`
--

CREATE TABLE `drag` (
  `code` int(11) NOT NULL,
  `description` varchar(100) NOT NULL,
  `type` int(11) NOT NULL,
  `producer` int(11) NOT NULL,
  `supplier` int(11) NOT NULL,
  `bprice` float NOT NULL DEFAULT 0,
  `sprice` float NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `drag`
--

INSERT INTO `drag` (`code`, `description`, `type`, `producer`, `supplier`, `bprice`, `sprice`) VALUES
(1, 'tralala', 1, 1, 1, 102.36, 98.3);

-- --------------------------------------------------------

--
-- Table structure for table `dragorder`
--

CREATE TABLE `dragorder` (
  `code` int(11) NOT NULL,
  `customer` int(11) NOT NULL,
  `orderdate` date NOT NULL,
  `orderstatus` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `dragpart`
--

CREATE TABLE `dragpart` (
  `drag` int(11) NOT NULL,
  `partno` int(11) NOT NULL,
  `partdate` date NOT NULL,
  `expdate` date NOT NULL,
  `quantity` int(11) NOT NULL,
  `seqquantity` int(11) NOT NULL,
  `quantmeasure` varchar(50) NOT NULL,
  `code` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `dragtype`
--

CREATE TABLE `dragtype` (
  `code` int(11) NOT NULL,
  `dragtype` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `dragtype`
--

INSERT INTO `dragtype` (`code`, `dragtype`) VALUES
(1, 'Φάρμακα'),
(2, 'Παραφαρμακευτικά');

-- --------------------------------------------------------

--
-- Table structure for table `orderitems`
--

CREATE TABLE `orderitems` (
  `dragorder` int(11) NOT NULL,
  `item` int(11) NOT NULL,
  `partno` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `dragorder` int(11) NOT NULL,
  `amount` double NOT NULL,
  `paydate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `producer`
--

CREATE TABLE `producer` (
  `code` int(11) NOT NULL,
  `prodname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `web` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `producer`
--

INSERT INTO `producer` (`code`, `prodname`, `email`, `web`) VALUES
(1, 'Unifarma', 'info@unifarma.gr', 'unifarma.gr'),
(2, 'Βιανέξ', 'info@vianex.gr', 'vianex.gr');

-- --------------------------------------------------------

--
-- Table structure for table `supplier`
--

CREATE TABLE `supplier` (
  `code` int(11) NOT NULL,
  `supname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `web` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `supplier`
--

INSERT INTO `supplier` (`code`, `supname`, `email`, `web`) VALUES
(1, 'PharmaGo', 'info@pharmago.gr', 'pharmago.gr'),
(2, 'ZetP', 'info@zetp.gr', 'zetp.gr');

-- --------------------------------------------------------

--
-- Table structure for table `systemuser`
--

CREATE TABLE `systemuser` (
  `code` int(11) NOT NULL,
  `usertype` varchar(50) NOT NULL,
  `department` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `fullname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `systemuser`
--

INSERT INTO `systemuser` (`code`, `usertype`, `department`, `username`, `password`, `fullname`, `email`, `phone`) VALUES
(1, 'Admin', 1, 'Admin', 'Admin', 'Admin Admin', 'admin@ph.gr', '2105852369');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `city`
--
ALTER TABLE `city`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`code`),
  ADD KEY `cuscity` (`cuscity`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `drag`
--
ALTER TABLE `drag`
  ADD PRIMARY KEY (`code`),
  ADD KEY `producer` (`producer`),
  ADD KEY `supplier` (`supplier`),
  ADD KEY `type` (`type`);

--
-- Indexes for table `dragorder`
--
ALTER TABLE `dragorder`
  ADD PRIMARY KEY (`code`),
  ADD KEY `customer` (`customer`);

--
-- Indexes for table `dragpart`
--
ALTER TABLE `dragpart`
  ADD PRIMARY KEY (`drag`,`partno`);

--
-- Indexes for table `dragtype`
--
ALTER TABLE `dragtype`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `orderitems`
--
ALTER TABLE `orderitems`
  ADD PRIMARY KEY (`dragorder`,`item`,`partno`),
  ADD KEY `item` (`item`,`partno`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`dragorder`,`paydate`),
  ADD KEY `dragorder` (`dragorder`);

--
-- Indexes for table `producer`
--
ALTER TABLE `producer`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `supplier`
--
ALTER TABLE `supplier`
  ADD PRIMARY KEY (`code`);

--
-- Indexes for table `systemuser`
--
ALTER TABLE `systemuser`
  ADD PRIMARY KEY (`code`),
  ADD KEY `department` (`department`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `city`
--
ALTER TABLE `city`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `drag`
--
ALTER TABLE `drag`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `dragorder`
--
ALTER TABLE `dragorder`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dragtype`
--
ALTER TABLE `dragtype`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `producer`
--
ALTER TABLE `producer`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `supplier`
--
ALTER TABLE `supplier`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `systemuser`
--
ALTER TABLE `systemuser`
  MODIFY `code` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`cuscity`) REFERENCES `city` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `drag`
--
ALTER TABLE `drag`
  ADD CONSTRAINT `drag_ibfk_1` FOREIGN KEY (`supplier`) REFERENCES `supplier` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `drag_ibfk_2` FOREIGN KEY (`producer`) REFERENCES `producer` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `drag_ibfk_3` FOREIGN KEY (`type`) REFERENCES `dragtype` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `dragorder`
--
ALTER TABLE `dragorder`
  ADD CONSTRAINT `dragorder_ibfk_1` FOREIGN KEY (`customer`) REFERENCES `customer` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `dragpart`
--
ALTER TABLE `dragpart`
  ADD CONSTRAINT `dragpart_ibfk_1` FOREIGN KEY (`drag`) REFERENCES `drag` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `orderitems`
--
ALTER TABLE `orderitems`
  ADD CONSTRAINT `dragorder_ibfk_2` FOREIGN KEY (`item`,`partno`) REFERENCES `dragpart` (`drag`, `partno`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `orderitems_ibfk_1` FOREIGN KEY (`dragorder`) REFERENCES `dragorder` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`dragorder`) REFERENCES `dragorder` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `systemuser`
--
ALTER TABLE `systemuser`
  ADD CONSTRAINT `systemuser_ibfk_1` FOREIGN KEY (`department`) REFERENCES `department` (`code`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
