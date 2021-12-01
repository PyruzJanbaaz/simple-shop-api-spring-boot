package org.pyruz.api.shop.repository;

import org.pyruz.api.shop.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Optional<Contact> findContactByMobileNumber(String mobileNumber);

}
