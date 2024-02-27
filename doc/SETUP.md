# SET UP for Success.

For deploying on Linux remote servers here is the guide
References:

Linode Guide: https://www.linode.com/docs/guides/installing-and-using-docker-on-ubuntu-and-debian/

Ubuntu Docker: https://docs.docker.com/engine/install/ubuntu/

Debian Docker: https://docs.docker.com/engine/install/debian/


NOTE:  "your_app_name" in our case is  **omi**

### Docker
Use Spring Boot to build image
`./mvnw clean package` to create JAR file in our "target" directory
once it is compiled we can run the application with `java -jar target/[your_app_name]-0.0.1-SNAPSHOT.jar`

Then we can use Spring Boot to build a Docker image
`./mvnw spring-boot:build-image`


follow by making sure the image exist `docker image ls -a | grep [your_app_name]` 
then we can run it try running it with `docker run -it -p 8080:8080 [your_app_name]:0.0.1-SNAPSHOT` IF you see this error 

```
org.postgresql.util.PSQLException: 
Connection to localhost:5432 refused. 
Check that the hostname and 
port are correct and 
that the postmaster is accepting TCP/IP connections.
```
#####  --net=host 
then use this  `docker run -it --net=host -p 8080:8080 [your_app_name]:0.0.1-SNAPSHOT`
reference: https://stackoverflow.com/questions/56374012/connection-to-localhost5432-refused-check-that-the-hostname-and-port-are-corre




