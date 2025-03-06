package br.com.jadersoncardoso.services;

import br.com.jadersoncardoso.exception.ResourceNotFoundException;
import br.com.jadersoncardoso.model.Person;
import br.com.jadersoncardoso.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        logger.info("Find all People!");

        return personRepository.findAll();
    }
    public Person findById(Long id) {
        logger.info("Find one Person!");

        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));
    }
    public Person create(Person person) {
        logger.info("Creating one Person!");

        return personRepository.save(person);
    }
    public Person update(Person person) {
        logger.info("Updating one Person!");
        Person entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return personRepository.save(entity);
    }
    public void delete(Long id) {
        logger.info("Deleting one Person!");

        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));
        personRepository.delete(entity);
    }

}
