package ru.otus;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> dataByCustomer = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return getEntry(dataByCustomer.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getEntry(dataByCustomer.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        this.dataByCustomer.put(customer, data);
    }

    private Map.Entry<Customer, String> getEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }

        return Map.entry(copyCustomer(entry.getKey()), entry.getValue());
    }

    private Customer copyCustomer(Customer customer) {
        return new Customer(customer.getId(), customer.getName(), customer.getScores());
    }
}
