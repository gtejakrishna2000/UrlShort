package com.example.UrlShort.Repository;

import com.example.UrlShort.Models.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {



    UserDetails findByUsername(String username);

    Customer findByEmail(String emailId);


    @Transactional
    @Query(value = "select * from Customer order by username",nativeQuery = true)
    List<Customer> findAllByOrderByUsername();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Customer c WHERE c.email = :email", nativeQuery = true)
    void deleteByEmail(String email);

}
