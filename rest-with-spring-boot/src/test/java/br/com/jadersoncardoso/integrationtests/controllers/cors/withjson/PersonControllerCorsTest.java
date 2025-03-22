package br.com.jadersoncardoso.integrationtests.controllers.cors.withjson;

import br.com.jadersoncardoso.config.TestConfigs;
import br.com.jadersoncardoso.integrationtests.dto.PersonDTO;
import br.com.jadersoncardoso.integrationtests.testeconteiners.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JADERSON_BRANDAO)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

       PersonDTO creatdPerson = objectMapper.readValue(content, PersonDTO.class);
       person = creatdPerson;

       assertNotNull(creatdPerson.getId());
       assertNotNull(creatdPerson.getFirstName());
       assertNotNull(creatdPerson.getLastName());
       assertNotNull(creatdPerson.getAddress());
       assertNotNull(creatdPerson.getGender());

       assertTrue(creatdPerson.getId() > 0);

       assertEquals("Richard", creatdPerson.getFirstName());
       assertEquals("Stallman", creatdPerson.getLastName());
       assertEquals("New York City - New York", creatdPerson.getAddress());
       assertEquals("Male", creatdPerson.getGender());
       assertTrue(creatdPerson.getEnabled());

    }@Test
    @Order(2)
    void createWithWrongOrigin() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_JADERSON_CARDOSO)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

       assertEquals("Invalid CORS request", content);

    }
    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id",person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO getPerson = objectMapper.readValue(content, PersonDTO.class);
        person = getPerson;

        assertNotNull(getPerson.getId());
        assertNotNull(getPerson.getFirstName());
        assertNotNull(getPerson.getLastName());
        assertNotNull(getPerson.getAddress());
        assertNotNull(getPerson.getGender());

        assertEquals("Richard", getPerson.getFirstName());
        assertEquals("Stallman", getPerson.getLastName());
        assertEquals("New York City - New York", getPerson.getAddress());
        assertEquals("Male", getPerson.getGender());
        assertTrue(getPerson.getEnabled());
    }
    @Test
    void update() {
    }
    @Test
    void delete() {
    }
    @Test
    void findAll() {
    }

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York City - New York");
        person.setGender("Male");
        person.setEnabled(true);
    }
}