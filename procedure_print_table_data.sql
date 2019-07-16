-- create procedure for print data of table
CREATE DEFINER=`root`@`localhost` PROCEDURE `getMember`()
BEGIN
  SELECT * FROM member;
END

