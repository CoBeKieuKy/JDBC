-- create database 'member' and table for database
CREATE database member;

CREATE TABLE member(
	id int,
	name varchar(20),
	PRIMARY KEY(id)
);

INSERT INTO member VALUES (1,"kanade"), (2,"isla"), (3,"yoshino"), (4,"kurumi"), (5,"tohka"), (6,"kurisu");

-- create procedure for exam commit, savepoint, rollback concept
CREATE DEFINER=`root`@`localhost` PROCEDURE `commit_savepoint_rollback`()
begin
		start transaction;
		delete from member where id = 3;
        savepoint sp;
        delete from member where id = 2;
        rollback to sp;
        commit;
        
        start transaction;
        insert into member values(7, "insertable set");
        commit;
        
		insert into member values(8, "non-insertable set");
end
