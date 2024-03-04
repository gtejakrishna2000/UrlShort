package com.example.UrlShort.Service;

import com.example.UrlShort.Dtos.AddCustomerDto;
import com.example.UrlShort.Dtos.DeleteByIdDto;
import com.example.UrlShort.Dtos.GetCustomerByIdDto;
import com.example.UrlShort.Models.Customer;
import com.example.UrlShort.Repository.CustomerRepository;
import com.example.UrlShort.TransFormer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository userRepository;

    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public String genarateUniqueId(AddCustomerDto addCustomerDto)
    {
//         List<Customer> customerList=userRepository.findAllByZipcode(addCustomerDto.getZipcode());
//         int noOfCustomersPresentsInZipCodeArea=customerList.size()+1;
//          String zipcode=addCustomerDto.getZipcode();
//          String uuid="SUNBASE"+"-"+zipcode+"-"+noOfCustomersPresentsInZipCodeArea;
          String uuid = "SB"+1;
          return uuid;
    }
    public Customer Update(Customer customer, AddCustomerDto addCustomerDto)
    {
        // genarate unique id

          customer.setFirst_name(addCustomerDto.getFirst_name());
          customer.setLast_name(addCustomerDto.getLast_name());
          customer.setEmail(addCustomerDto.getEmail());
          customer.setPassword(passwordEncoder.encode(addCustomerDto.getPassword())); // encode password
          customer.setRole(addCustomerDto.getRole());

          return customer;
    }
    public String addUserToDb(AddCustomerDto addCustomerDto) throws Exception
    {


          Customer customer=userRepository.findByEmail(addCustomerDto.getEmail());
          if(customer!=null)
          {
              // if customer already exist in db
              Customer updatedCustomer =Update(customer, addCustomerDto);
              userRepository.save(updatedCustomer);
          }
          else {
              // password encode
              String uniqueId=genarateUniqueId(addCustomerDto);
              String password = passwordEncoder.encode(addCustomerDto.getPassword());
              Customer newCustomer= CustomerTransformer.BuildCustomer(addCustomerDto, uniqueId,password);
              userRepository.save(newCustomer);

          }
          return "successfull";
    }

    public List<Customer> getAllCustomerPresentInDb() throws Exception
    {
        // Retrieve all customers from the database using the UserRepository
        List<Customer> customerList=userRepository.findAll();
         return customerList;
    }




    public List<Customer> getCustomerByOrderByName() throws Exception
    {
        // find all customers by
        List<Customer> customerList=userRepository.findAllByOrderByUsername();
        return customerList;
    }

    //get Customer Id;
    public Customer  getCustomerById(GetCustomerByIdDto getCustomerByIdDto) throws Exception
    {
        // email is unique get custoemer by email
        if(userRepository.findByEmail(getCustomerByIdDto.getEmailId())==null)
        {
            throw new Exception("customer not found Exceptions");
        }

        Customer customer=userRepository.findByEmail(getCustomerByIdDto.getEmailId());
        return customer;
    }

    // delete customer by emailId
    public String  deleteCustomerById(DeleteByIdDto deleteByIdDto) throws Exception
    {
        // check customer exist in db or not
            if(userRepository.findByEmail(deleteByIdDto.getEmail())==null)
            {
                throw new Exception("customer not found Exceptions");
            }
        // delete customer by email
        userRepository.deleteByEmail(deleteByIdDto.getEmail());
          return "successful";
    }

}
