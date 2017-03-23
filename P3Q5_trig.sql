CREATE OR REPLACE TRIGGER TRIG 
AFTER DELETE ON TRANSACTIONS
REFERENCING OLD AS O
FOR EACH ROW MODE DB2SQL
BEGIN
DELETE FROM ORDERS WHERE O.ORDERNO = ORDERNO;
DELETE FROM ORDERLIST WHERE O.ORDERNO = ORDERNO;
END
@
