package vn.com.gsoft.report.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class InOutCommingNoteReportResponse {

    /// <summary>
    ///  1. Tổng tiền đầu kỳ
    /// </summary>
    public BigDecimal firstTotalAmount;
    /// <summary>
    ///  2. Tổng tiền cuối kỳ
    /// </summary>
    public BigDecimal endTotalAmount;
    /// <summary>
    ///  3. Tổng bán
    /// </summary>
    public BigDecimal sellTotalAmount;
    /// <summary>
    ///  4. Tổng thu
    /// </summary>
    public BigDecimal inCommingTotalAmount;
    /// <summary>
    ///  5. Tổng chi
    /// </summary>
    public BigDecimal outCommingTotalAmount;

    public List<InOutCommingNoteReportModel> listDetail = new ArrayList<>();

}
