package vn.com.gsoft.report.model.dto;


import lombok.Data;
import org.springframework.data.domain.Page;
import vn.com.gsoft.report.entity.Thuocs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DrugWarehouseResponse {

    public BigDecimal firsInventoryValueTotal ;
    public BigDecimal receiptValueTotal ;
    public BigDecimal deliveryValueTotal ;
    public BigDecimal lastInventoryValueTotal ;

    public BigDecimal firsInventoryValueTotal1 ;
    public BigDecimal receiptValueTotal1 ;
    public BigDecimal deliveryValueTotal1 ;
    public BigDecimal lastInventoryValueTotal1 ;

    public Page<Thuocs> pageDetail;


}
