# email-generator
Work assessment
To start the application you should use the next commands:
1. mvn clean install
2. docker build -t email-generator-api-image
3. docker run -p 8080:8080 email-generator-api-image
The previous commands will start the spring application and if you try
http://localhost:8080/email-generator?Input1=in&Input2=ty&Input3=ut&Input4=ru&Input5=er&expression=input1.indexOf(1) will have a response

There is a swagger implemented: 
localhost:8080/swagger-ui/index.html#/email-controller/generateEmail

To run the other services

Open another terminal and use the docker-compose up command

The provided version is without certificates.
There is a logic provided for certificates but it is commented in the nginx.conf file. If you want to use certificates just uncomented it. The certificate and certificate key are generated and located at the ssl folder.
