# candidate-management single project architecture 
A model proposed for the architecture of the HR project. Frontend and Backend are built under the same project. A flow from backend to frontend is built.

# Step 0 - Setting up your environment
This demo uses:
  - Java 8
  - Maven Project (version higher than 3.1.0)
  - Latest stable release of Spring Boot
  - React.js coded in ES6. 

If you want to do it yourself, visit http://start.spring.io and pick these items:
- Rest Repositories
- Thymeleaf
- JPA
- H2
- Lombok (May want to ensure your IDE has support for this as well.)

# Step 1 - Importing and installing
1. Clone the repository in a desired location: git clone https://github.com/serban-petrescu/candidate-management.git
2. Checkout to blajv's branch: git checkout blajv
3. Import the project into your desired IDE as a maven project
4. Run maven install and wait for the dependencies to be downloaded( it could take while)
5. Create a run configuration file in the desired IDE and set the main class to "com.msg.HrmanagementApplication"
6. Run the project
7. Access http://localhost:8080/ and a table with an entry should be displayed

# Step 2 - Rebuilding and making changes
There are two ways:
 I. 
  1.Edit the "application.properties" file
  2.Replace the content of the file with:
  
      spring.data.rest.base-path=/api
      project.base-dir=file:///F:/Expert Team/Workspace/hrmanagement
      # Templates reloading during development
      spring.thymeleaf.prefix=${project.base-dir}/src/main/resources/templates/
      spring.thymeleaf.cache=false
      # Static resources reloading during development
      spring.resources.static-locations=${project.base-dir}/src/main/resources/static/
      spring.resources.cache-period=0

      Note: The path in project.base-dir is just an example and you should replace with your path
      Note: Do not push on git this file because it contains an absolute path
  3. Open a terminal and run "npm run-script watch".
      This will activate webpack in watch mode and tell it to regenerate the JS bundle every time a change is made.
      Note: For intellij users, in order for the watchmode of webpack to work(realtime changes of the static files), please go         to settings->system settings -> uncheck Use "safe write".
  4. Any change made will trigger the recompilation of the js files and you can just reload the browser to see them.
  5. For backend files, in order for changes to occur a server restart must be done
  
 II.
  1. After any change on the frontend please run:
     npm run-script webpack
  2. Restart Spring Boot
   
For more information:
Tutorial followed: https://spring.io/guides/tutorials/react-and-spring-data-rest/
Github: https://github.com/spring-guides/tut-react-and-spring-data-rest/tree/master/basic

