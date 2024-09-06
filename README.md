Gestión de Personas y Transacciones

Descripción del Proyecto

Esta aplicación gestiona la carga de datos de personas y la sincronización con una API externa (CRM). 
Además, permite realizar transferencias de créditos entre personas y consultar cantidad de créditos transferidos entre fechas especificas.

Requisitos Previos

Antes de levantar el proyecto, asegúrate de tener instalados los siguientes componentes:
1.	Java 17 o superior: El proyecto utiliza características modernas de Java, por lo que se recomienda usar al menos Java 17.
2.	Maven: Asegúrate de tener instalado Apache Maven para gestionar las dependencias.
3.	MySQL: Se utiliza una base de datos MySQL para almacenar los datos de personas y transacciones.

Dependencias

El proyecto incluye las siguientes dependencias principales:
•	Spring Boot: Para el manejo de los servicios REST y la configuración del servidor.
•	Spring Data JPA: Para el acceso a la base de datos.
•	Jackson: Para el manejo de la serialización y deserialización de JSON.
•	MySQL: Para la persistencia de datos en la base de datos.

Instalación y Configuración

1.	Accede a tu instancia de MySQL y crea la base de datos: sql
Copiar código
CREATE DATABASE gestion_personas;

2.	Configura las credenciales de la base de datos en el archivo src/main/resources/application.properties:

Copiar código
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_personas
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

3. Construir el proyecto

Para construir el proyecto, navega al directorio raíz del proyecto y ejecuta el siguiente comando:
Bash Copiar código
mvn clean install
Este comando descargará las dependencias y generará el archivo jar ejecutable.

4. Ejecutar la aplicación

Para ejecutar la aplicación, usa el siguiente comando:
bash
Copiar código
mvn spring-boot:run

5. Uso de la API

Sincronizar Personas desde un archivo CSV
Puedes subir un archivo CSV con los datos de personas utilizando el siguiente endpoint:
•	Endpoint: /gestion-personas/sincronizar
•	Método: POST
•	Parámetro: MultipartFile con el archivo .csv que contiene los datos de las personas.
Bash Copiar código
curl -F "archivo=@personas.csv" http://localhost:8080/gestion-personas/sincronizar

Realizar una transferencia de créditos
Puedes realizar una transferencia de créditos entre dos personas utilizando el siguiente endpoint:
•	Endpoint: /gestion-personas/transferencia
•	Método: POST
•	Parámetros:
o	idEmisor: ID de la persona que transfiere los créditos.
o	creditos: Cantidad de créditos a transferir.
o	idReceptor: ID de la persona que recibe los créditos.
bash
Copiar código
curl -X POST "http://localhost:8080/gestion-personas/transferencia?idEmisor=1&idReceptor=2&creditos=100"
Informe de Transferencias

Puedes consultar un informe de las transacciones realizadas entre dos fechas:
•	Endpoint: /gestion-personas/informe-transferencias
•	Método: GET
•	Parámetros:
o	fechaInicio: Fecha de inicio (formato yyyy-MM-dd HH:mm:ss).
o	fechaFin: Fecha de fin (formato yyyy-MM-dd HH:mm:ss).
bash
Copiar código
curl "http://localhost:8080/gestion-personas/informe-transferencias?fechaInicio=2024-09-01%2010:00:00&fechaFin=2024-09-05%2018:00:00"

6. Pruebas
Puedes realizar pruebas  en Curl o en Posatman se adjunta modelo de archivo .csv, Los encabezados del archivo son obligatorios, los campos: ID,  NOMBRE, MAIL y CREDITOS, son obligatorios.
 Para establecer el resto de los valores en null simplemente completar los 4 primeros campos,
Si  se desea solo alguno de los campos nulos después de los obligatorio solo colocar comas en el lugar del campo

7. Consideraciones
•	La API utiliza Jackson para procesar los archivos CSV y realizar las conversiones de JSON.
•	Las transacciones entre personas se registran automáticamente, y cualquier error se maneja de forma explícita en los logs.
Tecnologías Usadas
•	Java 17
•	Spring Boot 3.x
•	MySQL
•	Maven
•	Jakarta EE
