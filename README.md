# QueryDSL + Flyway
Springboot + SpringJPA + Flyway + QueryDSL. Generating sources.

## About
I've been working on projects who have issues using both Flyway and QueryDSL together.

First of all. They make use of an sql with the hole schema defined in the migration files! They are duplicating the same schema just for generating the QueryDSL files.

It's nonsense!

So. This project is an example of how to join their forces for migrate and aldo generate the QueryDSL code.

## 1. How does it work?
With the flyway plugin, it migrates to the target database configured in the profile.
Then the querydsl opens the migrated database and generates the code.

You must insert in a pom.xml the flyway-maven-plugin and configure as usual;
Insert the querydsl-maven-plugin and configure as usual.

After that, lets create a profile. A dev profile for example.

Config properties for this profile that uses an specific database and also an dependency for it`s db:
```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <plugin.flyway.url>jdbc:h2:file:./target/db/data;DATABASE_TO_UPPER=FALSE</plugin.flyway.url>
            <plugin.flyway.usr>sa</plugin.flyway.usr>
            <plugin.flyway.pwd>sa</plugin.flyway.pwd>
            <dependency.db.groupId>com.h2database</dependency.db.groupId>
            <dependency.db.artifactId>h2</dependency.db.artifactId>
            <dependency.db.version>1.4.200</dependency.db.version>
        </properties>
        <dependencies>
            <dependency>
                <groupId>${dependency.db.groupId}</groupId>
                <artifactId>${dependency.db.artifactId}</artifactId>
                <version>${dependency.db.version}</version>
                <scope>runtime</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```
They will be used for flyway-maven-plugin and querydsl-maven-plugin.

### 1.1 Adding new profile
For other db, create new profile like this:
```xml
<profiles>
    <profile>
        <id>dev</id>
        ...
    </profile>
    <profile>
        <id>mysql</id>
        <properties>
            <plugin.flyway.url>jdbc:mysql://localhost/data</plugin.flyway.url>
            <plugin.flyway.usr>sa</plugin.flyway.usr>
            <plugin.flyway.pwd>sa</plugin.flyway.pwd>
            <dependency.db.groupId>mysql</dependency.db.groupId>
            <dependency.db.artifactId>mysql-connector-java</dependency.db.artifactId>
            <dependency.db.version>8.0.19</dependency.db.version>
        </properties>
        <dependencies>
            <dependency>
                <groupId>${dependency.db.groupId}</groupId>
                <artifactId>${dependency.db.artifactId}</artifactId>
                <version>${dependency.db.version}</version>
                <scope>runtime</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

### 2.1 Let's configure the flyway plugin
At this point, you must add the flyway-maven-plugin global plugin.

The profiles for each db has their own properties used for the global plugin.

So, plugin.flyway.url, plugin.flyway.usr and plugin.flyway.pwd are for database configuration.
And dependency.db.groupId, dependency.db.artifactId and dependency.db.version are for the database dependency.

Example:
```xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>6.3.3</version>
    <configuration>
        <url>${plugin.flyway.url}</url>
        <user>${plugin.flyway.usr}</user>
        <password>${plugin.flyway.pwd}</password>
    </configuration>
    <executions>
        <execution>
            <id>process-resources2</id>
            <goals>
                <goal>clean</goal>
                <goal>migrate</goal>
            </goals>
            <phase>generate-sources</phase>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>${dependency.db.groupId}</groupId>
            <artifactId>${dependency.db.artifactId}</artifactId>
            <version>${dependency.db.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

### 2.2 Let's configure the query-dsl plugin
You must add the query dsl plugin and use profile's properties the same way you did previously.

Example:
```xml
<plugin>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-maven-plugin</artifactId>
    <version>4.3.1</version>
    <executions>
        <execution>
            <goals>
                <goal>export</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <jdbcDriver>org.h2.Driver</jdbcDriver>
        <jdbcUrl>${plugin.flyway.url}</jdbcUrl>
        <packageName>io.github.barbatchov.querydsl</packageName>
        <schemaToPackage>false</schemaToPackage>
        <namePrefix>S</namePrefix>
        <targetFolder>${project.basedir}/target/generated-sources/java</targetFolder>
        <sourceFolder/>
        <jdbcUser>${plugin.flyway.usr}</jdbcUser>
        <jdbcPassword>${plugin.flyway.pwd}</jdbcPassword>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>${dependency.db.groupId}</groupId>
            <artifactId>${dependency.db.artifactId}</artifactId>
            <version>${dependency.db.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

## What else?
I know this is a sample for show how generate querydsl code using the migrated database.

There are not an example for disable it an another profile. 
But it is just a beginning.

Feel free for get this code and test as you which.

Thank you.