package br.com.jadersoncardoso.services;

import br.com.jadersoncardoso.data.dto.v1.PersonDTO;
import br.com.jadersoncardoso.data.dto.v2.PersonDTOV2;
import br.com.jadersoncardoso.exception.ResourceNotFoundException;
import br.com.jadersoncardoso.mapper.custom.PersonMapper;
import br.com.jadersoncardoso.model.Person;
import br.com.jadersoncardoso.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static br.com.jadersoncardoso.mapper.ObjectMapper.parseListObjects;
import static br.com.jadersoncardoso.mapper.ObjectMapper.parseObject;


@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final PersonMapper converter;
    public PersonService(PersonRepository personRepository, PersonMapper converter) {
        this.personRepository = personRepository;
        this.converter = converter;
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all People!");

        return parseListObjects(personRepository.findAll(), PersonDTO.class);
    }
    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");
        var personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return parseObject(personEntity, PersonDTO.class);
    }
    public PersonDTO create(PersonDTO personDTO) {
        logger.info("Creating one Person!");
        var person = parseObject(personDTO, Person.class);
        return parseObject(personRepository.save(person), PersonDTO.class);
    }
    public PersonDTOV2 createV2(PersonDTOV2 personDTO) {
        logger.info("Creating one Person!");
        var entity = converter.convertDTOToEntity(personDTO);
        return converter.convertEntityToDTO(personRepository.save(entity));
    }
    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one Person!");
        Person entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(personRepository.save(entity), PersonDTO.class) ;
    }
    public void delete(Long id) {
        logger.info("Deleting one Person!");

        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));
        personRepository.delete(entity);
    }

}
