package vn.edu.iuh.fit.services;

import vn.edu.iuh.fit.models.Customer;
import vn.edu.iuh.fit.models.Order;
import vn.edu.iuh.fit.repositories.OrderRepository;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService() {
        orderRepository = new OrderRepository();
    }

    public boolean createOrder(Order order) {
        return orderRepository.createOrder(order);
    }

    public boolean updateOrder(Order order) {
        return orderRepository.updateOrder(order);
    }

    public boolean deleteOrder(long orderId) {
        return orderRepository.deleteOrder(orderId);
    }

    public Order getOrderById(long orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderByOrder(Order or){
        return orderRepository.getOrderByOrder(or);
    }

    public long getOrderId(){
        return orderRepository.getOrderId();
    }
}