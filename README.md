# simple-shop-api
Simple shop API created by spring boot, spring security, spring data!

I create a Spring Boot project(Rest APIs) for a simple shop. You need the following tools and technologies to develop the same.
- Spring-Boot 2.5.5.RELEASE
- Spring-Security 2.5.5.RELEASE
- Spring-Data 2.5.5.RELEASE
- Springfox-swagger2 2.9.2
- Lombok 1.18.6
- MapStruct 1.0.3.FINAL
- JavaSE 11
- Maven 3.3.9

# Dependencies
Open the pom.xml file for spring-aop configuration:

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
      </parent>
      
and dpendencies:

       <!-- SPRING -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <!-- Starter for using Spring Security -->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
       <!-- PROJECT LOMBOK -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!-- MAPSTRUCT -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>


# Features

1. User sign-in signup. The application should have 2 types of roles, ADMIN, and USER.
2. Admin functionality -> CRUD of products per category, block unblock the users.
3. Users be able to search for products per product name, price range, rate, etc.
4. Users be able to rate the product(1-5), leave a comment on it.


Also I make properties files for development and deployment environment and set the profile in the application accordingly, so it will pick the respective properties file.
