package engage;

import engage.Application;
import engage.Category;
import engage.CategoryController;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringDataRelationshipsTest {

    @Autowired
    private TestRestTemplate template;

    private static String CATEGORY_ENDPOINT = "http://localhost:8080/categories/";

    private static String CATEGORY_NAME_ROOT = "Hobbit";
    private static String CATEGORY_NAME_2 = "Bilbo Baggins";
    private static String CATEGORY_NAME_3 = "Frodo Baggins";

    @Test
    public void whenSaveOneToOneRelationship_thenCorrect() {
        Category categoryRoot = new Category(CATEGORY_NAME_ROOT);
        template.postForEntity(CATEGORY_ENDPOINT, categoryRoot, Category.class);

        Category category2 = new Category(CATEGORY_NAME_2);
        template.postForEntity(CATEGORY_ENDPOINT, category2, Category.class);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-type", "text/uri-list");
        HttpEntity<String> httpEntity
                = new HttpEntity<>(CATEGORY_ENDPOINT + "/1", requestHeaders);
        template.exchange(CATEGORY_ENDPOINT + "/1/children",
                HttpMethod.PUT, httpEntity, String.class);

        ResponseEntity<Category> categoryGetResponse
                = template.getForEntity(CATEGORY_ENDPOINT + "/1/children", Category.class);
        assertEquals("children are incorrect",
                categoryGetResponse.getBody().getName(), CATEGORY_NAME_ROOT);
    }

    @Test
    public void whenSaveOneToManyRelationship_thenCorrect() {
        Category categoryRoot = new Category(CATEGORY_NAME_ROOT);
        template.postForEntity(CATEGORY_ENDPOINT, categoryRoot, Category.class);

        Category category2 = new Category(CATEGORY_NAME_2);
        template.postForEntity(CATEGORY_ENDPOINT, category2, Category.class);

        Category category3 = new Category(CATEGORY_NAME_3);
        template.postForEntity(CATEGORY_ENDPOINT, category3, Category.class);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "text/uri-list");
        HttpEntity<String> categoryHttpEntity
                = new HttpEntity<>(CATEGORY_ENDPOINT + "/1", requestHeaders);
        template.exchange(CATEGORY_ENDPOINT + "/1/children",
                HttpMethod.PUT, categoryHttpEntity, String.class);
        template.exchange(CATEGORY_ENDPOINT + "/2/children",
                HttpMethod.PUT, categoryHttpEntity, String.class);

        ResponseEntity<Category> categoryGetResponse =
                template.getForEntity(CATEGORY_ENDPOINT + "/1/children", Category.class);
        assertEquals("children are incorrect",
                categoryGetResponse.getBody().getName(), CATEGORY_NAME_ROOT);
    }
}