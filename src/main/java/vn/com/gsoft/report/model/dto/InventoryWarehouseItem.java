package vn.com.gsoft.report.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InventoryWarehouseItem {
    public int order ;
    public Long inventoryItemId ;
    public Long noteNumber ;
    public Long noteId ;
    public BigDecimal amount ;
    public BigDecimal totalAmount ;
    public BigDecimal amountByOutPrice ;
    public BigDecimal quantity ;
    public BigDecimal price ;
    public BigDecimal outPrice ;

    public Date itemDate;

    public Long itemId;

    private String itemCode;

    private String itemName;

    private String unitName;



    public InventoryWarehouseItem() {

    }

}
