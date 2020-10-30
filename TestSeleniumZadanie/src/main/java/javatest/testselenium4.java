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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class testselenium4 {
    public static WebDriver driver;
    public String browser;

    @Parameterized.Parameters
    public static Collection getBrowser() {
        return Arrays.asList(new Object[][]{{"FF"}, {"Chrome"}});//ustawiam z jakich przeglądarek korzystam
    }
    public testselenium4(String browser) {
        this.browser = browser;
    }
    @Before
    public void setUp() throws Exception {
        if (browser.equalsIgnoreCase("FF")) {
            String dostepDoFirefox = "/home/greg/geckodriver";
            System.setProperty("webdriver.gecko.driver", dostepDoFirefox);
                driver = new FirefoxDriver();
        }
        else
        if (browser.equalsIgnoreCase("Chrome")) {
            String dostepDoChrom = "/home/greg/chromedriver";
            System.setProperty("webdriver.chrome.driver", dostepDoChrom);
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        driver.get("https://www.medicalgorithmics.pl/");
        TimeUnit.MILLISECONDS.sleep(1555);
    }

    @Test
    public void test2() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsCommand = "return document.readyState";

        Assert.assertEquals("complete", js.executeScript(jsCommand).toString());//Upewnij się, że strona Medicalgorithmics załadowała i wyświetla się poprawnie
        Assert.assertFalse("Timeout waiting for Page Load Request to complete.", false);
        Assert.assertEquals("https://www.medicalgorithmics.pl/",driver.getCurrentUrl());
        TimeUnit.MILLISECONDS.sleep(1555);
        driver.findElement(By.cssSelector(".icon_search")).click();
        driver.findElement(By.className("qode_search_field")).sendKeys("Pocket ECG CRS");//Wyszukaj frazę: “Pocket ECG CRS”, w wyszukiwarce znajdującej się na stronie
        driver.findElement(By.className("qode_search_field")).submit();
        TimeUnit.MILLISECONDS.sleep(2555);
        List<WebElement> links = driver.findElements(By.className("post_date"));//każdy wynik ma datę dodania, po niej wyszukuję
        List<WebElement> jedna_strona = driver.findElements(By.linkText("PocketECG CRS – telerehabilitacja kardiologiczna"));
        List<WebElement> nieaktywnelinki = driver.findElements(By.className("inactive"));//liczę liczbę nieaktywnych stron
        List<WebElement> aktywne = driver.findElements(By.className("active"));
        int sumalinkow = nieaktywnelinki.size()+aktywne.size();

        Assert.assertEquals(10, links.size());// Zweryfikuj, że wyszukiwanie po powyższej frazie zwraca dokładnie 10 wyników na pierwszej stronie.
        Assert.assertEquals(1, jedna_strona.size());// Zweryfikuj, że wynik wyszukiwania zwraca dokładnie 1 rezultat, w którym znajduje się fraza “PocketECG CRS – telerehabilitacja kardiologiczna"
        Assert.assertEquals(2, sumalinkow);//Zweryfikuj, że są 2 strony wyników wyszukiwania

   }
}
