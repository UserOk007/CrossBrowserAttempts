package tests;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 Lecture 4 - Homework
 */
public class CreateNewProduct extends FrequentOperations{

    public final String nameOfProduct = randomName();
    public final String quantityOfProduct = randomQuantity();
    public String priceOfProduct = randomPrice();

WebDriverWait wait;

    @DataProvider (name = "Authentication")
    public Object[][] takeLogin(){
        return new String[][] {
                {"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"}
        };
    }

    @Test(dataProvider = "Authentication")
    public void userLogin(String email, String passwd){
       // driver.manage().window().maximize();
        driver.get("http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("email")));
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("passwd")).sendKeys(passwd);
        driver.findElement(By.name("submitLogin")).click();
        String dashboardTitle = driver.getTitle();
        System.out.println(dashboardTitle);
        Assert.assertEquals(dashboardTitle, "prestashop-automation > Панель администратора (PrestaShop™)");
    }

    @Test (dependsOnMethods = "userLogin")
    public void createProduct() {
        openProductsTab();
        addNewProduct();
    }

    @Test (dependsOnMethods = "createProduct")
    public void checkTheProduct() {

        driver.get("http://prestashop-automation.qatestlab.com.ua");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("all-product-link")));
        driver.findElement(By.className("all-product-link")).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("product-title")));
        Assert.assertTrue(true, nameOfProduct);
        System.out.println("New product is " + randomName());

        //driver.get("http://prestashop-automation.qatestlab.com.ua/ru/2-home?page=3");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("products")));


        driver.findElement(By.linkText(nameOfProduct)).click();

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("h1")));
        String nameContainer = driver.findElement(By.tagName("h1")).getText();
        Assert.assertEquals(nameContainer, nameOfProduct);

        String quantityContainer = driver.findElement(By.xpath("//div[@class='product-quantities']//span")).getText();
        String actualQuantity = quantityOfProduct + " Товары";
        Assert.assertEquals(quantityContainer,actualQuantity);

        String priceContainer = driver.findElement(By.xpath("//div[@class='current-price']")).getText();
        priceOfProduct = priceOfProduct.replace(".", ",");
        String actualPrice = priceOfProduct +  " ₴";
        Assert.assertEquals(priceContainer, actualPrice);
    }


    @AfterTest

    private void tearDown(){
        driver.quit();
    }

    private void openProductsTab() {

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("subtab-AdminCatalog")));
        WebElement catalogTab = driver.findElement(By.id("subtab-AdminCatalog"));
        WebElement productTab = driver.findElement(By.xpath("//*[@id='subtab-AdminProducts']/a"));

        Actions actions = new Actions(driver);
        actions.moveToElement(catalogTab).pause(500).moveToElement(productTab).click(productTab).build().perform();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("page-header-desc-configuration-add")));
    }

    private  void openNewProductForm() {
        driver.findElement(By.id("page-header-desc-configuration-add")).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("form_step1_name")));
    }


    String randomQuantity() {
        Random rand = new Random();
        int randomNum = rand.nextInt((100 - 1) + 1) + 1;
        return Integer.toString(randomNum);
    }


    String randomPrice() {

        double x = (Math.random()*((100.0-0.1)+1))+0.1;
        double newDouble = new BigDecimal(x).setScale(2, RoundingMode.UP).doubleValue();
        return Double.toString(newDouble);
    }

    String randomName() {
        String characters = "ABCDEFGHIJKLMNOPRSTUVWXYZ";
        String randomString = "";
        int length = 5;

        Random rand = new Random();

        char[] text = new char[length];

        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rand.nextInt(characters.length()));
        }

        for (int i = 0; i < text.length; i++) {
            randomString += text[i];
        }

        return randomString;
    }

    public void addNewProduct() {
        openNewProductForm();

        WebElement inputProductName = driver.findElement(By.cssSelector("input[id='form_step1_name_1']"));
        inputProductName.sendKeys(nameOfProduct);

        WebElement quantityTab = driver.findElement(By.id("tab_step3"));
        WebElement inputProductQuantity = driver.findElement(By.cssSelector("input[id='form_step3_qty_0']"));

        quantityTab.click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("form_step3_qty_0")));
        inputProductQuantity.clear();
        inputProductQuantity.sendKeys(quantityOfProduct);

        WebElement priceTab = driver.findElement(By.id("tab_step2"));
        WebElement inputProductPrice = driver.findElement(By.cssSelector("input[id='form_step2_price']"));
        priceTab.click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("form_step2_price")));
        inputProductPrice.clear();
        inputProductPrice.sendKeys(priceOfProduct);
        System.out.println(priceOfProduct);

        WebElement inactiveSwitcher = driver.findElement(By.className("switch-input"));
        inactiveSwitcher.click();
        checkNotification();

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("submit")));
        WebElement saveButton = driver.findElement(By.id("submit"));
        saveButton.click();
        checkNotification();


    }

    private void checkNotification() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("growls")));
        WebElement closeNotificationButton = driver.findElement(By.className("growl-close"));
        closeNotificationButton.click();
    }





}
