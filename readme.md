A security in mind file uploading web app
=
This a proof-of-concept project to show how to implement a secure file uploading web app using open source software & libraries: <br/>
**Build/Deploy** <br/>
Java 8 : <a href="http://openjdk.java.net/" target="_blank">http://openjdk.java.net/</a> | <a href="http://www.oracle.com/technetwork/java/index.html" target="_blank">http://www.oracle.com/technetwork/java/index.html</a> <br/>
Maven : <a href="https://maven.apache.org/" target="_blank">https://maven.apache.org/</a> <br/>
**Web app** <br/>
Spring Boot: <a href="http://projects.spring.io/spring-boot/" target="_blank">http://projects.spring.io/spring-boot/</a> <br/>
Twitter Bootstrap: <a href="http://v4-alpha.getbootstrap.com/" target="_blank">http://v4-alpha.getbootstrap.com/</a> <br/>
jQuery : <a href="http://jquery.com/" target="_blank">http://jquery.com/</a> <br/>
WebJars : <a href="http://www.webjars.org/" target="_blank">http://www.webjars.org/</a> <br/>
**Antivirus** <br/>
ClamAv : <a href="http://www.clamav.net/" target="_blank">http://www.clamav.net/</a> <br/>
clamav-client : <a href="https://github.com/cdarras/clamav-client" target="_blank">https://github.com/cdarras/clamav-client</a> <br/>
**Content-Type Detection** <br/>
Apache Tika : <a href="https://tika.apache.org/" target="_blank">https://tika.apache.org/</a> <br/>
**Helper libraries** <br/>
Google Guava : <a href="https://github.com/google/guava" target="_blank">https://github.com/google/guava</a> <br/>
Apache Commons-IO : <a href="https://commons.apache.org/proper/commons-io/" target="_blank">https://commons.apache.org/proper/commons-io/</a> <br/>
**Testing** <br/>
JUnit : <a href="http://junit.org/junit4/" target="_blank">http://junit.org/junit4/</a> <br/>
Docker : <a href="https://www.docker.com/" target="_blank">https://www.docker.com/</a> <br/>
ClamAV Docker Image : <a href="https://hub.docker.com/r/mkodockx/docker-clamav/" target="_blank">https://hub.docker.com/r/mkodockx/docker-clamav/</a> <br/>
**Documentation/Guidelines** <br/>
OWASP - Unrestricted File Upload : <a href="https://www.owasp.org/index.php/Unrestricted_File_Upload" target="_blank">https://www.owasp.org/index.php/Unrestricted_File_Upload</a> <br/> 
SANS - 8 Basic Rules to Implement Secure File Uploads : <a href="https://software-security.sans.org/blog/2009/12/28/8-basic-rules-to-implement-secure-file-uploads" target="_blank">https://software-security.sans.org/blog/2009/12/28/8-basic-rules-to-implement-secure-file-uploads</a> <br/>

#### <i class="icon-cog">Settings</i>
1.- If you haven't already done, please clone this repository
```
git clone https://github.com/immontilla/file-uploading-web-app.git
```
2.- An available and updated ClamAV daemon is a **must** to run this app. Every candidate file has to be scanned for virus to be accepted as an uploaded file. Keeping in mind this is just a PoC, I suggest you to run a ClamAV docker image.
```
docker pull mkodockx/docker-clamav
docker run -d -p 3310:3310 mkodockx/docker-clamav
```
If you prefer a local installation, run this:
```
sudo apt-get update && sudo apt-get install clamav clamav-daemon
```
And don't forget to **configure the ClamAV daemon**:
```
sudo dpkg-reconfigure clamav-base
```
Finally, check its status with:
```
sudo /etc/init.d/clamav-daemon status
```
3.- <i class="icon-pencil"></i> Review src/main/resources/application.properties file before building

> server.port=8090 <br/>
> spring.http.multipart.max-file-size=3MB <br/>
> spring.http.multipart.max-request-size=3MB <br/>
> temp.path=/tmp/unsafe <br/>
> file.path=/tmp/safe <br/>
> clam.av.server.host=localhost <br/>
> clam.av.server.port=3310 <br/>
> clam.av.server.platform=UNIX <br/>
> logging.level.org.springframework=INFO <br/>
> logging.level.eu.immontilla.poc=DEBUG <br/>

- *server.port* is the port number the embedded Tomcat server will be listening on.
- *spring.http.multipart.max-file-size* and *spring.http.multipart.max-request-size* set the maximum accepted file size.
- *temp.path* and *file.path* are the locations paths where uploaded files will be stored. **Important** If you are planning to run this web app in a Windows machine, the back-slash \ has to be escaped with double slashes //
- *clam.av.server.host*, *clam.av.server.port* and *clam.av.server.platform* are the host and port the ClamAV daemon is listening on whereas platform is its operating system type. Platform could it be: UNIX, WINDOWS or JVM_PLATFORM. Default values belong to a local Linux installation.
- *logging.level.org.springframework* and *logging.level.eu.immontilla.poc* are just logback levels.

4.- Run a mvn clean install to see if everything is OK including the tests.
```
mvn clean install
```

#### <i class="icon-upload"> Run & Deploy</i>
```
mvn spring-boot:run
```

#### <i class="icon-refresh"> Additional Testing </i>
Use the /src/test/resources files to perform some *curl* back-end testing
```
curl -i -X POST -F file=@fake-csv-it-is-a-png-file.csv http://localhost:8090/upload
```
produces a 400 Bad Request response because fake-csv-it-is-a-png-file.csv is a PNG image file with a fake extension:
```
HTTP/1.1 100 

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 27 Jul 2017 19:11:03 GMT
Connection: close

{"virusFree":true,"failed":true,"validFileExtension":true,"validContentType":false}
```
```
curl -i -X POST http://localhost:8090/upload
```
produces a 415 Unsupported media type because there is no file:
```
HTTP/1.1 415 
Accept: multipart/form-data
Content-Length: 0
Date: Thu, 27 Jul 2017 19:13:29 GMT
```

```
curl -i -X POST -F file=@libre-office-csv-file.csv http://localhost:8090/upload
```
produces a 200 OK response code because libre-office-csv-file.csv is a valid virus-free file:
```
HTTP/1.1 100 

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 27 Jul 2017 19:17:36 GMT

{"virusFree":true,"failed":false,"validFileExtension":true,"validContentType":true}
```


```
curl -i -X POST -F file=@FL_insurance_sample.csv http://localhost:8090/upload
```
produces a 413 Payload too large response code because FL_insurance_sample.csv size' is bigger than 3Mb:
```
HTTP/1.1 100 

HTTP/1.1 413 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 27 Jul 2017 19:18:52 GMT
Connection: close

{"validFileSize":false,"failed":true}
```

#### <i class="icon-refresh"> Open the web app in your browser </i>

<a href="http://localhost:8090/" target="_blank">http://localhost:8090/</a>


