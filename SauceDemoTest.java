import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceDemoTest {

    private LoginPage loginPage;
    private ProductPage productPage;
    private CartPage cartPage;

    @BeforeAll
    public static void setup() {
        // Настройка конфигурации Selenide (например, браузер)
        Configuration.browserSize = "1920x1080";  // Устанавливаем размеры окна браузера

        Configuration.browser = "chrome"; // Можно выбрать другие браузеры, например, "firefox"
    }


    @Test
    public void testSuccessfulLogin() {
        loginPage = new LoginPage();
        loginPage.open("https://www.saucedemo.com"); // Это правильный вызов
        loginPage.login("standard_user", "secret_sauce");



    // Проверяем, что после входа мы попали на страницу с товарами
        assertTrue(WebDriverRunner.url().contains("inventory.html"));
    }

    @Test
    public void testInvalidLogin() {
        loginPage = new LoginPage();
        loginPage.open("https://www.saucedemo.com/");
        loginPage.login("standard_user", "wrong_password");

        // Проверяем, что отображается ошибка
        assertEquals("Epic sadface: Username and password do not match any user in this service", loginPage.getErrorMessage());
    }

    @Test
    public void testAddToCart() {
        loginPage = new LoginPage();
        productPage = new ProductPage();

        loginPage.open("https://www.saucedemo.com/inventory.html");
        loginPage.login("standard_user", "secret_sauce");

        // Добавляем товар в корзину
        productPage.addItemToCart();

        // Проверяем, что товар добавлен в корзину
        productPage.openCart();
        assertTrue(WebDriverRunner.url().contains("cart"));
    }

    @Test
    public void testRemoveFromCart() {
        loginPage = new LoginPage();
        productPage = new ProductPage();
        cartPage = new CartPage();

        loginPage.open("https://www.saucedemo.com/inventory-item.html?id=4");
        loginPage.login("standard_user", "secret_sauce");

        // Добавляем товар в корзину
        productPage.addItemToCart();

        // Открываем корзину и удаляем товар
        productPage.openCart();
        cartPage.removeItemFromCart();

        // Проверяем, что корзина пуста
        assertTrue(WebDriverRunner.url().contains("cart"));
    }

    @Test
    public void testCheckout() {
        loginPage = new LoginPage();
        productPage = new ProductPage();
        cartPage = new CartPage();

        loginPage.open("https://www.saucedemo.com/cart.html");
        loginPage.login("standard_user", "secret_sauce");

        // Добавляем товар в корзину
        productPage.addItemToCart();

        // Открываем корзину и нажимаем кнопку Checkout
        productPage.openCart();
        cartPage.checkout();

        // Проверяем, что мы на странице Checkout
        assertTrue(WebDriverRunner.url().contains("checkout-step-one"));
    }
}

