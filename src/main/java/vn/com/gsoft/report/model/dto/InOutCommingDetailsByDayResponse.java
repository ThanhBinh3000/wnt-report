package vn.com.gsoft.report.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class InOutCommingDetailsByDayResponse {

    ///  1. Tổng thu
    /// </summary>
    public BigDecimal inCommingTotalAmount;
    /// <summary>
    ///  2. Tổng chi
    /// </summary>
    public BigDecimal outCommingTotalAmount;
    /// <summary>
    ///  3. Tổng khách nợ
    /// </summary>
    public BigDecimal debtTotalAmount;

    public List<InOutCommingDetailsByDayModel> listDetail = new ArrayList<>();

}
