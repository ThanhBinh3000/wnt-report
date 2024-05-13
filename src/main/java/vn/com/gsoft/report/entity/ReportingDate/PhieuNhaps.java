package vn.com.gsoft.report.entity.ReportingDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import vn.com.gsoft.report.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuNhaps")
public class PhieuNhaps extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SoPhieuNhap")
    private Long soPhieuNhap; // Số Phiếu Nhập
    @Column(name = "NgayNhap")
    private Date ngayNhap; // Ngày Nhập
    @Column(name = "VAT")
    private Integer vat; // VAT
    @Column(name = "DienGiai")
    private String dienGiai; // Diễn Giải
    @Column(name = "TongTien")
    private BigDecimal tongTien; // Tổng Tiền
    @Column(name = "DaTra")
    private BigDecimal daTra; // Đã Trả
    @Column(name = "NhaThuoc_MaNhaThuoc")
    private String nhaThuocMaNhaThuoc; // Mã Nhà Thuốc
    @Column(name = "LoaiXuatNhap_MaLoaiXuatNhap")
    private Long loaiXuatNhapMaLoaiXuatNhap; // Mã Loại Xuất Nhập
    @Column(name = "NhaCungCap_MaNhaCungCap")
    private Long nhaCungCapMaNhaCungCap; // Mã Nhà Cung Cấp
    @Column(name = "KhachHang_MaKhachHang")
    private Long khachHangMaKhachHang; // Mã Khách Hàng
    @Column(name = "Active")
    private Boolean active; // Hoạt Động
    @Column(name = "IsModified")
    private Boolean isModified; // Đã Sửa Đổi
    @Column(name = "Locked")
    private Boolean locked; // Đã Khóa
    @Column(name = "IsDebt")
    private Boolean isDebt; // Nợ
    @Column(name = "PreNoteDate")
    private Date preNoteDate; // Ngày Ghi Chú Trước
    @Column(name = "ConnectivityNoteID")
    private String connectivityNoteID; // ID Ghi Chú Kết Nối
    @Column(name = "ConnectivityStatusID")
    private Long connectivityStatusID; // ID Trạng Thái Kết Nối
    @Column(name = "ConnectivityResult")
    private String connectivityResult; // Kết Quả Kết Nối
    @Column(name = "ConnectivityDateTime")
    private Date connectivityDateTime; // Thời Gian Kết Nối
    @Column(name = "OrderId")
    private Long orderId; // ID Đơn Đặt Hàng
    @Column(name = "ArchivedId")
    private Long archivedId; // ID Đã Lưu Trữ
    @Column(name = "StoreId")
    private Long storeId; // ID Cửa Hàng
    @Column(name = "TargetId")
    private Long targetId; // ID Đích
    @Column(name = "SourceId")
    private Long sourceId; // ID Nguồn
    @Column(name = "SourceStoreId")
    private Long sourceStoreId; // ID Kho Nguồn
    @Column(name = "TargetStoreId")
    private Long targetStoreId; // ID Kho Đích
    @Column(name = "PartnerId")
    private Long partnerId; // ID Đối Tác
    @Column(name = "UId")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uId; // UUID
    @Column(name = "InvoiceCode")
    private String invoiceCode; // Mã Hóa Đơn
    @Column(name = "InvoiceNo")
    private String invoiceNo; // Số Hóa Đơn
    @Column(name = "Score")
    private BigDecimal score; // Điểm
    @Column(name = "PreScore")
    private BigDecimal preScore; // Điểm Trước
    @Column(name = "ReferenceKey")
    private String referenceKey; // Khóa Tham Chiếu
    @Column(name = "InvoiceDate")
    private Date invoiceDate; // Ngày Hóa Đơn
    @Column(name = "InvoiceTemplateCode")
    private String invoiceTemplateCode; // Mã Mẫu Hóa Đơn
    @Column(name = "InvoiceSeries")
    private String invoiceSeries; // Loại Hóa Đơn
    @Column(name = "InvoiceType")
    private String invoiceType; // Loại Hóa Đơn
    @Column(name = "ArchivedDate")
    private Date archivedDate; // Ngày Lưu Trữ
    @Column(name = "TimeTypeId")
    private Long timeTypeId; // ID Loại Thời Gian
    @Column(name = "NoteName")
    private String noteName; // Tên Ghi Chú
    @Column(name = "Notes")
    private String notes; // Ghi Chú
    @Column(name = "Reasons")
    private String reasons; // Lý Do
    @Column(name = "SynStatusId")
    private Long synStatusId; // ID Trạng Thái Đồng Bộ
    @Column(name = "PaymentTypeId")
    private Long paymentTypeId; // ID Loại Thanh Toán
    @Column(name = "DebtPaymentAmount")
    private BigDecimal debtPaymentAmount; // Số Tiền Thanh Toán Nợ
    @Column(name = "PickUpOrderId")
    private Long pickUpOrderId; // ID Đơn Đặt Hàng Lấy Hàng
    @Lob
    @Column(name = "LinkFile")
    private String linkFile; // Đường Dẫn Tệp Tin
    @Column(name = "Discount")
    private BigDecimal discount; // Giảm Giá
    @Column(name = "TargetManagementId")
    private Long targetManagementId; // ID Quản Lý Đích
}