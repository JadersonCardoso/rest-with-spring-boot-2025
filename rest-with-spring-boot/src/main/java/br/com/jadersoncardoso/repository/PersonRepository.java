package br.com.jadersoncardoso.repository;

import br.com.jadersoncardoso.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
