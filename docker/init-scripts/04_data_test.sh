#!/bin/sh

sqlplus -s spring_db_test/spring_db_test@//localhost:1521/FREEPDB1 <<'SQL'
WHENEVER SQLERROR EXIT SQL.SQLCODE
@/opt/oracle/scripts/02_data.sql
EXIT
SQL