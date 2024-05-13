package vn.com.gsoft.report.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class RevenueDrugSynthesisResponse {

    public double total;
    public double rRevenue;
    public double deliveryTotal;
    public double debtTotal;
    public double noteDiscountTotal;
    public double paymentScoreAmountTotal;
//    public bool HasDebtValue{ get { return DebtTotal > AppConstants.EspQuantity; } }
    public double debtTotalPaymentMoney;
    public double debtTotalPaymentTranfer;
    public double totalPaymentMoney;
    public double totalPaymentTranfer;
    public double deliveryTotalPayMoney;
    public double deliveryTotalPayTranfer;
    public double discount;
    public double totalAmount;
    public double returnItemTotal;

}
