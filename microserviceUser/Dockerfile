# Usa imagem base do Java 17
FROM eclipse-temurin:17-jdk-alpine

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o .jar gerado pelo Maven
COPY target/*.jar app.jar

# Expõe a porta (ajuste conforme seu app)
EXPOSE 8080

# Comando para iniciar o app
ENTRYPOINT ["java", "-jar", "app.jar"]