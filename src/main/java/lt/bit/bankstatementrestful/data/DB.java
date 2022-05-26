package lt.bit.bankstatementrestful.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;


public class DB {
    /**
     * reads data from CSV file (location: "/WEB-INF/bankStatements.csv" ) 
     * and parses BankStatement object from each line
     * 
     * @param app ServletContext
     * @return list of correctly parsed BankStatement objects
     * @throws IOException 
     */

    public static List<BankStatement> readData(ServletContext app) throws IOException {
        List<BankStatement> list = new ArrayList();
        try (
                InputStream is = app.getResourceAsStream("/WEB-INF/bankStatements.csv");
                Reader r = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(r);) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    list.add(new BankStatement(line));
                } catch (IllegalArgumentException | ParseException ex) {
                    //if line can't be parsed the object will be skipped
                }

            }
        }
        return list;
    }
    /**
     * 
     * @param app ServletContext
     * @param list writes to CSV file (location "/WEB-INF/filtered.csv")
     * @throws IOException 
     */

    public static void saveData(ServletContext app, List<BankStatement> list) throws IOException {
        URL url = app.getResource("/WEB-INF/filtered.csv");
        try (
                OutputStream os = new FileOutputStream(url.getFile());
                Writer w = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter bw = new BufferedWriter(w);) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (BankStatement bs : list) {
                String line
                        = bs.getAccountNumber() + ","
                        + sdf.format(bs.getOperationDate()) + ","
                        + bs.getBeneficiary() + ","
                        + (bs.getComment() != null ? bs.getComment() : "") + ","
                        + bs.getAmount() + ","
                        + bs.getCurrency();
                bw.write(line);
                bw.write("\r\n");
            }
            bw.flush();
            w.flush();
            os.flush();
        }
    }
    
    /**
     * 
     * @param app ServletContext
     * @return set of Strings containing account numbers read from CSV file
     * @throws IOException 
     */
    
    public static Set<String> getAccounts(ServletContext app) throws IOException {
        Set<String> set = new HashSet();
        List<BankStatement> list=readData(app);
        for (BankStatement bs : list) {
            set.add(bs.getAccountNumber());
        }
        return set;
    }
    /**
     * Filters list of BankStatements with operation date within the requested period of time
     * @param list
     * @param from
     * @param to
     * @return 
     */

    public static List<BankStatement> filterByDate(List<BankStatement> list, Date from, Date to) {
        List<BankStatement> filteredList = list.stream().
                filter(bs -> bs.getOperationDate().getTime() >= from.getTime()&& 
                        bs.getOperationDate().getTime() <= to.getTime()).
                toList();
        return filteredList;
    }
    
    /**
     * Filters list of BankStatements for the requested account number
     * @param list
     * @param an for Account number
     * @return 
     */
    public static List<BankStatement> filterByAccountNumber(List<BankStatement> list, String an){
        List<BankStatement> filteredList = list.stream().
                filter(bs -> an.equals(bs.getAccountNumber())).
                toList();
        return filteredList;
    }
}
