# Getting Started

### Prerequis
* install Mysql (XAMP can help in windows case)
* install jdk 17 and set JAVA_HOME environnement variable
* install maven 3.8.6 and set M2_HOME environnement variable
* recuperate code source from [aiboo code source](https://toto) using git clone command
* create the database aiboo using ressources/sql/0_aiboo_create_database.sql script
* drop the Spring Batch tables using ressources/sql/1_spring_batch_schema-drop-mysql.sql script
* create the Spring Batch tables using ressources/sql/2_spring_batch_schema-mysql.sql script
* create the Aiboo tables using ressources/sql/3_aiboo_create_tables.sql script


### Install & launch the AIBOO application
* build: mvn clean install -DskipTests=true
* launch: cd target
* launch AO documents : java -jar .\aiboo-1.0.0-SNAPSHOT.jar -type "AO" 
* launch CV documents : java -jar .\aiboo-1.0.0-SNAPSHOT.jar -type "CV"