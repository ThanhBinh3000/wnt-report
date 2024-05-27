package vn.com.gsoft.report.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.report.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuXuatChiTiets")
public class PhieuXuatChiTiets extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PhieuXuat_MaPhieuXuat")
    private Long phieuXuatMaPhieuXuat; // Mã phiếu xuất
    @Column(name = "NhaThuoc_MaNhaThuoc")
    private String nhaThuocMaNhaThuoc; // Mã nhà thuốc
    @Column(name = "Thuoc_ThuocId")
    private Long thuocThuocId; // ID thuốc
    @Column(name = "DonViTinh_MaDonViTinh")
    private Integer donViTinhMaDonViTinh; // Mã đơn vị tính
    @Column(name = "ChietKhau")
    private BigDecimal chietKhau; // Chiết khấu
    @Column(name = "GiaXuat")
    private BigDecimal giaXuat; // Giá xuất
    @Column(name = "SoLuong")
    private BigDecimal soLuong; // Số lượng
    @Column(name = "Option1")
    private String option1; // Tùy chọn 1
    @Column(name = "Option2")
    private String option2; // Tùy chọn 2
    @Column(name = "Option3")
    private String option3; // Tùy chọn 3
    @Column(name = "RefConnectivityCode")
    private String refConnectivityCode; // Mã kết nối tham chiếu
    @Column(name = "PreQuantity")
    private String preQuantity; // Số lượng trước đó
    @Column(name = "IsReceiptDrugPriceRefGenerated")
    private Boolean isReceiptDrugPriceRefGenerated; // Đã tạo giá thuốc tham chiếu
    @Column(name = "RetailQuantity")
    private Float retailQuantity; // Số lượng bán lẻ
    @Column(name = "HandledStatusId")
    private Long handledStatusId; // ID trạng thái đã xử lý
    @Column(name = "RetailPrice")
    private Float retailPrice; // Giá bán lẻ
    @Column(name = "RequestUpdateFromBkgService")
    private Boolean requestUpdateFromBkgService; // Yêu cầu cập nhật từ dịch vụ bán lẻ
    @Column(name = "ReduceNoteItemIds")
    private String reduceNoteItemIds; // ID của mục giảm
    @Column(name = "ReduceQuantity")
    private Float reduceQuantity; // Số lượng giảm
    @Column(name = "IsModified")
    private Boolean isModified; // Đã sửa đổi
    @Column(name = "ItemOrder")
    private Integer itemOrder; // Thứ tự mục
    @Column(name = "ArchiveDrugId")
    private Long archiveDrugId; // ID thuốc đã lưu trữ
    @Column(name = "ArchiveUnitId")
    private Long archiveUnitId; // ID đơn vị đã lưu trữ
    @Column(name = "PreRetailQuantity")
    private Float preRetailQuantity; // Số lượng bán lẻ trước đó
    @Column(name = "BatchNumber")
    private String batchNumber; // Số lô
    @Column(name = "ExpiredDate")
    private Date expiredDate; // Ngày hết hạn
    @Column(name = "ExpirySetAuto")
    private Boolean expirySetAuto; // Tự động đặt hết hạn
    @Column(name = "ReferenceId")
    private Long referenceId; // ID tham chiếu
    @Column(name = "ArchivedId")
    private Long archivedId; // ID đã lưu trữ
    @Column(name = "StoreId")
    private Long storeId; // ID cửa hàng
    @Column(name = "ConnectivityStatusId")
    private Long connectivityStatusId; // ID trạng thái kết nối
    @Column(name = "ConnectivityResult")
    private String connectivityResult; // Kết quả kết nối
    @Column(name = "VAT")
    private Integer vat; // Thuế VAT
    @Column(name = "Reason")
    private String reason; // Lý do
    @Column(name = "Solution")
    private String solution; // Giải pháp
    @Column(name = "Notes")
    private String notes; // Ghi chú
    @Column(name = "LockReGenReportData")
    private Boolean lockReGenReportData; // Khóa tái tạo dữ liệu báo cáo
    @Column(name = "IsProdRef")
    private Boolean isProdRef; // Là tham chiếu sản phẩm
    @Column(name = "NegativeRevenue")
    private Boolean negativeRevenue; // Doanh thu âm
    @Column(name = "Revenue")
    private BigDecimal revenue; // Doanh thu
    @Column(name = "RefPrice")
    private BigDecimal refPrice; // Giá tham chiếu
    @Column(name = "Usage")
    private String usage; // Cách dùng
    @Column(name = "OutOwnerPriceChild")
    private BigDecimal outOwnerPriceChild; // Giá ngoại vi

    @Transient
    private BigDecimal totalAmount;
}