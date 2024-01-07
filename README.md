# Web Chat App

Este repositorio contiene una aplicación de chat web desarrollada con Spring Boot (servidor) y Next.js (cliente).

## Servidor Spring Boot

### Requisitos previos
- Java 17 o superior
- Maven
- PostgreSql

### Base de datos y configuración

1. Crea una base de datos en postgreSql
2. Crea un archivo `application.propperties` en `src/main/resources/` haciendo uso del template proporcionado `application.properties.template`. Allí podrás modificar el datasource y las opciones de seguridad.

### Instrucciones de ejecución

1. Navega a la carpeta del servidor:
    ```
    cd server
    ```

2. Compila el proyecto con Maven:
    ```
    mvn clean install
    ```

3. Ejecuta el servidor Spring Boot:
    ```
    java -jar target/chat-0.0.1-SNAPSHOT.jar
    ```

El servidor estará disponible en [http://localhost:8080](http://localhost:8080).
