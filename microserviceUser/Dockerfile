# Usa uma imagem base com Java
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o .jar que está no target/
COPY target/*.jar app.jar

# Expõe a porta usada pela aplicação
EXPOSE 8080

# Comando para rodar o app
CMD ["java", "-jar", "app.jar"]