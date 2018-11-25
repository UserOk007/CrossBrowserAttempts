package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

/**
 * Lecture 4
 */
public class FrequentOperations {

    public static WebDriver driver;

@Parameters("browser")
    @BeforeTest
    public static WebDriver getDriver(String browser) {
    if (browser.equals("chrome")) {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "//resources//chromedriver.exe");
        driver = new ChromeDriver();
        // return new ChromeDriver();
    } else if (browser.equals("firefox")) {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "//resources//geckodriver.exe");
        driver = new FirefoxDriver();
    }
    driver.manage().window().maximize();
    return driver;
}

}
