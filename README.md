# RaspberryPi
## Build the Project
To build the project maven (https://maven.apache.org/) is needed. The following command has to be executed in the main folder:

    mvn package

A jar will be build and can be found in the target folder.
## Running the Project
First, this project works only with redis (https://redis.io/). Therefore redis has to be executed before executing this project.
Redis should be started with the default configuration (Port:6379). When it is running,the jar can be executed and your good to go.
