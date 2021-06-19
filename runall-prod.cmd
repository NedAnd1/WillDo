@echo off

echo Note: Node.js, JDK, and MongoDB need to be installed prior to running this script


:: make sure the database is running
start /b cmd /c mongod


:: make sure the production server is running (no need for client server)
cd /d %~dp0 
call mvnw.cmd spring-boot:run -Pprod


pause