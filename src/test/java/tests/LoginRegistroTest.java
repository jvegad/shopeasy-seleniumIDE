package tests;

// Importaciones de JUnit 5 
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginRegistroTest {

    private WebDriver driver;

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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys("test");
        driver.findElement(By.xpath("//form[@action='/signup']//input[@name='email']")).sendKeys("testuser" + System.currentTimeMillis() + "@test.com");
        driver.findElement(By.xpath("//button[text()='Signup']")).click();

        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[text()='Enter Account Information']")));
        assertTrue(pageTitle.isDisplayed(), "No se redirigió a la página de creación de cuenta.");

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

        WebElement accountCreatedTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//b[text()='Account Created!']")));
        assertTrue(accountCreatedTitle.isDisplayed(), "El mensaje de cuenta creada no apareció.");

        driver.findElement(By.linkText("Continue")).click();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
