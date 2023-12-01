package vn.edu.iuh.fit.resoures;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.entities.InformationProduct;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.services.ProductService;

import java.util.List;

@Path("/products")
public class ProductResource {
    private final ProductService productService = new ProductService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product) {
        return Response.status(productService.createProduct(product) ? Response.Status.CREATED : Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Path("/{productId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("productId") long productId, Product product) {
        product.setProductId(productId);
        return Response.status(productService.updateProduct(product) ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("/{productId}")
    public Response deleteProduct(@PathParam("productId") long productId) {
        return Response.status(productService.deleteProduct(productId) ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("productId") long productId) {
        Product product = productService.getProductById(productId);
        return product != null ? Response.ok(product).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return Response.status(Response.Status.OK).entity(products).build();
    }

    @GET
    @Path("/getInfoProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoProduct() {
        List<InformationProduct> infoProduct = productService.getInfoProduct();
        return Response.status(Response.Status.OK).entity(infoProduct).build();
    }

    @GET
    @Path("/getAllNameOfProductOrdered")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNameOfProductOrdered() {
        List<Object[]> objects = productService.getAllNameOfProductOrdered();
        return Response.status(Response.Status.OK).entity(objects).build();
    }
}
