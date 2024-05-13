package vn.com.gsoft.report.entity.ReportingDate;

import jakarta.persistence.*;
import lombok.*;
import vn.com.gsoft.report.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ReceiptDrugPriceRef")
public class ReceiptDrugPriceRef {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DrugId")
    private Long drugId;
    @Column(name = "ReceiptNoteItemId")
    private Long receiptNoteItemId;
    @Column(name = "DeliveryNoteItemId")
    private Long deliveryNoteItemId;
    @Column(name = "Price")
    private Long price;
    @Column(name = "Quantity")
    private Long quantity;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @Column(name = "ReferencePriceTypeId")
    private Long referencePriceTypeId;

    @Column(name = "DrugStoreCode")
    private String drugStoreCode;

    @Column(name = "CreatedDateTime")
    private Date createdDateTime;

    @Column(name = "BatchGuid")
    private Long batchGuid;

    @Column(name = "Comments")
    private String comments;

    @Column(name = "StoreId")
    private Long storeId;

    @Transient
    private BigDecimal finalRetailPrice;
    @Transient
    private Long noteTypeId;

    @Transient
    private Long noteId;
}