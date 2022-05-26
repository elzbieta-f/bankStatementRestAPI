package lt.bit.bankstatementrestful.ws;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lt.bit.bankstatementrestful.data.Balance;
import lt.bit.bankstatementrestful.data.BankStatement;
import lt.bit.bankstatementrestful.data.DB;

@Path("bs")
public class BankStatements {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Context
    HttpServletRequest request;

    /**
     * calls a method readData from DB class to fetch results from CSV file
     *
     * @return a list of BankStatement in JSON
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BankStatement> getAll() throws IOException {
        List<BankStatement> list = DB.readData(request.getServletContext());
        return list;
    }

    /**
     * Filters BankStatements from start date to end date
     *
     * Calls a method saveData from DB class to save filtered list to CSV file
     *
     * @param fromStr fetched from URL path, if date cannot be parsed from the
     * parameter a date 1970-01-01 is set as date from
     * @param toStr fetched from URL path if date cannot be parsed from the
     * parameter a current is set as date to
     * @return Response with Status OK
     * @throws IOException
     */
    @GET
    @Path("{from}/{to}")
    public Response exportToCSV(
            @PathParam("from") String fromStr,
            @PathParam("to") String toStr) throws IOException {
        Date from = null;
        Date to = null;
        try {
            from = sdf.parse(fromStr);
        } catch (ParseException ex) {
            from = new Date(0);
        }
        try {
            to = sdf.parse(toStr);
        } catch (ParseException ex) {
            to = new Date();
        }
        List<BankStatement> filteredByDate = DB.filterByDate(DB.readData(request.getServletContext()), from, to);
        if (filteredByDate != null) {
            DB.saveData(request.getServletContext(), filteredByDate);
        }
        return Response
                .status(Response.Status.OK).
                build();
    }
    
    @GET
    @Path("all")
    public Response exportAllToCSV() throws IOException {        
        List<BankStatement> list = DB.readData(request.getServletContext());
        if (list != null) {
            DB.saveData(request.getServletContext(), list);
        }
        return Response
                .status(Response.Status.OK).
                build();
    }
    
    @GET
    @Path("{from}")
    public Response exportToCSVFrom(
            @PathParam("from") String fromStr) throws IOException {
        Date from = null;
        Date to = new Date();
        try {
            from = sdf.parse(fromStr);
        } catch (ParseException ex) {
            from = new Date(0);
        }
        
        List<BankStatement> filteredByDate = DB.filterByDate(DB.readData(request.getServletContext()), from, to);
        if (filteredByDate != null) {
            DB.saveData(request.getServletContext(), filteredByDate);
        }
        return Response
                .status(Response.Status.OK).
                build();
    }
    
    @GET
    @Path("from/{to}")
    public Response exportToCSVTo(            
            @PathParam("to") String toStr) throws IOException {
        Date from = new Date(0);
        Date to = null;
        try {
            to = sdf.parse(toStr);
        } catch (ParseException ex) {
            to = new Date();
        }
        List<BankStatement> filteredByDate = DB.filterByDate(DB.readData(request.getServletContext()), from, to);
        if (filteredByDate != null) {
            DB.saveData(request.getServletContext(), filteredByDate);
        }
        return Response
                .status(Response.Status.OK).
                build();
    }

    /**
     * Reads data from CSV file
     *
     * @return set of strings with unique account numbers
     * @throws IOException
     */

    @GET
    @Path("accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> showAccounts() throws IOException {
        Set<String> set = DB.getAccounts(request.getServletContext());
        return set;
    }

