package vn.com.gsoft.report.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
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
@Table(name = "Thuocs")
public class Thuocs extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "MaThuoc")
    private String maThuoc; // Mã Thuốc
    @Column(name = "TenThuoc")
    private String tenThuoc; // Tên Thuốc
    @Column(name = "ThongTin")
    private String thongTin; // Thông Tin
    @Column(name = "HeSo")
    private Integer heSo; // Hệ số
    @Column(name = "GiaNhap")
    private BigDecimal giaNhap; // Giá Nhập
    @Column(name = "GiaBanBuon")
    private BigDecimal giaBanBuon; // Giá Bán Buôn
    @Column(name = "GiaBanLe")
    private BigDecimal giaBanLe; // Giá Bán Lẻ
    @Column(name = "SoDuDauKy")
    private BigDecimal soDuDauKy; // Số Dư Đầu Kỳ
    @Column(name = "GiaDauKy")
    private BigDecimal giaDauKy; // Giá Đầu Kỳ
    @Column(name = "GioiHan")
    private Integer gioiHan; // Giới Hạn
    @Column(name = "NhaThuoc_MaNhaThuoc")
    private String nhaThuocMaNhaThuoc; // Mã Nhà Thuốc
    @Column(name = "NhomThuoc_MaNhomThuoc")
    private Long nhomThuocMaNhomThuoc; // Mã Nhóm Thuốc
    @Column(name = "Nuoc_MaNuoc")
    private Integer nuocMaNuoc; // Mã Nước
    @Column(name = "DangBaoChe_MaDangBaoChe")
    private Integer dangBaoCheMaDangBaoChe; // Mã Dạng Bào Chế
    @Column(name = "DonViXuatLe_MaDonViTinh")
    private Long donViXuatLeMaDonViTinh; // Mã Đơn Vị Xuất Lẻ
    @Column(name = "DonViThuNguyen_MaDonViTinh")
    private Long donViThuNguyenMaDonViTinh; // Mã Đơn Vị Thu Nguyên
    @Column(name = "BarCode")
    private String barCode; // Mã Vạch
    @Column(name = "HoatDong")
    private Boolean hoatDong; // Hoạt Động
    @Column(name = "HangTuVan")
    private Boolean hangTuVan; // Hãng Tư Vấn
    @Column(name = "HanDung")
    private Date hanDung; // Hạn Dùng
    @Column(name = "DuTru")
    private Integer duTru; // Dự Trữ
    @Column(name = "Active")
    private Boolean active; // Hoạt Động
    @Column(name = "NhaThuoc_MaNhaThuocCreate")
    private String nhaThuocMaNhaThuocCreate; // Mã Nhà Thuốc (Tạo)
    @Column(name = "ConnectivityDrugID")
    private Long connectivityDrugID; // ID Liên Kết Thuốc
    @Column(name = "ConnectivityDrugFactor")
    private BigDecimal connectivityDrugFactor; // Yếu Tố Liên Kết Thuốc
    @Column(name = "MaNhaCungCap")
    private Integer maNhaCungCap; // Mã Nhà Cung Cấp
    @Column(name = "ParentDrugId")
    private Long parentDrugId; // ID Thuốc Cha
    @Column(name = "MetadataHash")
    private Integer metadataHash; // Metadata Hash
    @Column(name = "RpMetadataHash")
    private Integer rpMetadataHash; // Metadata Hash RP
    @Column(name = "ReferenceId")
    private Long referenceId; // ID Tham Chiếu
    @Column(name = "Discount")
    private BigDecimal discount; // Chiết Khấu
    @Column(name = "DiscountByRevenue")
    private Boolean discountByRevenue; // Chiết Khấu Theo Doanh Thu
    @Column(name = "SaleTypeId")
    private Long saleTypeId; // ID Loại Giảm Giá
    @Column(name = "SaleOff")
    private BigDecimal saleOff; // Giảm Giá
    @Column(name = "SaleDescription")
    private String saleDescription; // Mô Tả Giảm Giá
    @Column(name = "SaleStartDate")
    private Date saleStartDate; // Ngày Bắt Đầu Giảm Giá
    @Column(name = "SaleEndDate")
    private Date saleEndDate; // Ngày Kết Thúc Giảm Giá
    @Column(name = "Scorable")
    private Boolean scorable; // Có Thể Điểm
    @Column(name = "ImageThumbUrl")
    private String imageThumbUrl; // URL Ảnh Thumbnail
    @Column(name = "ImagePreviewUrl")
    private String imagePreviewUrl; // URL Ảnh Xem Trước
    @Column(name = "ConnectivityTypeId")
    private Long connectivityTypeId; // ID Loại Kết Nối
    @Column(name = "ArchivedId")
    private Long archivedId; // ID Lưu Trữ
    @Column(name = "StoreId")
    private Long storeId; // ID Cửa Hàng
    @Column(name = "ProductTypeId")
    private Long productTypeId; // ID Loại Sản Phẩm
    @Column(name = "SerialNumber")
    private String serialNumber; // Số Serial
    @Column(name = "MoneyToOneScoreRate")
    private BigDecimal moneyToOneScoreRate; // Tỷ Lệ Tiền Thành Điểm
    @Column(name = "Presentation")
    private Boolean presentation; // Trình Bày
    @Column(name = "NameHash")
    private Long nameHash; // Hash Tên
    @Column(name = "RegisteredNo")
    private String registeredNo; // Số Đăng Ký
    @Column(name = "ActiveSubstance")
    private String activeSubstance; // Hoạt Chất
    @Column(name = "Contents")
    private String contents; // Nội Dung
    @Column(name = "PackingWay")
    private String packingWay; // Cách Đóng Gói
    @Column(name = "Manufacturer")
    private String manufacturer; // Nhà Sản Xuất
    @Column(name = "CountryOfManufacturer")
    private String countryOfManufacturer; // Quốc Gia Sản Xuất
    @Column(name = "CountryId")
    private Long countryId; // ID Quốc Gia
    @Column(name = "ConnectivityId")
    private String connectivityId; // ID Kết Nối
    @Column(name = "ConnectivityResult")
    private String connectivityResult; // Kết Quả Kết Nối
    @Column(name = "ConnectivityDateTime")
    private Date connectivityDateTime; // Thời Gian Kết Nối
    @Column(name = "DosageForms")
    private String dosageForms; // Hình Thức Liều Dùng
    @Column(name = "SmallestPackingUnit")
    private String smallestPackingUnit; // Đơn Vị Đóng Gói Nhỏ Nhất
    @Column(name = "Importers")
    private String importers; // Nhà Nhập Khẩu
    @Column(name = "DeclaredPrice")
    private BigDecimal declaredPrice; // Giá Khai Báo
    @Column(name = "ConnectivityCode")
    private String connectivityCode; // Mã Kết Nối
    @Column(name = "CodeHash")
    private Long codeHash; // Hash Mã
    @Column(name = "ConnectivityStatusId")
    private Long connectivityStatusId; // ID Trạng Thái Kết Nối
    @Column(name = "OrganizeDeclaration")
    private String organizeDeclaration; // Tổ Chức Tuyên Bố
    @Column(name = "CountryRegistration")
    private String countryRegistration; // Quốc Gia Đăng Ký
    @Column(name = "AddressRegistration")
    private String addressRegistration; // Địa Chỉ Đăng Ký
    @Column(name = "AddressManufacture")
    private String addressManufacture; // Địa Chỉ Sản Xuất
    @Column(name = "Identifier")
    private String identifier; // Định Danh
    @Column(name = "Classification")
    private String classification; // Phân Loại
    @Column(name = "ForWholesale")
    private Integer forWholesale; // Bán Buôn
    @Column(name = "HoatChat")
    private String hoatChat; // Hoạt Chất
    @Column(name = "TypeService")
    private Integer typeService; // Loại Dịch Vụ
    @Column(name = "TypeServices")
    private Integer typeServices; // Loại Dịch Vụ
    @Column(name = "IdTypeService")
    private Long idTypeService; // ID Loại Dịch Vụ
    @Column(name = "IdClinic")
    private Long idClinic; // ID Phòng Khám
    @Column(name = "CountNumbers")
    private Integer countNumbers; // Số Lượng
    @Column(name = "IdWarehouseLocation")
    private Long idWarehouseLocation; // ID Vị Trí Kho
    @Column(name = "HamLuong")
    private String hamLuong; // Hàm Lượng
    @Column(name = "QuyCachDongGoi")
    private String quyCachDongGoi; // Quy Cách Đóng Gói
    @Column(name = "NhaSanXuat")
    private String nhaSanXuat; // Nhà Sản Xuất
    @Column(name = "Advantages")
    private String advantages; // Ưu Điểm
    @Column(name = "UserObject")
    private String userObject; // Đối Tượng Sử Dụng
    @Column(name = "UserManual")
    private String userManual; // Hướng Dẫn Sử Dụng
    @Column(name = "Pharmacokinetics")
    private String pharmacokinetics; // Dược Động Học
    @Column(name = "IsShow_CustomerWebsite")
    private Boolean isShowCustomerWebsite; // Hiển Thị trên Website Khách Hàng
    @Column(name = "Flag")
    private Boolean flag; // Cờ
    @Column(name = "GroupIdMapping")
    private Long groupIdMapping; // ID Nhóm Kết Nối
    @Column(name = "GroupNameMapping")
    private String groupNameMapping; // Tên Nhóm Kết Nối
    @Column(name = "ResultService")
    private String resultService; // Kết Quả Dịch Vụ
    @Column(name = "TitleResultService")
    private String titleResultService; // Tiêu Đề Kết Quả Dịch Vụ
    @Column(name = "TypeResultService")
    private Integer typeResultService; // Loại Kết Quả Dịch Vụ
    @Column(name = "GroupIdMappingV2")
    private Long groupIdMappingV2; // ID Nhóm Kết Nối V2
    @Column(name = "StorageConditions")
    private String storageConditions; // Điều Kiện Bảo Quản
    @Column(name = "StorageLocation")
    private String storageLocation; // Vị Trí Bảo Quản
    @Column(name = "MappingDate")
    private Date mappingDate; // Ngày Kết Nối
    @Column(name = "ChiDinh")
    private String chiDinh; // Chỉ Định
    @Column(name = "ChongChiDinh")
    private String chongChiDinh; // Chống Chỉ Định
    @Column(name = "XuatXu")
    private String xuatXu; // Xuất Xứ
    @Column(name = "LuuY")
    private String luuY; // Lưu Ý
    @Column(name = "PromotionalDiscounts")
    private BigDecimal promotionalDiscounts; // Chiết Khấu Khuyến Mãi
    @Column(name = "EnablePromotionalDiscounts")
    private Boolean enablePromotionalDiscounts; // Cho Phép Chiết Khấu Khuyến Mãi
    @Column(name = "DescriptionOnWebsite")
    private String descriptionOnWebsite; // Mô Tả Trên Website
    @Column(name = "ImgReferenceDrugId")
    private Long imgReferenceDrugId; // ID Ảnh Tham Khảo Thuốc
    @Column(name = "UserUploadImgDate")
    private Date userUploadImgDate; // Ngày Tải Ảnh Lên
    @Column(name = "UserUploadImgId")
    private Long userUploadImgId; // ID Người Tải Ảnh Lên
    @Column(name = "StatusConfirm")
    private Integer statusConfirm; // Trạng Thái Xác Nhận
    @Column(name = "UserIdConfirm")
    private Long userIdConfirm; // ID Người Xác Nhận
    @Column(name = "UserIdMapping")
    private Long userIdMapping; // ID Người Kết Nối

    // @Transient

    @Transient
    private String tenNhomThuoc; // Tên Nhóm Thuốc

    @Transient
    private String tenDonViTinhXuatLe; // Tên Đơn Vị Tính Xuất Lẻ

    @Transient
    private String tenDonViTinhThuNguyen; // Tên Đơn Vị Tính Thu Nguyên

    @Transient
    private String tenViTri; // Tên Vị Trí
}