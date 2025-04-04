package br.com.jadersoncardoso.services;

import br.com.jadersoncardoso.controllers.PersonController;
import br.com.jadersoncardoso.data.dto.PersonDTO;
import br.com.jadersoncardoso.exception.RequiredObjectIsNullException;
import br.com.jadersoncardoso.exception.ResourceNotFoundException;
import br.com.jadersoncardoso.model.Person;
import br.com.jadersoncardoso.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.jadersoncardoso.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final PagedResourcesAssembler<PersonDTO> assembler;
    public PersonService(PersonRepository personRepository, PagedResourcesAssembler<PersonDTO> assembler) {
        this.personRepository = personRepository;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all People!");

        var people = personRepository.findAll(pageable);

        var peapleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(),String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(peapleWithLinks, findAllLink);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Finding by name Peaple!");

        var people = personRepository.findPeapleByName(firstName, pageable);

        var peapleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(),String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(peapleWithLinks, findAllLink);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var personEntity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var dto = parseObject(personEntity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");
        var entity = parseObject(person, Person.class);

        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }
    public PersonDTO update(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Person entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }
    public void delete(Long id) {
        logger.info("Deleting one Person!");

        Person entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));
        personRepository.delete(entity);
    }
    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disablind one Person!");

        personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found fir this ID"));
        personRepository.disabledPerson(id);

        var entity = personRepository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        return dto;
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1,12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disablePerson").withType("PATH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
