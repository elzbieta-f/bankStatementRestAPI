
package lt.bit.bankstatementrestful.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import lt.bit.bankstatementrestful.data.BankStatement;

@Path("BankStatements")
public class BankStatements {
    @Context
    HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BankStatement> getAll() throws IOException{
        List<BankStatement> list =new ArrayList();
        try (
                InputStream is = request.getServletContext().getResourceAsStream("/WEB-INF/bankStatements.csv");
                Reader r = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(r);
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(new BankStatement());
            }
        }
        return list;
    }
}
