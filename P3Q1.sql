CREATE OR REPLACE PROCEDURE GET_BEST_CUSTOMERS_OF_YEAR(IN PARAMETER1 INTEGER)
LANGUAGE SQL
BEGIN
DECLARE EOF INT DEFAULT 0;
DECLARE YEAR INTEGER;
DECLARE v_email VARCHAR(100);
DECLARE v_count INTEGER;
DECLARE v_sql varchar(100);
DECLARE stmt varchar(100);
DECLARE CSR1 CURSOR FOR
  SELECT EMAIL,count(*) as COUNT from ORDERS where YEAR(deliverydate)=PARAMETER1 group by EMAIL order by count desc FETCH FIRST 3 ROWS ONLY;
DECLARE CONTINUE HANDLER FOR NOT FOUND
  SET EOF = 1;

SET YEAR=PARAMETER1;
SET v_sql= 'CREATE TABLE BESTCUSTOMERS' || YEAR || '(EMAIL VARCHAR(100), COUNT integer)';
PREPARE v_statement FROM v_sql;
EXECUTE v_statement;
SET stmt='INSERT INTO BESTCUSTOMERS' || YEAR || ' VALUES(?,?)';
PREPARE s1 FROM stmt;
OPEN CSR1;
WHILE EOF = 0 DO
  FETCH FROM CSR1 INTO v_email, v_count;
  EXECUTE s1 USING v_email, v_count;
END WHILE;
CLOSE CSR1;
END
@
