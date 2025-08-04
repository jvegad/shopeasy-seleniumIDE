# Análisis de Caso: Pruebas Funcionales con Selenium IDE

#### Entregable para: ShopEasy
#### Alumna: Josselyn Vega Devia

---

## Situación inicial 📍

Una pequeña empresa de comercio electrónico, ShopEasy, necesita validar de forma rápida y eficiente sus flujos de usuario en la aplicación web. Debido a la necesidad de prototipar y ajustar casos de prueba sin mucha complejidad, se decide utilizar **Selenium IDE** para grabar las interacciones. Posteriormente, se exportará el caso de prueba a código Java para realizar ajustes y mejoras en **Visual Studio Code**. La solución se basa en los contenidos del manual “SELENIUM IDE”.

## Descripción del Caso 🔎

Como tester en ShopEasy, tu misión es grabar un caso de prueba funcional con Selenium IDE que cubra un flujo básico (por ejemplo, iniciar sesión y navegar por el catálogo de productos). Luego, exportar el script a código Java, integrarlo en Visual Studio Code y agregar mejoras (como esperas explícitas y validaciones adicionales). El caso debe demostrarse utilizando únicamente los conocimientos adquiridos en el manual.

## Instrucciones de Configuración y Ejecución
A continuación se detallan los pasos para clonar, compilar y probar el proyecto.

Prerrequisitos
JDK 17 (o compatible) instalado.
Apache Maven instalado y configurado en el PATH del sistema.
Git instalado.
Pasos para la Ejecución
Clonar el repositorio:

git clone https://github.com/jvegad/shopeasy-seleniumIDE
cd shopeasy-seleniumIDE
Compilar y ejecutar las pruebas: El siguiente comando limpiará compilaciones anteriores, compilará el código y ejecutará la suite de pruebas definida en testing.xml.

mvn clean test
Al finalizar, deberías ver un BUILD SUCCESS en la consola.

---

## 1. Análisis de Selenium IDE

### Ventajas de Selenium IDE

*   **Curva de aprendizaje baja:** Permite a usuarios sin experiencia en programación crear pruebas automatizadas de forma rápida y visual. Su interfaz de *grabar y reproducir* (record-and-playback) es muy intuitiva.
*   **Creación rápida de prototipos:** Es ideal para generar rápidamente borradores de casos de prueba que luego pueden ser refinados. Acelera la fase inicial de la automatización.
*   **Extensibilidad y Portabilidad:** Los casos de prueba pueden ser exportados a lenguajes de programación como `Java`, `Python` o `C#`, permitiendo su integración en frameworks de prueba más robustos como `JUnit` o `TestNG`.
*   **Feedback Inmediato:** Al ejecutar las pruebas directamente en el navegador, se obtiene una retroalimentación visual e instantánea del comportamiento de la aplicación.

### Limitaciones de Selenium IDE

*   **Falta de lógica compleja:** No permite la implementación de estructuras de control complejas (bucles avanzados, condicionales complejos) o la manipulación de datos externos (leer de archivos Excel, bases de datos) de forma nativa.
*   **Localizadores frágiles:** Por defecto, Selenium IDE a veces genera localizadores (`XPath`, selectores CSS) muy largos y frágiles que dependen en exceso de la estructura del `DOM`. Estos localizadores son propensos a romperse con pequeños cambios en la UI.
*   **Mantenimiento difícil a escala:** Gestionar una gran cantidad de pruebas exclusivamente en Selenium IDE se vuelve complicado. No está diseñado para ser la herramienta principal en proyectos de automatización grandes y a largo plazo.
*   **No es ideal para pruebas no funcionales:** No es adecuado para pruebas de carga, rendimiento o seguridad.

---

## 2. Descripción del Flujo Grabado en Selenium IDE

Para el caso de ShopEasy, se grabó el siguiente flujo funcional:

1.  **Abrir Navegador:** Se abre la página principal y se navega a la sección de Login/Signup.
2.  **Iniciar Registro:** En la sección `"New User Signup!"`, se introduce un nombre y una dirección de correo electrónico única. Se hace clic en el botón `"Signup"`.
3.  **Validar Redirección:** Se verifica que la página ha redirigido al formulario `"ENTER ACCOUNT INFORMATION"`.
4.  **Completar Formulario:** Se rellenan los campos obligatorios del formulario de registro (género, contraseña, nombre, dirección, etc.).
5.  **Enviar Formulario:** Se hace clic en el botón `"Create Account"`.
6.  **Validar Creación:** Se verifica que aparece el mensaje `"ACCOUNT CREATED!"` en la siguiente página.
7.  **Continuar:** Se hace clic en el enlace `"Continue"` para finalizar el flujo.

