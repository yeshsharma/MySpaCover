package zasyasolutions.SpaCover.TestCases;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class Cart {

    WebDriver driver;
    WebDriverWait wait;
    Wait<WebDriver> fluentWait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        driver.get("https://rahulshettyacademy.com/client/");
    }

    // ✅ Wait for spinner gone + JavaScript click = guaranteed to work
    private void waitAndClick(By locator) {
        fluentWait.until(d -> {
            List<WebElement> spinners = d.findElements(
                By.cssSelector(".ngx-spinner-overlay"));
            return spinners.isEmpty() || !spinners.get(0).isDisplayed();
        });
        WebElement element = wait.until(
            ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void waitAndClick(WebElement element) {
        fluentWait.until(d -> {
            List<WebElement> spinners = d.findElements(
                By.cssSelector(".ngx-spinner-overlay"));
            return spinners.isEmpty() || !spinners.get(0).isDisplayed();
        });
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    @Test
    public void addToCartTest() {

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userEmail")))
                .sendKeys("yeshsharma516032@gmail.com");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userPassword")))
                .sendKeys("Hsey255198@");

        waitAndClick(By.id("login"));

        // Wait for products
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card-body")));

        List<WebElement> products = driver.findElements(By.cssSelector(".card-body"));
        String productName = "ZARA COAT 3";

        for (WebElement product : products) {
            String name = product.findElement(By.tagName("b")).getText();
            if (name.equals(productName)) {
                waitAndClick(product.findElement(
                        By.xpath(".//button[contains(text(),'Add To Cart')]")));
                break;
            }
        }

        // Go to Cart
        waitAndClick(By.xpath("//button[@routerlink='/dashboard/cart']"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div li")));

        List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cart ul li"));
        boolean productFound = false;

        for (WebElement item : cartProducts) {
            String cartItem = item.findElement(By.tagName("h3")).getText();
            if (cartItem.equals(productName)) {
                productFound = true;
                waitAndClick(item.findElement(
                        By.xpath(".//button[text()='Buy Now']")));
                break;
            }
        }

        Assert.assertTrue(productFound, "Product not found in cart");

        // Country Selection
        WebElement countryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[placeholder='Select Country']")));
        countryInput.sendKeys("India");

        List<WebElement> dropdownOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector(".ta-item.list-group-item")));

        for (WebElement option : dropdownOptions) {
            if (option.getText().trim().equals("India")) {
                waitAndClick(option);
                break;
            }
        }

        // Fill details
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".field.small input.txt:nth-child(1)")))
                .sendKeys("223");
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".field input.txt:nth-child(3)")))
                .sendKeys("yesh");

        // Submit Order
//        waitAndClick(By.cssSelector(".actions .action__submit"));
//
//        // Get Order ID
//        String orderIdText = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.cssSelector(".em-spacer-1 .ng-star-inserted")))
//                .getText();
//        String orderId = orderIdText.split("\\|")[1].trim();
//        System.out.println("Order ID: " + orderId);
//
//        // Go to Orders Page
//        waitAndClick(By.xpath("//button[text()='Orders']"));
//
//        List<WebElement> orders = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                By.cssSelector("tbody tr")));
//        boolean orderMatched = false;
//
//        for (WebElement order : orders) {
//            String id = order.findElement(By.tagName("th")).getText();
//            if (id.equals(orderId)) {
//                orderMatched = true;
//                waitAndClick(order.findElement(By.cssSelector(".btn-primary")));
//                break;
//            }
//        }
//
//        Assert.assertTrue(orderMatched, "Order ID not found in Orders page");
//
//        // Validate Order Details
//        String orderDetails = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.cssSelector(".col-text.-main")))
//                .getText();
//        Assert.assertTrue(orderDetails.contains(orderId), "Order verification failed");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}