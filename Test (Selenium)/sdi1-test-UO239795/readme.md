# Ejecución de Tests en SDI1-UO239795

## Requisitos

Antes de ejecutar los tests, asegúrate de tener instalados los siguientes componentes:

- **Firefox** (Versión 46 o compatible)
- **Geckodriver** (para Selenium WebDriver)

## Instalación y Configuración

1. Configura el `PathFirefox` en `Sdi1UO239795Test.java` si es necesario:

   ```java
   private static String PathFirefox = "../Firefox46.win/FirefoxPortable.exe";
   ```

   Asegúrate de que la ruta apunta a una instalación válida de Firefox.

2. Descarga y configura `Geckodriver`:
   - Descarga desde: [https://github.com/mozilla/geckodriver/releases](https://github.com/mozilla/geckodriver/releases)
   - Agrega el ejecutable a la variable de entorno o colócalo en el directorio del proyecto.

## Ejecución de los Tests

Para ejecutar los tests, usa el siguiente comando:

```sh
java -cp "bin;selenium-server-standalone-2.53.0.jar" org.junit.runner.JUnitCore com.uniovi.test.Sdi1UO239795Test
```

Si deseas ejecutar un test específico, puedes hacerlo desde tu entorno de desarrollo o con JUnit en línea de comandos.

## Estructura de los Tests

Los tests se encuentran en el paquete `com.uniovi.test` y validan las siguientes funcionalidades:

- **Registro de usuarios**
- **Inicio de sesión**
- **Gestión de usuarios**
- **Publicaciones y amigos**

Los tests usan **Selenium WebDriver** para automatizar inter
