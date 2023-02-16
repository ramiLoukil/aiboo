# Getting Started

### Prerequis
* install Mysql (XAMP can help in windows case)
* install jdk 17 and set JAVA_HOME environnement variable
* install maven 3.8.6 and set M2_HOME environnement variable
* recuperate code source from [aiboo code source](https://github.com/ramiLoukil/aiboo) using git clone command
* create the database aiboo using ressources/sql/0_aiboo_create_database.sql script
* drop the Spring Batch tables using ressources/sql/1_spring_batch_schema-drop-mysql.sql script
* create the Spring Batch tables using ressources/sql/2_spring_batch_schema-mysql.sql script
* create the Aiboo tables using ressources/sql/3_aiboo_create_tables.sql script
* register to Babelnet to recuperate a Key (1000 possible requests per day!) 
* in the application.properties file set the spring.cache.jcache.config parameter as following

  `spring.cache.jcache.config=classpath:ehcache.xml`

* add the Babelnet recuperated key in config/babelnet-var.properties file (in babelnet.key parameter)

### Install & launch the AIBOO application
* build: mvn install:install-file -Dfile="jltutils-2.2.jar" -DgroupId="it.uniroma1.lcl.jlt" -DartifactId="jltutils" -Dversion="2.2" -Dpackaging=jar
* build: mvn install:install-file -Dfile=babelnet-api-3.7.jar
* build: mvn clean install -DskipTests=true
* launch: cd target
* launch AO documents : java -jar .\aiboo-1.0.0-SNAPSHOT.jar -type "AO" 
* launch CV documents : java -jar .\aiboo-1.0.0-SNAPSHOT.jar -type "CV"

### Launch AIBOO in PROD
* copy the generated jar file: aiboo-1.0.0-SNAPSHOT.jar in a directory Aiboo
* create a subdirectory config in Aiboo and put all properties files in :
  application.properties
  ehcache.xml
  babelnet.properties
  babelnet.var.properties
  jlt.properties
  jlt.var.properties
  ser.properties

* Configure the application.properties file values (input, archive & error directory, datasource, cache, log level ...)

`#Spring batch
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=ALWAYS`

`#Aiboo
aiboo.input=C:\\IN_APPLI
aiboo.archive=C:\\ARCH_APPLI
aiboo.error=C:\\ERROR_APPLI`

`aiboo.numberOfConcepts=10
aiboo.numberMinOfOccurence=3
aiboo.domain=COMPUTING`

`#Datasource
spring.datasource.url=jdbc:mysql://localhost:3306/aiboo
spring.datasource.username=root
spring.datasource.password=`

`#PDFBOX
#logging.level.org.apache.pdfbox:ERROR`

`#babelnet
logging.level.it.uniroma1.lcl.babelnet:INFO`

`#EhCache conf file
spring.cache.jcache.config=file:config/ehcache.xml`

* Configure the ehcache.xml file values:

`<config
xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
xmlns='http://www.ehcache.org/v3'
xsi:schemaLocation='http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core-3.0.xsd'>
    <persistence directory="C:\\aiboo-ehcache\\cache" />
    <cache alias="BabelSynsetCache">
        <key-type>java.lang.String</key-type>
        <value-type>java.lang.String</value-type>
        <!--expiry>
            <ttl>3600</ttl>
        </expiry-->
        <resources>
            <heap unit="entries">10000</heap>
            <offheap unit="MB">10</offheap>
            <disk persistent="true" unit="MB">20</disk>
        </resources>
    </cache>
</config>`

* launch AO documents : 
  cd Aiboo
  java -jar .\aiboo-1.0.0-SNAPSHOT.jar -type "AO"

* launch CV documents :
  cd Aiboo
  java -jar .\aiboo-1.0.0-SNAPSHOT.jar -type "CV"


### Stop words
* The english and frensh stopwords files were downloaded from this [link](https://countwordsfree.com/stopwords/) : you can use other languages.