package vn.edu.iuh.fit.services;

import vn.edu.iuh.fit.models.OrderDetail;
import vn.edu.iuh.fit.repositories.OrderDetailRepository;
import java.util.List;

public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService() {
        orderDetailRepository = new OrderDetailRepository();
    }

    public boolean createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.createOrderDetail(orderDetail);
    }

    public boolean updateOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.updateOrderDetail(orderDetail);
    }

    public boolean deleteOrderDetail(long orderDetailId) {
        return orderDetailRepository.deleteOrderDetail(orderDetailId);
    }

    public OrderDetail getOrderDetailById(long orderDetailId) {
        return orderDetailRepository.getOrderDetailById(orderDetailId);
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.getAllOrderDetails();
    }

    public long getOrderDetailId(){
        return orderDetailRepository.getOrderDetailId();
    }

    public List<Object[]> statisticOrderDetailOfMonthAndYear() {
        return orderDetailRepository.statisticOrderDetailOfMonthAndYear();
    }

    public List<Object[]> statisticOrderDetailOfMonthAndYearByMonth(int month) {
        return orderDetailRepository.statisticOrderDetailOfMonthAndYearByMonth(month);
    }

    public double getTotalPricesOfYear() {
        return orderDetailRepository.getTotalPricesOfYear();
    }
}