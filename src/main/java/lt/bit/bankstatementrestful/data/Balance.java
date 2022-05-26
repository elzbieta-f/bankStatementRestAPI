
package lt.bit.bankstatementrestful.data;

import java.math.BigDecimal;
import java.util.Objects;


public class Balance {
    private String accountNumber;
    
    private BigDecimal result;
    
    private String currency;

    public Balance() {
    }

    public Balance(String accountNumber, BigDecimal result, String currency) {
        this.accountNumber = accountNumber;
        this.result = result;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.accountNumber);
        hash = 67 * hash + Objects.hashCode(this.result);
        hash = 67 * hash + Objects.hashCode(this.currency);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Balance other = (Balance) obj;
        if (!Objects.equals(this.accountNumber, other.accountNumber)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.result, other.result)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Balance{" + "accountNumber=" + accountNumber + ", result=" + result + ", currency=" + currency + '}';
    }
    
    
    
    
}
