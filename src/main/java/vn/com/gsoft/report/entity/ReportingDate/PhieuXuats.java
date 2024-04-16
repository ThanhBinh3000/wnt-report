package vn.com.gsoft.report.entity.ReportingDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import vn.com.gsoft.report.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuXuats")
public class PhieuXuats extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SoPhieuXuat")
    private Long soPhieuXuat; // Số phiếu xuất
    @Column(name = "NgayXuat")
    private Date ngayXuat; // Ngày xuất
    @Column(name = "VAT")
    private Integer vat; // Thuế VAT
    @Column(name = "DienGiai")
    private String dienGiai; // Diễn giải
    @Column(name = "TongTien")
    private BigDecimal tongTien; // Tổng tiền
    @Column(name = "DaTra")
    private BigDecimal daTra; // Đã trả
    @Column(name = "NhaThuoc_MaNhaThuoc")
    private String nhaThuocMaNhaThuoc; // Mã nhà thuốc
    @Column(name = "MaLoaiXuatNhap")
    private Integer maLoaiXuatNhap; // Mã loại xuất nhập
    @Column(name = "KhachHang_MaKhachHang")
    private Long khachHangMaKhachHang; // Mã khách hàng
    @Column(name = "NhaCungCap_MaNhaCungCap")
    private Long nhaCungCapMaNhaCungCap; // Mã nhà cung cấp
    @Column(name = "BacSy_MaBacSy")
    private Integer bacSyMaBacSy; // Mã bác sỹ
    @Column(name = "Active")
    private Boolean active; // Hoạt động
    @Column(name = "IsModified")
    private Boolean isModified; // Đã chỉnh sửa
    @Column(name = "Locked")
    private Boolean locked; // Đã khóa
    @Column(name = "IsDebt")
    private Boolean isDebt; // Là nợ
    @Column(name = "PreNoteDate")
    private Date preNoteDate; // Ngày ghi chú trước
    @Column(name = "ConnectivityNoteID")
    private String connectivityNoteID; // ID ghi chú kết nối
    @Column(name = "ConnectivityStatusID")
    private Long connectivityStatusID; // ID trạng thái kết nối
    @Column(name = "ConnectivityResult")
    private String connectivityResult; // Kết quả kết nối
    @Column(name = "ConnectivityDateTime")
    private Date connectivityDateTime; // Thời gian kết nối
    @Column(name = "OrderId")
    private Long orderId; // ID đơn hàng
    @Column(name = "Discount")
    private BigDecimal discount; // Chiết khấu
    @Column(name = "Score")
    private BigDecimal score; // Điểm
    @Column(name = "PreScore")
    private BigDecimal preScore; // Điểm trước
    @Column(name = "ArchivedId")
    private Long archivedId; // ID đã lưu trữ
    @Column(name = "StoreId")
    private Long storeId; // ID cửa hàng
    @Column(name = "TargetId")
    private Long targetId; // ID mục tiêu
    @Column(name = "SourceId")
    private Long sourceId; // ID nguồn
    @Column(name = "SourceStoreId")
    private Long sourceStoreId; // ID cửa hàng nguồn
    @Column(name = "TargetStoreId")
    private Long targetStoreId; // ID cửa hàng đích
    @Column(name = "PartnerId")
    private Long partnerId; // ID đối tác
    @Column(name = "PrescriptionId")
    private Long prescriptionId; // ID đơn thuốc
    @Column(name = "UId")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uId; // UUID
    @Column(name = "InvoiceCode")
    private String invoiceCode; // Mã hóa đơn
    @Column(name = "InvoiceNo")
    private String invoiceNo; // Số hóa đơn
    @Column(name = "ReferenceKey")
    private String referenceKey; // Khóa tham chiếu
    @Column(name = "InvoiceDate")
    private Date invoiceDate; // Ngày hóa đơn
    @Column(name = "PaymentScore")
    private BigDecimal paymentScore; // Điểm thanh toán
    @Column(name = "PaymentScoreAmount")
    private BigDecimal paymentScoreAmount; // Số điểm thanh toán
    @Column(name = "BonusPaymentId")
    private Long bonusPaymentId; // ID thanh toán thưởng
    @Column(name = "InvoiceTemplateCode")
    private String invoiceTemplateCode; // Mã mẫu hóa đơn
    @Column(name = "InvoiceSeries")
    private String invoiceSeries; // Loạt hóa đơn
    @Column(name = "InvoiceType")
    private String invoiceType; // Loại hóa đơn
    @Column(name = "ArchivedDate")
    private Date archivedDate; // Ngày đã lưu trữ
    @Column(name = "PrePaymentScore")
    private BigDecimal prePaymentScore; // Số điểm thanh toán trước
    @Column(name = "SynStatusId")
    private Long synStatusId; // ID trạng thái đồng bộ
    @Column(name = "TransPaymentAmount")
    private BigDecimal transPaymentAmount; // Số tiền thanh toán chuyển giao
    @Column(name = "PaymentTypeId")
    private Long paymentTypeId; // ID loại thanh toán
    @Column(name = "DebtPaymentAmount")
    private BigDecimal debtPaymentAmount; // Số tiền thanh toán nợ
    @Column(name = "BackPaymentAmount")
    private BigDecimal backPaymentAmount; // Số tiền thanh toán trả lại
    @Column(name = "LinkFile")
    private String linkFile; // Đường dẫn tới tệp
    @Column(name = "DoctorComments")
    private String doctorComments; // Ý kiến của bác s
    @Column(name = "KeyNewEInvoice")
    private String keyNewEInvoice; // Khóa hóa đơn điện tử mới
    @Column(name = "KeyOldEInvoice")
    private String keyOldEInvoice; // Khóa hóa đơn điện tử cũ
    @Column(name = "EInvoiceStatusID")
    private Long eInvoiceStatusID; // ID trạng thái hóa đơn điện tử
    @Column(name = "SignEInvoiceStatusID")
    private Long signEInvoiceStatusID; // ID trạng thái ký hóa đơn điện tử
    @Column(name = "ConnEInvoiceDateTime")
    private Date connEInvoiceDateTime; // Thời gian kết nối hóa đơn điện tử
    @Column(name = "SignEInvoiceDateTime")
    private Date signEInvoiceDateTime; // Thời gian ký hóa đơn điện tử
    @Column(name = "DestroyEInvoiceDateTime")
    private Date destroyEInvoiceDateTime; // Thời gian hủy hóa đơn điện tử
    @Column(name = "ReplacedEInvoiceDateTime")
    private Date replacedEInvoiceDateTime; // Thời gian thay thế hóa đơn điện tử
    @Column(name = "EditedEInvoiceDateTime")
    private Date editedEInvoiceDateTime; // Thời gian chỉnh sửa hóa đơn điện tử
    @Column(name = "KHHDon")
    private String kHHDon; // Ký hiệu hàng hóa đơn
    @Column(name = "KHMSHDon")
    private String kHMSHDon; // Ký hiệu mẫu số hàng hóa đơn
    @Column(name = "SHDon")
    private String sHDon; // Số hàng hóa đơn
    @Column(name = "PickUpOrderId")
    private Long pickUpOrderId; // ID đơn hàng lấy hàng
    @Column(name = "ESampleNoteCode")
    private String eSampleNoteCode; // Mã ghi chú mẫu mẫu
    @Lob
    @Column(name = "LinkConfirm")
    private String linkConfirm; // Đường dẫn xác nhận
    @Column(name = "TaxAuthorityCode")
    private String taxAuthorityCode; // Mã cơ quan thuế
    @Column(name = "EInvoiveResult")
    private String eInvoiveResult; // Kết quả hóa đơn điện tử
    @Column(name = "IsRefSampleNote")
    private Boolean isRefSampleNote; // Là ghi chú mẫu tham chiếu
    @Column(name = "NextPurchaseDate")
    private Date nextPurchaseDate; // Ngày mua tiếp theo
    @Column(name = "TrackingIdZNS")
    private String trackingIdZNS; // ID theo dõi ZNS
    @Column(name = "ResultZNS")
    private String resultZNS; // Kết quả ZNS
    @Column(name = "IdPaymentQR")
    private Long idPaymentQR; // ID thanh toán QR
    @Column(name = "TargetManagementId")
    private Long targetManagementId; // ID quản lý mục tiêu
    @Transient
    private Boolean IsConnectivity; // Đã kết nối
    @Transient
    private Boolean IsManagement; // Quản lý
    @Transient
    private String tenKhachHang; // Tên khách hàng
    @Transient
    private String maThuoc; // Mã thuốc
    @Transient
    private String tenThuoc; // Tên thuốc
    @Transient
    private String tenDonViTinh; // Tên đơn vị tính
    @Transient
    private BigDecimal soLuong; // Số lượng
    @Transient
    private BigDecimal giaXuat; // Giá xuất
    @Transient
    private BigDecimal revenue; // Doanh thu

    @Transient
    List<PhieuXuatChiTiets> children = new ArrayList<>();

    public void setChildren(List<PhieuXuatChiTiets> children) {
        this.children = children;
        if (children != null && !children.isEmpty()) {
            this.soLuong = children.stream().map(PhieuXuatChiTiets::getSoLuong).reduce(BigDecimal.ZERO, BigDecimal::add);
            this.revenue = children.stream().map(PhieuXuatChiTiets::getRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}