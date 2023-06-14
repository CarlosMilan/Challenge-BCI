# desafio-bci

Este proyecto fue desarrollado usando
* Java 8
* Gradle 7.6.1

#### Para poder construir el ejecutable, se debe ejecutar el siguiente comando mendiante terminal en el directorio del proyecto

``` $ ./gradlew clean build ```

#### Para levantar el proyecto, simplemente es necesario ejecutar

``` $ ./gradlew bootrun ```

#### Cobertura de pruebas unitarias
Se utilizó Jacoco para generar informe de cobertura de pruebas unitarias
Ejecutar el comando:

``` $ ./gradlew clean build jacocoTestReport```

El reporte es generado en build/JacocoHtml/index.html

### Diagramas de secuencia



#### Sing-up

![Diagrama de secuencua sing-up](https://github.com/CarlosMilan/Challenge-BCI/blob/develop/Diagramas/Sing-up.jpg)

#### Login

![Diagrama de secuencua login](https://github.com/CarlosMilan/Challenge-BCI/blob/develop/Diagramas/Login.svg.jpg)