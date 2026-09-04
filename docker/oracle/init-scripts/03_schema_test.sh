#!/bin/sh

sqlplus -s spring_db_test/spring_db_test@//localhost:1521/FREEPDB1 <<'SQL'
WHENEVER SQLERROR EXIT SQL.SQLCODE
@/opt/oracle/scripts/00_only_test.sql
@/opt/oracle/scripts/01_schema.sql
EXIT
SQL