package com.bestbuy.bestbuyinfo;

import com.bestbuy.constants.path;
import com.bestbuy.info.ProductSteps;
import com.bestbuy.testbase.TestBase;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import static org.hamcrest.Matchers.hasValue;
import java.util.Random;

@RunWith(SerenityRunner.class)
public class ProductCRUDTestWithSteps extends TestBase {

    public static String getRandomValue() {
        Random random = new Random();
        int randomInt = random.nextInt(100000);
        return Integer.toString(randomInt);
    }

    static String name = "Duracell - AAA Batteries (4-Pack)" + getRandomValue();
    static String type = "HardGood" + getRandomValue();
    static Double price = 5.49;
    static int shipping = 0;
    static String upc = "041333424019";
    static String description = "Compatible with select electronic devices; AAA size; DURALOCK Power Preserve technology; 4-pack";
    static String manufacturer = "Duracell";
    static String model = "MN2400B4Z";
    static String url = "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC";
    static String image = "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg";

    static int productID;


    @BeforeClass
    public static void endPath() {
        RestAssured.basePath = path.PRODUCTS;
    }




    @Steps
    ProductSteps productSteps;

    @Title("This will create new product")

    @Test
    public void test001() {
        ValidatableResponse response = productSteps.createProduct(name, type, price, shipping, upc, description, manufacturer, model, url, image);
        response.log().all().statusCode(201);
        productID = response.log().all().extract().path("id");

    }

    @Title("Verify if the product was added to the list")
    @Test

    public void test002() {
        HashMap<String, Object> productMap = productSteps.getProductIndoByProductName(productID);
        Assert.assertThat(productMap, hasValue(name));
    }

    @Title("Update the product information")
    @Test
    public void test003() {
        name = name + "_updated";
        productSteps.updateProduct(productID, name, type, price, shipping, upc, description, manufacturer, model, url, image);
        HashMap<String, Object> productMap = productSteps.getProductIndoByProductName(productID);
        Assert.assertThat(productMap, hasValue(name));
    }

    @Title("Delete the product and verify if the product is deleted!")
    @Test
    public void test004() {
        productSteps.deleteProduct(productID).statusCode(200);
        productSteps.getProduct(productID).statusCode(404);
    }


}



