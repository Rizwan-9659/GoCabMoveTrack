package com.CabBookingMaven.Service;

import com.CabBookingMaven.dao.CustomerDAO;
import com.CabBookingMaven.model.Customer;

public class CustomerService {
    private CustomerDAO dao = new CustomerDAO();

    public boolean signup(Customer customer) { return dao.signup(customer); }

    public Customer login(String email, String password) { return dao.login(email, password); }

    public boolean isEmailExist(String email) { return dao.getCustomerByEmail(email) != null; }

    public boolean updatePin(int customerId, String newPin) { return dao.updatePin(customerId, newPin); }

    public boolean addBankDetails(int customerId, String bankName, String accountNumber, String ifsc, String upiId) {
        return dao.addBankDetails(customerId, bankName, accountNumber, ifsc, upiId);
    }
}
