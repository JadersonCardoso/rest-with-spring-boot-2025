package br.com.jadersoncardoso.services;

import br.com.jadersoncardoso.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<Person> findAll() {
        logger.info("Find All People!");
        var persons = new ArrayList<Person>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }
    public Person findById(String id) {
        logger.info("Find one Person!");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFisrtName("Jaderson");
        person.setLastName("Brandão");
        person.setAddress("Belém - Pará - Brasil");
        person.setGender("Male");
        return person;
    }
    public Person create(Person person) {
        logger.info("Creating one Person!");
        return person;
    }

    public Person update(Person person) {
        logger.info("Updating one Person!");
        return person;
    }

    public void delete(String id) {
        logger.info("Deleting one Person!");
    }



    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFisrtName("Firstname " + i);
        person.setLastName("Lastname " + i);
        person.setAddress("Some address in Brasil");
        person.setGender("Male");
        return person;
    }

}
