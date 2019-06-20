package engage;

import engage.Application;
import engage.Category;
import engage.CategoryController;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = WebEnvironment.DEFINED_PORT)
public class CategoriesTest {

    @Autowired
    private TestRestTemplate template;

    private static String CATEGORY_ENDPOINT = "http://localhost:8080/categories/";

    private static String CATEGORY_NAME_ROOT = "Hobbit";
    private static String CATEGORY_NAME_2 = "Bilbo Baggins";
    private static String CATEGORY_NAME_3 = "Frodo Baggins";

    @Test
    public void whenSaveOneToOneRelationship_thenCorrect() {
        Category categoryRoot = new Category();
        categoryRoot.setName(CATEGORY_NAME_ROOT);
        template.postForEntity(CATEGORY_ENDPOINT, categoryRoot, Category.class);

        Category category2 = new Category();
        category2.setName(CATEGORY_NAME_2);
        template.postForEntity(CATEGORY_ENDPOINT, category2, Category.class);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-type", "text/uri-list");
        HttpEntity<String> httpEntity
                = new HttpEntity<>(CATEGORY_ENDPOINT + "/2", requestHeaders);
        template.exchange(CATEGORY_ENDPOINT + "/1/children",
                HttpMethod.PUT, httpEntity, String.class);

        ResponseEntity<Category> categoryGetResponse
                = template.getForEntity(CATEGORY_ENDPOINT + "/2/parent", Category.class);
        assertEquals("parent is incorrect",
                categoryGetResponse.getBody().getName(), CATEGORY_NAME_ROOT);
    }

    @Test
    public void whenSaveOneToManyRelationship_thenCorrect() {
        Category categoryRoot = new Category();
        categoryRoot.setName(CATEGORY_NAME_ROOT);
        template.postForEntity(CATEGORY_ENDPOINT, categoryRoot, Category.class);

        Category category2 = new Category();
        category2.setName(CATEGORY_NAME_2);
        template.postForEntity(CATEGORY_ENDPOINT, category2, Category.class);

        Category category3 = new Category();
        category3.setName(CATEGORY_NAME_3);
        template.postForEntity(CATEGORY_ENDPOINT, category3, Category.class);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "text/uri-list");
        HttpEntity<String> categoryHttpEntity
                = new HttpEntity<>(CATEGORY_ENDPOINT + "/2\n" + CATEGORY_ENDPOINT + "/3\n", requestHeaders);
        template.exchange(CATEGORY_ENDPOINT + "/1/children",
                HttpMethod.PUT, categoryHttpEntity, String.class);

        ResponseEntity<Category> categoryGetResponse2 =
                template.getForEntity(CATEGORY_ENDPOINT + "/2/parent", Category.class);
        assertEquals("parent for 2 is incorrect",
                categoryGetResponse2.getBody().getName(), CATEGORY_NAME_ROOT);
        ResponseEntity<Category> categoryGetResponse3 =
                template.getForEntity(CATEGORY_ENDPOINT + "/3/parent", Category.class);
        assertEquals("parent for 3 is incorrect",
                categoryGetResponse3.getBody().getName(), CATEGORY_NAME_ROOT);
    }

    @Test
    public void whenSaveOneToManyNRelationship_thenCorrect() {
        Category categoryRoot = new Category();
        categoryRoot.setName(CATEGORY_NAME_ROOT);
        template.postForEntity(CATEGORY_ENDPOINT, categoryRoot, Category.class);

        Category category2 = new Category();
        category2.setName(CATEGORY_NAME_2);
        template.postForEntity(CATEGORY_ENDPOINT, category2, Category.class);

        Category category3 = new Category();
        category3.setName(CATEGORY_NAME_3);
        template.postForEntity(CATEGORY_ENDPOINT, category3, Category.class);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-type", "text/uri-list");
        HttpEntity<String> httpEntity
                = new HttpEntity<>(CATEGORY_ENDPOINT + "/2", requestHeaders);
        template.exchange(CATEGORY_ENDPOINT + "/1/children",
                HttpMethod.PUT, httpEntity, String.class);

        ResponseEntity<Category> categoryGetResponse
                = template.getForEntity(CATEGORY_ENDPOINT + "/2/parent", Category.class);
        assertEquals("parent for 2 is incorrect",
                categoryGetResponse.getBody().getName(), CATEGORY_NAME_ROOT);


        HttpHeaders requestHeaders2 = new HttpHeaders();
        requestHeaders2.add("Content-type", "text/uri-list");
        HttpEntity<String> httpEntity2
                = new HttpEntity<>(CATEGORY_ENDPOINT + "/3", requestHeaders2);
        template.exchange(CATEGORY_ENDPOINT + "/2/children",
                HttpMethod.PUT, httpEntity2, String.class);

        ResponseEntity<Category> categoryGetResponse2 =
                template.getForEntity(CATEGORY_ENDPOINT + "/2/parent", Category.class);
        assertEquals("parent for 2 is incorrect",
                categoryGetResponse2.getBody().getName(), CATEGORY_NAME_ROOT);

        ResponseEntity<Category> categoryGetResponse3 =
                template.getForEntity(CATEGORY_ENDPOINT + "/3/parent", Category.class);
        assertEquals("parent for 3 is incorrect",
                categoryGetResponse3.getBody().getName(), CATEGORY_NAME_2);
    }
}