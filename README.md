# OMI 原點 

Omi means origin.

### Goal
A point of origin for all my web projects. It will serve as the backend hub of all the API endpoints.

The frontends will be deployed separately with other repo.

### Why? Because time worth more than money
I am tired of spinning up yet another Spring Boot application 
and have to set up yet another Docker all for the same purpose.
My focus is to write the logic, build the functions, 
make the product useful instead of reconfiguring again and again, 
time worth more than money, don't waste it.

## Projects

[//]: # (- **Breakfast**: A daily news aggregator for business and finance news.)
- **Drafter**: We found someone to draft a cover letter for you.

[//]: # (- **Mesure**: A food and beverage industry focus cost management app.)

[//]: # (- **Invoice**: A invoice management system.)


## TODOs
### Drafter
#### Bug fix
1. Chunking inputs
2. Handle exceed token
3. Handl maximum context length 
4. Limit retry
5. Rate limit reached error 
6. Restrict retry to 1 minute for each retry.

#### Important features
1. Spring security:
   a. CORS
   b. API security
2. Spring test:
   a. Integration
   b. Unit test.

## References
Multiple Docker PG: 
- reference to https://dev.to/bgord/multiple-postgres-databases-in-a-single-docker-container-417l
- github: https://github.com/mrts/docker-postgresql-multiple-databases

Multi DataSource: Dan Vega https://www.youtube.com/watch?v=ZKYFGuukhT4
Spring JWT: Dan Vega https://www.danvega.dev/blog/spring-security-jwt