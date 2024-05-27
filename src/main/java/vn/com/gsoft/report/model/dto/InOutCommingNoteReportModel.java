package vn.com.gsoft.report.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InOutCommingNoteReportModel {
    public int order ;
    public String childStoreCode ;
    public String childStoreName ;
    public Long staffId ;
    public String staffName ;
    public BigDecimal firstAmount ;
    public BigDecimal outCommingAmount ;
    public BigDecimal sellAmount ;
    public BigDecimal inCommingAmount ;
    public BigDecimal endAmount ;


    public InOutCommingNoteReportModel() {

    }

    public BigDecimal getEndAmount() {
        return firstAmount.add(inCommingAmount).subtract(outCommingAmount);
    }
}
