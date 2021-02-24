# file-upload-download
This is a web based project which is helpful for storing files at central location and accessing them across the internet.
This uses spring boot for REST APIs and JavaScript, CSS, and Html for UI part
-----------------
#How to run
-----------------
To use this project, checkout this project and compile locally using below command
mvn clean install -Dmaven.test.skip=true
Now generated jar can be used to run as spring application like below ..
 java -jar file-upload-download-0.0.1-SNAPSHOT.jar --server.port=80 --file.upload-dir=C:\download-upload
 
 server.port and file.upload-dir can be passed as per convince
