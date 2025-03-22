package br.com.jadersoncardoso.integrationtests.controllers.withjson;

import br.com.jadersoncardoso.config.TestConfigs;
import br.com.jadersoncardoso.data.dto.PersonDTO;
import br.com.jadersoncardoso.integrationtests.testeconteiners.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTest() throws IOException {
        mockPerson();
        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
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

        assertTrue(creatdPerson.getId() > 0);

        assertEquals("Linus", creatdPerson.getFirstName());
        assertEquals("Torvalds", creatdPerson.getLastName());
        assertEquals("Helsinki - Finland", creatdPerson.getAddress());
        assertEquals("Male", creatdPerson.getGender());
        assertTrue(creatdPerson.getEnabled());

    } @Test
    @Order(2)
    void updateTest() throws IOException {
        person.setLastName("Benedict Torvalds");

        specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(person)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        PersonDTO creatdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = creatdPerson;

        assertNotNull(creatdPerson.getId());

        assertTrue(creatdPerson.getId() > 0);

        assertEquals("Linus", creatdPerson.getFirstName());
        assertEquals("Benedict Torvalds", creatdPerson.getLastName());
        assertEquals("Helsinki - Finland", creatdPerson.getAddress());
        assertEquals("Male", creatdPerson.getGender());
        assertTrue(creatdPerson.getEnabled());

    }

    @Test
    @Order(3)
    void findByIdTest() throws IOException {
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

        assertEquals("Linus", getPerson.getFirstName());
        assertEquals("Benedict Torvalds", getPerson.getLastName());
        assertEquals("Helsinki - Finland", getPerson.getAddress());
        assertEquals("Male", getPerson.getGender());
        assertTrue(getPerson.getEnabled());
    }


    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
