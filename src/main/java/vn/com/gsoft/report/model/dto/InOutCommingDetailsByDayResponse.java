package vn.com.gsoft.report.model.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InOutCommingDetailsByDayResponse {

    ///  1. Tổng thu
    /// </summary>
    public double InCommingTotalAmount;
    /// <summary>
    ///  2. Tổng chi
    /// </summary>
    public double OutCommingTotalAmount;
    /// <summary>
    ///  3. Tổng khách nợ
    /// </summary>
    public double DebtTotalAmount;

    public List<InOutCommingDetailsByDayModel> listDetail = new ArrayList<>();

}
