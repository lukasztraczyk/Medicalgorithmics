package javatest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class NumberCountCheck {
    private static WebDriver driver;
    private final Browser browser;

    public NumberCountCheck(Browser browser) {
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Iterable<Browser> getBrowser() {
        return Arrays.asList(Browser.FF, Browser.CHROME);//ustawiam z jakich przeglądarek korzystam
    }

    @Before
    public void setUp() throws Exception {
        if (Browser.FF == browser) {
            driver = new FirefoxDriver();
        } else if (Browser.CHROME == browser) {
            driver = new ChromeDriver();
        }

        driver.manage().window().maximize();
        driver.get("https://www.medicalgorithmics.pl/");
    }

    @Test
    public void test2() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsCommand = "return document.readyState";
        Assert.assertEquals("complete", js.executeScript(jsCommand).toString());//Upewnij się, że strona Medicalgorithmics załadowała i wyświetla się poprawnie
        Assert.assertEquals("https://www.medicalgorithmics.pl/", driver.getCurrentUrl());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        TimeUnit.MILLISECONDS.sleep(444);
        driver.findElement(By.cssSelector(".icon_search")).click();
        driver.findElement(By.className("qode_search_field")).sendKeys("Pocket ECG CRS");//Wyszukaj frazę: “Pocket ECG CRS”, w wyszukiwarce znajdującej się na stronie
        driver.findElement(By.className("qode_search_field")).submit();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("PocketECG CRS – telerehabilitacja kardiologiczna")));//czekam az strona sie wczyta
        List<WebElement> links = driver.findElements(By.className("post_date"));//każdy wynik ma datę dodania, po niej wyszukuję
        List<WebElement> jedna_strona = driver.findElements(By.linkText("PocketECG CRS – telerehabilitacja kardiologiczna"));
        List<WebElement> nieaktywnelinki = driver.findElements(By.className("inactive"));//liczę liczbę nieaktywnych stron
        List<WebElement> aktywne = driver.findElements(By.className("active"));
        int sumalinkow = nieaktywnelinki.size() + aktywne.size();
        Assert.assertEquals(10, links.size());// Zweryfikuj, że wyszukiwanie po powyższej frazie zwraca dokładnie 10 wyników na pierwszej stronie.
        Assert.assertEquals(1, jedna_strona.size());// Zweryfikuj, że wynik wyszukiwania zwraca dokładnie 1 rezultat, w którym znajduje się fraza “PocketECG CRS – telerehabilitacja kardiologiczna"
        Assert.assertEquals(2, sumalinkow);//Zweryfikuj, że są 2 strony wyników wyszukiwania

    }
}


