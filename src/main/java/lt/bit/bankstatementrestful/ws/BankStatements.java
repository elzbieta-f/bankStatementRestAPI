package lt.bit.bankstatementrestful.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BankStatement> getAll() throws IOException {
        List<BankStatement> list = DB.readData(request.getServletContext());
        return list;
    }

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
    @Path("accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> showAccounts() throws IOException {
        Set<String> set = DB.getAccounts(request.getServletContext());
        return set;
    }

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
}
