# About
This repository contains the code for the backend of the full-stack Bugtracker project. The frontend code can be found in the following 
[repository](https://github.com/skorotky/bugtracker-client).

# Tech Stack
* **Language**: Java 19
* **Framework:** Spring Boot
* **Build tool:** Maven
* **Server:** Tomcat
* **Database:** PostgreSQL

# Dependencies
<ul type="square">
  <li>Spring Web</li>
  <li>Spring Data JPA</li>
  <li>Spring Security</li>
  <li>Spring HATEOAS</li>
  <li>PostgreSQL Driver</li>
  <li>Spring Boot Dev Tools</li>
  <li>Spring Validation</li>
  <li>Lombok</li>
  <li>Jackson Databind</li>
</ul>

# Project Features
* REST API
* Authentication (Basic)
* Authorization 

# Project Functionalities
- [ ] User registration 
- [x] User authentication
- [ ] Endpoint access control based on user roles/privileges 
- [x] Users can create, update, and delete projects  
- [ ] Project owners can configure their project visibility (private vs. public)
- [x] Project owners can add to or remove collaborators from their project
- [x] Inside the project, users can create, update, and delete bug reports
- [x] Users can leave comments under bug reports and update/delete them

# Installation 
1. ```git clone https://github.com/skorotky/bugtracker-api.git```  
2. ```cd bugtracker-api ```  
3. In the ```application.properties``` file, put your own PostgreSQL credentials.
4. Run the Spring Boot application.
5. Open http://localhost:8080/ in the browser.
6. Navigate to ```/api/**``` routes to see the API.
7. To access the API, enter ```user``` for username and ```password``` for password in the pop-up window.
