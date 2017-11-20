A security in mind file uploading web app - Docker version
=
This branch let you build a Docker image. You can run the web app in a Docker container or in a server.

A ClamAV server is necessary to build and run both versions. If you run ClamAV from a Docker image, you have to add it to the Docker *host* network.
```
docker pull mkodockx/docker-clamav
docker run -itd --name avserver --net=host -p 3310:3310 mkodockx/docker-clamav
```
 
#### <i class="icon-upload"> Run & Deploy in a server</i>

Edit pom.xml and change the docker.image.prefix property value, the run

```
mvn spring-boot:run
```
#### <i class="icon-upload"> Build & Run in a Docker container</i>
```
mvn clean install dockerfile:build 
```

change ??? to the docker.image.prefix pom.xml property value

```
docker run -itd --name=secure_upload --net=host -p 8090:8090 ???/secure-upload 
```
Please find a detailed explanation at <a href="https://blog.immontilla.eu/running-a-secure-file-uploading-web-app-in-a-docker-container/" title="Running a secure file uploading web app in a Docker container" target="_blank">Running a secure file uploading web app in a Docker container</a>
