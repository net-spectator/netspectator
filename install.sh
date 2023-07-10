#!/bin/bash
mkdir repo
mkdir docker/dbdata
mvn clean package -Dmaven.test.skip=true -pl 'api, DirServer, UserService'
mvn deploy:deploy-file -Durl=file:./repo  -Dfile=api/target/api-1.0-SNAPSHOT.jar -DgroupId=org.net -DartifactId=api -Dpackaging=jar -Dversion=1.0-SNAPSHOT
mvn deploy:deploy-file -Durl=file:./repo  -Dfile=DirServer/target/DirServer-1.0-SNAPSHOT.jar -DgroupId=org.net -DartifactId=DirServer -Dpackaging=jar -Dversion=1.0-SNAPSHOT
mvn deploy:deploy-file -Durl=file:./repo  -Dfile=UserService/target/UserService-1.0.jar.original -DgroupId=org.net -DartifactId=UserService -Dpackaging=jar -Dversion=1.0
docker-compose up -d
docker rmi `docker images | grep -P 'none' | awk '{print $3}'`
