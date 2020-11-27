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
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

enum Browser {
    CHROME,
    FF
}

@RunWith(Parameterized.class)
public class MenuColorTest {
    private static WebDriver driver;
    private final Browser browser;
    private final String downloadPath = "/home/greg/Downloads";
    private final File dir = new File(downloadPath);
    private final File[] dirContents = dir.listFiles();

    public MenuColorTest(Browser browser) {
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Iterable<Browser> getBrowser() {
        return Arrays.asList(Browser.FF, Browser.CHROME);//ustawiam z jakich przeglądarek korzystam
    }

    @Before
    public void setUp() {
        if (Browser.FF == browser) {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip");
            profile.setPreference("browser.download.manager.showWhenStarting", false);
            profile.setPreference("pdfjs.disabled", true);
            FirefoxOptions opt = new FirefoxOptions();
            opt.setProfile(profile);//ustawiam w ustawieniach Firefox że w przypadku pobierania plików typu application/zip nie wyskakuje okienko z pytaniem czy chce się zobaczyć pobrany plik czy zapisać
            driver = new FirefoxDriver(opt);
        } else if (Browser.CHROME == browser) {

            driver = new ChromeDriver();

        }
        driver.manage().window().maximize();
        driver.get("https://www.medicalgorithmics.pl/");

    }

    @Test
    public void colorCheck() throws InterruptedException {
        WebElement contactMenuItem = driver.findElement(By.id("mega-menu-item-29"));
        WebElement contactAnchor = contactMenuItem.findElement(By.tagName("a"));
        String oldColorString = contactMenuItem.getCssValue("color");
        Actions actions = new Actions(driver);
        actions.moveToElement(contactMenuItem).perform();//przesuwam kursorem żeby zmienić kolor
        TimeUnit.MILLISECONDS.sleep(1555);
        String newColorString = contactAnchor.getCssValue("color");
        Assert.assertNotEquals(oldColorString, newColorString);// zweryfikuj, że Zakładka Kontakt zmienia kolor czcionki po najechaniu,
    }

    @Test
    public void downloadFileCheck() throws InterruptedException {
        for (File i : dirContents) {
            if (i.getName().equalsIgnoreCase("logotypy.zip"))//usuwam zbędny nadmiarowy pobrany plik logotypy.zip z poprzedniej próby żeby nie robiły mi się pliki logotypy.zip(2)
            {
                i.delete();
            }
        }
        TimeUnit.MILLISECONDS.sleep(1555);
        WebElement contactMenuItem = driver.findElement(By.id("mega-menu-item-29"));
        contactMenuItem.click();
        TimeUnit.MILLISECONDS.sleep(3555);
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        TimeUnit.MILLISECONDS.sleep(3555);
        driver.findElement(By.cssSelector("div.spacer-mobile:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(1) > h3:nth-child(1) > a:nth-child(1)")).click();
        TimeUnit.MILLISECONDS.sleep(3555);
        driver.findElement(By.className("vc_single_image-img")).click();
        TimeUnit.MILLISECONDS.sleep(3555);
        File[] dirContents = dir.listFiles();//reading folder data again
        Assert.assertTrue(dirContents[dirContents.length - 1].getName().equalsIgnoreCase("logotypy.zip"));// zweryfikuj, że plik pobrał się poprawnie na lokalny komputer zakładając, że zawsze pobierany jest do folderu "Downloads".

    }
}
