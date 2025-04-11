package br.com.jadersoncardoso.repository;

import br.com.jadersoncardoso.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disabledPerson(@Param("id") Long id);
    @Query("FROM Person p WHERE p.firstName like LOWER(CONCAT('%',:firstName,'%'))")
    Page<Person> findPeapleByName(@Param("firstName") String firstName, Pageable pageable);

}
