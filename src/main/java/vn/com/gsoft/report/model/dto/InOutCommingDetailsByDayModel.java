package vn.com.gsoft.report.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InOutCommingDetailsByDayModel {
    public int Order ;
    public String NoteType ;
    public BigDecimal OutCommingCashAmount ;
    public BigDecimal OutCommingTransferAmount ;
    public BigDecimal InCommingCashAmount ;
    public BigDecimal InCommingTransferAmount ;
    public BigDecimal SellTotalAmount ;
    public BigDecimal BuyTotalAmount ;
    public int TotalCustomerDebt ;
    public int TotalSupplierDebt ;
    public int TotalCustomerReturn ;
    public int TotalSupplierReturn ;
    public String Description ;
    public String Link ;


    public InOutCommingDetailsByDayModel() {
    }
}
