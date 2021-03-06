package TVpage;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class TVPageObject {
    private final WebDriver driver;
    private Map<String, String> listBrands = new HashMap<>();
    private final By showAllBrandsLocator = By.xpath("//*[@id='schema-filter']/div[5]/div[4]/div[2]/div[1]");
    private final By allBrandsLocator = By.xpath("//div[contains(text(), 'Производитель')][@class='schema-filter-popover__title']//following-sibling::div");
    private final By brandsElementsLocator = By.cssSelector("div.schema-filter-popover__column-item");
    private final By schemaProductsLocator = By.cssSelector("div.schema-products");
    private final By productsTitleLocator = By.xpath("//span[@data-bind='html: product.extended_name || product.full_name']");

    public TVPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public TVPageObject openCatalog() {
        driver.findElement(By.linkText("Каталог")).click();
        return this;
    }

    public TVPageObject openTVPage() {
        driver.findElement(By.xpath("//span[contains(text(), 'Электроника')]")).click();
        driver.findElement(By.xpath("//div[contains(text(), 'Телевидение')]")).click();
        driver.findElement(By.partialLinkText("Телевизоры")).click();
        return this;
    }

    public TVPageObject selectBrandName() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,1000)", "");
        driver.findElement(showAllBrandsLocator).click();
        WebElement brands = driver.findElement(allBrandsLocator);
        List<WebElement> brandsElements = brands.findElements(brandsElementsLocator);
        for (int i = 0; i < brandsElements.size(); i++) {
            listBrands.put(brandsElements.get(i).findElement(By.tagName("input")).getAttribute("value"), brandsElements.get(i).findElement(By.cssSelector("span.schema-filter__checkbox-text")).getText());
        }
        int random = new Random().nextInt(listBrands.size());
        List<String> values = new ArrayList<>(listBrands.values());
        String brand = values.get(random);
        brands.findElement(By.xpath("//span[contains(text(), '" + brand + "')]")).click();
        return this;
    }

    public boolean isNotSamsung() {
        WebElement schemaProducts = driver.findElement(schemaProductsLocator);
        List<WebElement> productsTitle = schemaProducts.findElements(productsTitleLocator);
        for (WebElement productTitle : productsTitle) {
            if (StringUtils.contains("Samsung", productTitle.getText())) {
                return false;
            }
        }
        return true;
    }
}
