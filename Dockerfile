FROM openjdk:20-jdk

WORKDIR /home/vehicles-rest-service

ADD target/my-url-shortener-app-*.jar my-url-shortener-app.jar

ADD ./wait-for-it.sh wait-for-it.sh

RUN bash -c "chmod 755 wait-for-it.sh"