    /**
     *
     * @param account fetched from URL path (when date from and date to is not
     * provided) default dates are set
     * @return Balance object for the requested account in JSON
     * @throws IOException
     */
    @GET
    @Path("accounts/{account}")
    @Produces(MediaType.APPLICATION_JSON)
    public Balance getBalanceDateless(
            @PathParam("account") String account
    ) throws IOException {
        List<BankStatement> bs = DB.filterByAccountNumber(DB.readData(request.getServletContext()), account);
        Balance b = null;
        if (!bs.isEmpty()) {
            Date from = new Date(0);
            Date to = new Date();
            String currency = bs.get(0).getCurrency();
            BigDecimal balance = DB.filterByDate(bs, from, to).
                    stream().map(bsa -> bsa.getAmount()).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            b = new Balance(account, balance, currency);
        }
        return b;
    }

    /**
     *
     * @param account fetched from URL path
     * @param fromStr fetched from URL path, if date cannot be parsed from the
     * parameter a date 1970-01-01 is set as date from
     * @param toStr fetched from URL path if date cannot be parsed from the
     * parameter a current is set as date to
     * @return Balance object for the requested account in a requested period in
     * JSON
     * @throws IOException
     */

    @GET
    @Path("accounts/{account}/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Balance getBalance(
            @PathParam("account") String account,
            @PathParam("from") String fromStr,
            @PathParam("to") String toStr) throws IOException {
        List<BankStatement> bs = DB.filterByAccountNumber(DB.readData(request.getServletContext()), account);
        Balance b = null;
        if (!bs.isEmpty()) {
            Date from = null;
            Date to = null;
            try {
                from = sdf.parse(fromStr);
            } catch (ParseException ex) {
                from = new Date(0);
            }
            try {
                to = sdf.parse(toStr);
            } catch (ParseException ex) {
                to = new Date();
            }
            String currency = bs.get(0).getCurrency();
            BigDecimal balance = DB.filterByDate(bs, from, to).
                    stream().map(bsa -> bsa.getAmount()).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            b = new Balance(account, balance, currency);
        }
        return b;
    }
    /**
     *
     * @param account fetched from URL path when date to is not provided 
     * (current date set as default)
     * @param fromStr fetched from URL path, if date cannot be parsed from the
     * parameter a date 1970-01-01 is set as date from     
     * @return Balance object for the requested account in a requested period in
     * JSON
     * @throws IOException
     */

    @GET
    @Path("accounts/{account}/{from}")
    @Produces(MediaType.APPLICATION_JSON)
    public Balance getBalanceFrom(
            @PathParam("account") String account,
            @PathParam("from") String fromStr) throws IOException {
        List<BankStatement> bs = DB.filterByAccountNumber(DB.readData(request.getServletContext()), account);
        Balance b = null;
        if (!bs.isEmpty()) {
            Date from = null;
            Date to = new Date();
            try {
                from = sdf.parse(fromStr);
            } catch (ParseException ex) {
                from = new Date(0);
            }

            String currency = bs.get(0).getCurrency();
            BigDecimal balance = DB.filterByDate(bs, from, to).
                    stream().map(bsa -> bsa.getAmount()).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            b = new Balance(account, balance, currency);
        }
        return b;
    }
    
    /**
     *
     * @param account fetched from URL path when date from is not provided 
     * (  date set as default)
     * @param toStr fetched from URL path, if date cannot be parsed from the
     * parameter a current date is set as date to     
     * @return Balance object for the requested account in a requested period in
     * JSON
     * @throws IOException
     */

    @GET
    @Path("accounts/{account}/from/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public Balance getBalanceTo(
            @PathParam("account") String account,
            @PathParam("to") String toStr) throws IOException {
        List<BankStatement> bs = DB.filterByAccountNumber(DB.readData(request.getServletContext()), account);
        Balance b = null;
        if (!bs.isEmpty()) {
            Date from = null;
            Date to = new Date();
            try {
                from = sdf.parse(toStr);
            } catch (ParseException ex) {
                from = new Date(0);
            }

            String currency = bs.get(0).getCurrency();
            BigDecimal balance = DB.filterByDate(bs, from, to).
                    stream().map(bsa -> bsa.getAmount()).
                    reduce(BigDecimal.ZERO, BigDecimal::add);
            b = new Balance(account, balance, currency);
        }
        return b;
    }
}
