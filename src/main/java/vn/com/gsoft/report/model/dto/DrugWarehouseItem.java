package vn.com.gsoft.report.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DrugWarehouseItem {
    public Long drugId;
    public int order;
    public String drugCode;
    public String drugName;
    public String drugRetailUnitName;
    public BigDecimal firstInventoryQuantity;
    public BigDecimal firstInventoryValue;
    public BigDecimal receiptInventoryQuantityInPeriod;
    public BigDecimal receiptInventoryValueInPeriod;
    public BigDecimal deliveryInventoryQuantityInPeriod;
    public BigDecimal deliveryInventoryValueInPeriod;
    public BigDecimal lastInventoryQuantity;
    public BigDecimal lastInventoryValue;

    public BigDecimal firstInventoryQuantity1;
    public BigDecimal firstInventoryValue1;
    public BigDecimal receiptInventoryQuantityInPeriod1;
    public BigDecimal receiptInventoryValueInPeriod1;
    public BigDecimal deliveryInventoryQuantityInPeriod1;
    public BigDecimal deliveryInventoryValueInPeriod1;
    public BigDecimal lastInventoryQuantity1;
    public BigDecimal lastInventoryValue1;

    public DrugWarehouseItem() {
    }
}
