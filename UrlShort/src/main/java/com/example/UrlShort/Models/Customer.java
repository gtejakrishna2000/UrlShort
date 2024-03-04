package com.example.UrlShort.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Builder
public class Customer implements UserDetails {

    @Id
    private String customerId;

      @Column(unique = true)
    private String username;

      @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String first_name;

    @Column(nullable = false)
    private String last_name;


    @Column(nullable = false)
    private String email;

    // i'm assuming that we are handling custoemrs not admin or manager
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Url> url;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // get authorities
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Customer(String username, String email, String password, String role) {
        this.username=username;
        this.email=email;
        this.password=password;
        this.role=role;
    }

}
