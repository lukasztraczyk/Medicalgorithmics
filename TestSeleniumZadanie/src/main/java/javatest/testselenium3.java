
package javatest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class testselenium3 {
    public static WebDriver driver;
    public String browser;
    String downloadPath = "/home/greg/Downloads";
    File dir = new File(downloadPath);
    File[] dirContents = dir.listFiles();

    @Parameterized.Parameters
    public static Collection getBrowser() {
        return Arrays.asList(new Object[][]{{"FF"}, {"Chrome"}});//ustawiam z jakich przeglądarek korzystam
    }

    public testselenium3(String browser) {
        this.browser = browser;
    }

    @Before
    public void setUp() throws Exception {
        if (browser.equalsIgnoreCase("FF")) {
            String dostepDoFirefox = "/home/greg/geckodriver";
            System.setProperty("webdriver.gecko.driver", dostepDoFirefox);
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("browser.download.folderList", 2);
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip");
            profile.setPreference("browser.download.manager.showWhenStarting", false);
            profile.setPreference("pdfjs.disabled", true);
            FirefoxOptions opt = new FirefoxOptions();
            opt.setProfile(profile);//ustawiam w ustawieniach Firefox że w przypadku pobierania plików typu application/zip nie wyskakuje okienko z pytaniem czy chce się zobaczyć pobrany plik czy zapisać
            driver = new FirefoxDriver(opt);
        }
        else
        if (browser.equalsIgnoreCase("Chrome")) {
        String dostepDoChrom = "/home/greg/chromedriver";
            System.setProperty("webdriver.chrome.driver", dostepDoChrom);
            driver = new ChromeDriver();
        }
        if (dirContents[dirContents.length - 1].getName().equalsIgnoreCase("logotypy.zip"))//usuwam zbędny nadmiarowy pobrany plik logotypy.zip z poprzedniej próby żeby nie robiły mi się pliki logotypy.zip(2)
        {
            dirContents[dirContents.length - 1].delete();
        }
        driver.manage().window().maximize();
        driver.get("https://www.medicalgorithmics.pl/");
        Thread.sleep(2000);
    }

    @Test
    public void test1() throws InterruptedException {

        WebElement newBtn = driver.findElement(By.xpath("/html/body/div[3]/div/header/div/div/div/nav[1]/div/ul/li[4]/a"));
        String s1=newBtn.getCssValue("color");
        Actions actions = new Actions(driver);
        actions.moveToElement(newBtn).perform();//przesuwam kursorem żeby zmienić kolor
        Thread.sleep(7000);
        String s2=newBtn.getCssValue("color");


        String StrNew1 = s1.replace("a", "");
        String strNew2 = s2.replace("a", "");//fierfox ma rgb a chrome rgba kolor
        Assert.assertNotEquals(StrNew1,strNew2);// zweryfikuj, że Zakładka Kontakt zmienia kolor czcionki po najechaniu,

        newBtn.click();
        Thread.sleep(4000);
        driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[2]/div/div[7]/div/div/div/div/div/div[4]/div/div/div/div[1]/div/div/div[1]/div/a/div/img")).click();
            Thread.sleep(2000);
        driver.findElement(By.className("vc_single_image-img")).click();
        Thread.sleep(7000);

        {
            Assert.assertEquals(true, dirContents[dirContents.length - 1].getName().equalsIgnoreCase("logotypy.zip"));// zweryfikuj, że plik pobrał się poprawnie na lokalny komputer zakładając, że zawsze pobierany jest do folderu "Downloads".

    }
    }
}