---

## 3. Código Exportado y Modificado en Visual Studio Code

A continuación, se presenta el código Java exportado de Katalon Recorder (Selenium IDE) y posteriormente refactorizado en VS Code para usar **JUnit 5**, añadiendo **esperas explícitas** y **aserciones** para mayor robustez.

````java
package tests;

// Importaciones de JUnit 5 (en lugar de TestNG)
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Importaciones de Selenium
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

// Importación de WebDriverManager
import io.github.bonigarcia.wdm.WebDriverManager;

// Importación para la espera explícita
import java.time.Duration;

public class LoginRegistroTest {

    private WebDriver driver;

    // Se ejecuta ANTES de CADA @Test
    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void registroTestCase() {
        driver.get("https://www.automationexercise.com/login");

        // --- Mejora 1: Usar esperas explícitas para mayor robustez ---
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Rellenar formulario de nuevo usuario
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys("test");
        // Se usa System.currentTimeMillis() para generar un email único en cada ejecución
        driver.findElement(By.xpath("//form[@action='/signup']//input[@name='email']")).sendKeys("testuser" + System.currentTimeMillis() + "@test.com");
        driver.findElement(By.xpath("//button[text()='Signup']")).click();
        
        // --- Mejora 2: Validación explícita (Aserción) ---
        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[text()='Enter Account Information']")));
        assertTrue(pageTitle.isDisplayed(), "No se redirigió a la página de creación de cuenta.");

        // Rellenar detalles de la cuenta
        driver.findElement(By.id("id_gender2")).click();
        driver.findElement(By.id("password")).sendKeys("987654");
        new Select(driver.findElement(By.id("days"))).selectByVisibleText("1");
        new Select(driver.findElement(By.id("months"))).selectByVisibleText("January");
        new Select(driver.findElement(By.id("years"))).selectByVisibleText("2000");
        driver.findElement(By.id("first_name")).sendKeys("test");
        driver.findElement(By.id("last_name")).sendKeys("user");
        driver.findElement(By.id("address1")).sendKeys("avda siempre viva");
        new Select(driver.findElement(By.id("country"))).selectByVisibleText("United States");
        driver.findElement(By.id("state")).sendKeys("Ohio");
        driver.findElement(By.id("city")).sendKeys("Ohio City");
        driver.findElement(By.id("zipcode")).sendKeys("22334455");
        driver.findElement(By.id("mobile_number")).sendKeys("+166778899");

        driver.findElement(By.xpath("//button[text()='Create Account']")).click();
        
        // --- Mejora 3: Validación final del flujo ---
        WebElement accountCreatedTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[text()='Account Created!']")));
        assertTrue(accountCreatedTitle.isDisplayed(), "El mensaje de cuenta creada no apareció.");

        driver.findElement(By.linkText("Continue")).click();
    }
    
    // Se ejecuta DESPUÉS de CADA @Test
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```
## 4. Reflexión Final sobre la Experiencia

Utilizar **Selenium IDE** como punto de partida para la automatización de pruebas es una estrategia muy efectiva. La capacidad de grabar un flujo de usuario en minutos proporciona una base de código funcional que, de otro modo, llevaría mucho más tiempo escribir desde cero. Esta fase inicial de "prototipado rápido" es, sin duda, la mayor fortaleza de la herramienta.

Sin embargo, la experiencia también deja claro que el código exportado rara vez está listo para producción. La verdadera tarea comienza en el entorno de desarrollo (`Visual Studio Code`), donde se debe refinar el script. La integración de **esperas explícitas** (`WebDriverWait`) es el paso más crucial para transformar una prueba frágil en una robusta, eliminando fallos intermitentes causados por tiempos de carga de la página.

Además, añadir **validaciones explícitas** (`Assertions`) en puntos clave del flujo permite que la prueba no solo "ejecute pasos", sino que realmente "verifique resultados", que es el verdadero objetivo del *testing*.

En conclusión, la combinación de Selenium IDE y un IDE de código como VS Code ofrece un flujo de trabajo potente: **rapidez, simplicidad y la flexibilidad de la programación al final**. Es una demostración de cómo una herramienta sencilla sirve como una excelente herramienta para proyectos de automatización más serios y mantenibles.