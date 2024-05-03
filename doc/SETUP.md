# SET UP for Success.

## Deploying without Spring Official support (Current)
### Deploying without Spring Docker
reference: https://medium.com/@saygiligozde/using-docker-compose-with-spring-boot-and-postgresql-235031106f9f
Setting up Dockerfile, docker-compose.yml.

For current setup checkout docker-compose.yml, Dockerfile at root.

### At deploy server after updating.
`./mvnw clean package -DskipTests` to make sure no cache or not updated files in the `target/` directory.\
(the reason why we skip the tests it's because it can connect to jdbc, but do run all the test locally before pushing.)\
then\
`docker-compose up --build` to check if the application runs properly, `<CR>-c` or `ctrl+c` to terminate\
lastly\
`docker-compose up --detach --build`\
if there are more than 1 docker-compose file such as dev\
run `docker-compose -f docker-compose-dev.yml up --detach --build`.

## Official Deployment Guide:
**For deploying on Linux remote servers here is the guide**
### Table of Content
- [Ubuntu](#ubuntu)
  - [Docker](#docker)
  - [Postgres](#postgres-)
  - [Environment Variables](#env)
   
NOTE: any name inside the [  ] is a variable, you can input anything suits you.

## Ubuntu 

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

### Postgres 
How to set up Postgres with latest version

reference: https://www.linode.com/docs/guides/how-to-install-use-postgresql-ubuntu-20-04/

### ENV
To make sure our the "application.properties" or "application.yml" works able to access the ${variable_name}, we will need set up the environment variables.
To do so we can go to ~/.bashrc by `vi ~/.bashrc` go to the bottom of the file, or anywhere input as follows:

REMEMBER: after pasting input `:wq` then in the terminal `source ~/.bashrc`
we can verify with `echo $PG_DB` we should get `postgres`
```.bashrc
#==================== OMI ENV =====================#
#======= API =======#
export FRONTEND_URL=http://localhost:5174

#======= Keys =======#
export OPENAI_KEY=[openai_api_key]

#======= Postgres =======#
# export PG=jdbc:postgresql://localhost:5432/postgres
export PG_HOST=localhost
export PG_PORT=5432
export PG_DB=postgres
export PG_USERNAME=[username]
export PG_PASSWORD=[password]
# export PG_BREAKFAST=jdbc:postgresql://localhost:5432/breakfast
# export PG_MESURE=jdbc:postgresql://localhost:5432/mesure
export PG_BREAKFAST=breakfast
export PG_MESURE=mesure
```

