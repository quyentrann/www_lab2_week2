package vn.edu.iuh.fit.resoures;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.models.Order;
import vn.edu.iuh.fit.services.OrderService;

import java.util.List;

@Path("/orders")
public class OrderResource {
    private final OrderService orderService = new OrderService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(Order order) {
        return Response.status(orderService.createOrder(order) ? Response.Status.CREATED : Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{orderId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("orderId") long orderId, Order order) {
        order.setOrderId(orderId);
        return Response.status(orderService.updateOrder(order) ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("/{orderId}")
    public Response deleteOrder(@PathParam("orderId") long orderId) {
        return Response.status(orderService.deleteOrder(orderId) ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("orderId") long orderId) {
        Order order = orderService.getOrderById(orderId);
        return order != null ? Response.ok(order).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return Response.status(Response.Status.OK).entity(orders).build();
    }

    @GET
    @Path("/getID")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderId() {
        long id = orderService.getOrderId();
        return id != 0 ? Response.ok(id).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/inforOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerByCust(Order or) {
        Order order = orderService.getOrderByOrder(or);
        return Response.ok(order).build();
    }
}
