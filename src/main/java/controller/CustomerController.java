package controller;

import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CustomerService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> save(@RequestBody Customer customer) throws SQLException {
        return new ResponseEntity<>(customerService.save(customer), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Customer> findAll(@RequestParam(value = "model", required = false) String model) throws SQLException {
        if (model != null) {
            return customerService.findAll();
        }
        return customerService.findAll();
    }

    // http://localhost:8080/api/customers/1
    @GetMapping("{id}")
    public ResponseEntity<Customer> findCustomerById(@PathVariable("id") long customerId) throws SQLException {
        return new ResponseEntity<>(customerService.findById(customerId), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) throws SQLException {
        return new ResponseEntity<>(customerService.update(customer), HttpStatus.OK);
    }

    // http://localhost:8080/api/customer/1
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) throws SQLException {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
