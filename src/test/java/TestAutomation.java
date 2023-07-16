import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestAutomation {

    private ChromeDriver driver;

    @BeforeMethod
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.get("https://demo.actitime.com/login.do");
    }

    @Test
    public void successLogin() {
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("pwd")).sendKeys("manager");
        driver.findElement(By.id("loginButton")).click();
    }

    public void loginAsAdmin(ChromeDriver driver){
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.name("pwd")).sendKeys("manager");
        driver.findElement(By.id("loginButton")).click();
    }

    @Test
    public void failedLogin() {
        driver.findElement(By.id("username")).sendKeys("ashan");
        driver.findElement(By.name("pwd")).sendKeys("ashan");
        driver.findElement(By.id("loginButton")).click();
    }

    @Test
    public void filtertasks(){
        this.loginAsAdmin(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/user/submit_tt.do"));
        driver.findElement(By.linkText("Tasks")).click();
        driver.findElement(By.cssSelector(".statusFilterButtonContainer .label")).click();
        driver.findElement(By.cssSelector(".customSection .title")).click();
        driver.findElement(By.cssSelector(".workflowStatusNode:nth-child(4) .checkmark")).click();
        driver.findElement(By.cssSelector(".apply")).click();
    }

    @Test
    public void tasksDashboard() {
        this.loginAsAdmin(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/user/submit_tt.do"));

        driver.findElement(By.id("container_tasks")).click();
    }

    @Test
    public void failedTestForScreenshot() {
        this.loginAsAdmin(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/user/submit_tt.do"));

        driver.findElement(By.id("fail-test")).click();
    }

    @AfterMethod
    public void captureScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destinationFile = new File("src/test/resources/screenshots/" + result.getName() + ".png");

            try {
                Files.copy(screenshotFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Screenshot captured: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
