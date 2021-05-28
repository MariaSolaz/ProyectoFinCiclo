# MY MECANIC

_Este proyecto consiste en una plicación para facilitar la comunicación entre un taller mecánico y sus clientes._

# Comenzando

_Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas._


## Pre-requisitos

_Sofware necesario para el buen funcionamiento del proyecto:_

* [NodeJs V14.17.](https://nodejs.org/es/) - Entorno de ejecución para JavaScript construido con el motor de JavaScript V8 de Chrome.
* [PostgreSql](https://www.postgresql.org/download/) - Gestor de base de datos.
* [IntelliJ](https://www.jetbrains.com/es-es/idea/) - Entorno de desarrollo integrado.
* [Visual Estudio Code](https://code.visualstudio.com/) - Entorno de desarrollo.
* Angular Cli.

```
npm install -g @angular/cli
```
* Ionic Cli.
```
npm install -g @ionic/cli
```
* Jhipster
```
npm install -g generator-jhipster
```


# Puesta en marcha

_Pasos para ejecutar la aplicación _

#### BACKEND

1. Abrimos la carpeta backend con IntelliJ.
2. Confiamos en el proyecto.
3. Nos dirigimos a la ruta /src/main/resources/config/application-dev.yml y midificamos los campos datasource: Url, username y password (datos de tu base de datos.)

```
 datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/mlpredictive
    username: postgres
    password: *******
```
#### FRONT
1. Abrimos la carpeta front con Visual Estudio Code.
2. Desde la consola iniciaremos la aplicación.

```
npm strat
```

#### APP

1. Abrimos la carpeta en una nueva ventana de Visual Estudio Code.
2. Desde la consola iniciaremos la app.

```
ionic serve
```

Una vez esté todo en marcha desde el navegador podrá visualizar los resultados 
* Backend: http://localhost:8080/
* Front: http://localhost:9000/
* App: htpps://localhost:8100/

# Pruebas
 _Para poder hacer pruebas puedes usar los siguientes usuarios de prueba._
 #### Front
 * User: admin Password:admin.
 * User: user Password: user.

 #### App
 * Login: Carlos Password: Carlos1234

 # Autores
 **Maria Solaz** 