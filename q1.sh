db2 connect to cs421;
db2 -td@ -vf P3Q1.sql
db2 "select distinct * from BESTCUSTOMERS2015";
db2 "call GET_BEST_CUSTOMERS_OF_YEAR(2015)";
db2 "select distinct * from BESTCUSTOMERS2015";
