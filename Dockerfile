FROM openjdk:21
EXPOSE :9090
ADD target/payment-integration.jar payment-integration.jar
LABEL authors="sreeni"

ENTRYPOINT ["java", "jar","/payment-integration.jar"]