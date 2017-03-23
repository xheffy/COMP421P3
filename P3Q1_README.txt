db2 connect to cs421

db2 -td@ -vf P3Q1.sql 

db2 "call GET_BEST_CUSTOMERS_OF_YEAR(YEAR)"