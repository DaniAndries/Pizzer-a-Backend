package service.impl;

import model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import repository.CustomerRepository;
import service.CustomerService;

import java.sql.SQLException;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        if (customerRepository.existsByDni(customer.getDni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already exist a customer with DNI:" + customer.getDni());
        }
        return customerRepository.save(customer);
    }

    @Override
    public void delete(long id) throws SQLException {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer update(Customer customer) throws SQLException {
        if (customerRepository.existsById(customer.getId())) {
            return customerRepository.save(customer);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found Id: " + customer.getId());
        }
    }

    @Override
    public Customer findById(long id) throws SQLException {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found Id: " + id));
    }

    @Override
    public Customer findByMail(String mail) throws SQLException {
        return customerRepository.findByMail(mail).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found Dni: " + mail));
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        return customerRepository.findAll();
    }


}
