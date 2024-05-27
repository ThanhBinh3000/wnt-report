package vn.com.gsoft.report.model.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class InventoryWarehouseResponse {

    public BigDecimal totalAmount;
    public BigDecimal totalAmountByOutPrice;

    public List<InventoryWarehouseItem> listDetail = new ArrayList<>();


}
