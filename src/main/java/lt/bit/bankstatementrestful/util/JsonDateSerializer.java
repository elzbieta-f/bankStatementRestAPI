package lt.bit.bankstatementrestful.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.bind.adapter.JsonbAdapter;

public class JsonDateSerializer implements JsonbAdapter<Date, String>{

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    @Override
    public String adaptToJson(Date date) throws Exception {
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    @Override
    public Date adaptFromJson(String dateStr) throws Exception {
        return sdf.parse(dateStr);
    }
    
}
