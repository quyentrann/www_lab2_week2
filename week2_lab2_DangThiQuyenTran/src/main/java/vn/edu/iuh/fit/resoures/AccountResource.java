package vn.edu.iuh.fit.resoures;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import vn.edu.iuh.fit.entities.Login;
import vn.edu.iuh.fit.models.Account;
import vn.edu.iuh.fit.services.AccountService;

import java.util.List;

@Path("/accounts")
public class AccountResource {
    private final AccountService accountService = new AccountService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        return accountService.createAccount(account)
                ? Response.status(Response.Status.CREATED).entity(true).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login login) {
        return Response.ok(accountService.login(login)).build();
    }

    @PUT
    @Path("/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccount(@PathParam("accountId") long accountId, Account account) {
        account.setAccountId(accountId);
        return accountService.updateAccount(account)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("/{accountId}")
    public Response deleteAccount(@PathParam("accountId") long accountId) {
        return accountService.deleteAccount(accountId)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("accountId") long accountId) {
        Account account = accountService.getAccountById(accountId);
        return account != null
                ? Response.status(Response.Status.OK).entity(account).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return Response.status(Response.Status.OK).entity(accounts).build();
    }

    @GET
    @Path("/getID")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountId() {
        long id = accountService.getAccountId();
        return id != 0
                ? Response.ok(id).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/checkExist/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAccountExist(@PathParam("email") String email) {
        return Response.ok(accountService.checkAccountExist(email)).build();
    }

    @GET
    @Path("/account-by-email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountsByEmail(@PathParam("email") String email) {
        List<Account> accounts = accountService.getAccountsByEmail(email);
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/get-quantity-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuantityAccount() {
        long quantity = accountService.getQuantityAccount();
        return Response.ok(quantity).build();
    }
}
