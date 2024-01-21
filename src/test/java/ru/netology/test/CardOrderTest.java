package ru.netology.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldTestValidName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Тестов Тест Тестович");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        assertEquals("  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText());
    }

    @Test
    public void shouldHaveNameFieldFilled() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        assertEquals("Поле обязательно для заполнения", driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText());
    }

    @Test
    public void withoutLatinCharacters() {
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Testov Test Testovich");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.className("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", driver.findElement(By.cssSelector("[data-test-id = name].input_invalid .input__sub")).getText());
    }

    @Test
    public void longPhoneNumber() {
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Тестов Тест Тестович");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+799912345678");
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.className("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", driver.findElement(By.cssSelector("[data-test-id = phone].input_invalid .input__sub")).getText());
    }

    @Test
    public void shouldHavePhoneFieldFilled() {
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Тестов Тест Тестович");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).clear();
        driver.findElement(By.cssSelector("[data-test-id = agreement]")).click();
        driver.findElement(By.className("button")).click();
        assertEquals("Поле обязательно для заполнения", driver.findElement(By.cssSelector("[data-test-id = phone].input_invalid .input__sub")).getText());
    }

    @Test
    void CheckboxIsChecked() {
        driver.findElement(By.cssSelector("[data-test-id = name] input")).sendKeys("Тим Ирина");
        driver.findElement(By.cssSelector("[data-test-id = phone] input")).sendKeys("+79109105566");
        driver.findElement(By.className("button")).click();
        boolean actual = driver.findElement(By.cssSelector("[data-test-id = agreement].input_invalid .checkbox__text")).isDisplayed();
        assertTrue(actual);
    }
}