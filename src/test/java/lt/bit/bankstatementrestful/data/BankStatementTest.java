/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package lt.bit.bankstatementrestful.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author elzbi
 */
public class BankStatementTest {
    
    public BankStatementTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void contructorWithLine() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        BankStatement bs= new BankStatement("AccountNumber,2022-05-05,Beneficiary,comment,200.55,currency");
        assertEquals("AccountNumber", bs.getAccountNumber());
        assertEquals("2022-05-05", sdf.format(bs.getOperationDate()));
        assertEquals("Beneficiary", bs.getBeneficiary());
        assertEquals("comment",bs.getComment());
        assertEquals(new BigDecimal("200.55"),bs.getAmount());
        assertEquals("currency",bs.getCurrency());
        
        assertThrows(ParseException.class, () -> new BankStatement("a,b,c,d,e,f"));
        assertThrows(IllegalArgumentException.class, () -> new BankStatement("a,b,d,e,f"));

    }
}
