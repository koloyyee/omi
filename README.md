# OMI 原點 

Omi means origin.

### Goal
A point of origin for all my web projects. It will serve as the backend 
hub of all the API endpoints.
The idea behind this project is to build a monolithic backend so that 
I can experience complexity that would naturally occur.
For example Multiple data sources, 
Security Filter Chain for different endpoints, 
and handling different database in a single postgres container.

The focus of the project will be a collection of projects 
that I am interested in, the number of subprojects in Omi will reflect 
my interests in different topics including 
using LLM technology, Virtual Threads, Securities, 
and trying out different kinds of databases 
including Postgres, Casandra, Neo4J

Other practices like CI/CD, performance monitoring, and data analysis 
could be part of Omi, there is no limit to this project 
until I need to upgrade my nano server.

The frontends SPA will be deployed separately with other repos, 
currently my focus is React + Spring Boot, but later I would like to 
include Svelte, Vaadin or Hilla, HTMX with Thymeleaf, or trying out Vue.

### Why? Because time worth more than money
I am tired of spinning up yet another Spring Boot application 
and have to set up yet another Docker all for the same purpose.
My focus is to write the logic, build the functions, 
make the product useful instead of reconfiguring again and again, 
time worth more than money, don't waste it.

## Projects

[//]: # (- **Breakfast**: A daily news aggregator for business and finance news.)
- **Drafter**: We found someone to draft a cover letter for you.
  -  Design document: [design-doc.md](src%2Fmain%2Fjava%2Fco%2Floyyee%2FOmi%2FDrafter%2Fdoc%2Fdesign-doc.md)

[//]: # (- **Mesure**: A food and beverage industry focus cost management app.)

[//]: # (- **Invoice**: A invoice management system.)


## TODOs
#### Important
1. Spring security:
   1. CORS 
   2. API security
2. Spring test: 
   1. Integration
   2. Unit test.

## References
Multiple Docker PG: 
- reference to https://dev.to/bgord/multiple-postgres-databases-in-a-single-docker-container-417l
- github: https://github.com/mrts/docker-postgresql-multiple-databases

Multi DataSource: Dan Vega https://www.youtube.com/watch?v=ZKYFGuukhT4 \
Spring JWT: Dan Vega https://www.danvega.dev/blog/spring-security-jwt
