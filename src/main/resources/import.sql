INSERT INTO USER (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES(1,'Han','1234','Myunghan','han@gmail.com' );
INSERT INTO USER (ID, USER_ID, PASSWORD, NAME, EMAIL) VALUES(2,'Apr','1234','April','april@gmail.com' );

INSERT INTO QUESTION (id, writer_id, title, contents, create_date) VALUES(1,1,'Test data 1','test contents 1234', CURRENT_TIMESTAMP());
INSERT INTO QUESTION (id, writer_id, title, contents, create_date) VALUES(2,2,'whats your nick name?','meh', CURRENT_TIMESTAMP());