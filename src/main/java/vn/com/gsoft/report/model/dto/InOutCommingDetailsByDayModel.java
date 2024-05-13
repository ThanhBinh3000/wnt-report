package vn.com.gsoft.report.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InOutCommingDetailsByDayModel {
    public int order ;
    public String noteType ;
    public BigDecimal outCommingCashAmount ;
    public BigDecimal outCommingTransferAmount ;
    public BigDecimal inCommingCashAmount ;
    public BigDecimal inCommingTransferAmount ;
    public BigDecimal sellTotalAmount ;
    public BigDecimal buyTotalAmount ;
    public int totalCustomerDebt ;
    public int totalSupplierDebt ;
    public int totalCustomerReturn ;
    public int totalSupplierReturn ;
    public int totalOutReturnCustomer ;
    public int totalInReturnSupplier ;
    public String Description ;
    public String Link ;


    public InOutCommingDetailsByDayModel() {
    }
}
