package vn.com.gsoft.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.entity.PhieuXuats;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.repository.BaseRepository;

import java.util.List;

@Repository
public interface PhieuXuatsRepository extends BaseRepository<PhieuXuats, PhieuXuatsReq, Long> {
    @Query("SELECT c FROM PhieuXuats c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.soPhieuXuat} IS NULL OR c.soPhieuXuat = :#{#param.soPhieuXuat}) "
            + " AND (:#{#param.dienGiai} IS NULL OR lower(c.dienGiai) LIKE lower(concat('%',CONCAT(:#{#param.dienGiai},'%'))))"
            + " AND (:#{#param.tongTien} IS NULL OR c.tongTien = :#{#param.tongTien}) "
            + " AND (:#{#param.daTra} IS NULL OR c.daTra = :#{#param.daTra}) "
            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR lower(c.nhaThuocMaNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.nhaThuocMaNhaThuoc},'%'))))"
            + " AND (:#{#param.maLoaiXuatNhap} IS NULL OR c.maLoaiXuatNhap = :#{#param.maLoaiXuatNhap}) "
            + " AND (:#{#param.khachHangMaKhachHang} IS NULL OR c.khachHangMaKhachHang = :#{#param.khachHangMaKhachHang}) "
            + " AND (:#{#param.nhaCungCapMaNhaCungCap} IS NULL OR c.nhaCungCapMaNhaCungCap = :#{#param.nhaCungCapMaNhaCungCap}) "
            + " AND (:#{#param.bacSyMaBacSy} IS NULL OR c.bacSyMaBacSy = :#{#param.bacSyMaBacSy}) "
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
            + " AND (:#{#param.connectivityNoteID} IS NULL OR lower(c.connectivityNoteID) LIKE lower(concat('%',CONCAT(:#{#param.connectivityNoteID},'%'))))"
            + " AND (:#{#param.connectivityStatusID} IS NULL OR c.connectivityStatusID = :#{#param.connectivityStatusID}) "
            + " AND (:#{#param.connectivityResult} IS NULL OR lower(c.connectivityResult) LIKE lower(concat('%',CONCAT(:#{#param.connectivityResult},'%'))))"
            + " AND (:#{#param.orderId} IS NULL OR c.orderId = :#{#param.orderId}) "
            + " AND (:#{#param.discount} IS NULL OR c.discount = :#{#param.discount}) "
            + " AND (:#{#param.score} IS NULL OR c.score = :#{#param.score}) "
            + " AND (:#{#param.preScore} IS NULL OR c.preScore = :#{#param.preScore}) "
            + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " AND (:#{#param.targetId} IS NULL OR c.targetId = :#{#param.targetId}) "
            + " AND (:#{#param.sourceId} IS NULL OR c.sourceId = :#{#param.sourceId}) "
            + " AND (:#{#param.sourceStoreId} IS NULL OR c.sourceStoreId = :#{#param.sourceStoreId}) "
            + " AND (:#{#param.targetStoreId} IS NULL OR c.targetStoreId = :#{#param.targetStoreId}) "
            + " AND (:#{#param.partnerId} IS NULL OR c.partnerId = :#{#param.partnerId}) "
            + " AND (:#{#param.prescriptionId} IS NULL OR c.prescriptionId = :#{#param.prescriptionId}) "
            + " AND (:#{#param.invoiceCode} IS NULL OR lower(c.invoiceCode) LIKE lower(concat('%',CONCAT(:#{#param.invoiceCode},'%'))))"
            + " AND (:#{#param.invoiceNo} IS NULL OR lower(c.invoiceNo) LIKE lower(concat('%',CONCAT(:#{#param.invoiceNo},'%'))))"
            + " AND (:#{#param.referenceKey} IS NULL OR lower(c.referenceKey) LIKE lower(concat('%',CONCAT(:#{#param.referenceKey},'%'))))"
            + " AND (:#{#param.paymentScore} IS NULL OR c.paymentScore = :#{#param.paymentScore}) "
            + " AND (:#{#param.paymentScoreAmount} IS NULL OR c.paymentScoreAmount = :#{#param.paymentScoreAmount}) "
            + " AND (:#{#param.bonusPaymentId} IS NULL OR c.bonusPaymentId = :#{#param.bonusPaymentId}) "
            + " AND (:#{#param.invoiceTemplateCode} IS NULL OR lower(c.invoiceTemplateCode) LIKE lower(concat('%',CONCAT(:#{#param.invoiceTemplateCode},'%'))))"
            + " AND (:#{#param.invoiceSeries} IS NULL OR lower(c.invoiceSeries) LIKE lower(concat('%',CONCAT(:#{#param.invoiceSeries},'%'))))"
            + " AND (:#{#param.invoiceType} IS NULL OR lower(c.invoiceType) LIKE lower(concat('%',CONCAT(:#{#param.invoiceType},'%'))))"
            + " AND (:#{#param.prePaymentScore} IS NULL OR c.prePaymentScore = :#{#param.prePaymentScore}) "
            + " AND (:#{#param.synStatusId} IS NULL OR c.synStatusId = :#{#param.synStatusId}) "
            + " AND (:#{#param.transPaymentAmount} IS NULL OR c.transPaymentAmount = :#{#param.transPaymentAmount}) "
            + " AND (:#{#param.paymentTypeId} IS NULL OR c.paymentTypeId = :#{#param.paymentTypeId}) "
            + " AND (:#{#param.debtPaymentAmount} IS NULL OR c.debtPaymentAmount = :#{#param.debtPaymentAmount}) "
            + " AND (:#{#param.backPaymentAmount} IS NULL OR c.backPaymentAmount = :#{#param.backPaymentAmount}) "
            + " AND (:#{#param.linkFile} IS NULL OR lower(c.linkFile) LIKE lower(concat('%',CONCAT(:#{#param.linkFile},'%'))))"
            + " AND (:#{#param.doctorComments} IS NULL OR lower(c.doctorComments) LIKE lower(concat('%',CONCAT(:#{#param.doctorComments},'%'))))"
            + " AND (:#{#param.keyNewEInvoice} IS NULL OR lower(c.keyNewEInvoice) LIKE lower(concat('%',CONCAT(:#{#param.keyNewEInvoice},'%'))))"
            + " AND (:#{#param.keyOldEInvoice} IS NULL OR lower(c.keyOldEInvoice) LIKE lower(concat('%',CONCAT(:#{#param.keyOldEInvoice},'%'))))"
            + " AND (:#{#param.signEInvoiceStatusID} IS NULL OR c.signEInvoiceStatusID = :#{#param.signEInvoiceStatusID}) "
            + " AND (:#{#param.kHHDon} IS NULL OR lower(c.kHHDon) LIKE lower(concat('%',CONCAT(:#{#param.kHHDon},'%'))))"
            + " AND (:#{#param.kHMSHDon} IS NULL OR lower(c.kHMSHDon) LIKE lower(concat('%',CONCAT(:#{#param.kHMSHDon},'%'))))"
            + " AND (:#{#param.sHDon} IS NULL OR lower(c.sHDon) LIKE lower(concat('%',CONCAT(:#{#param.sHDon},'%'))))"
            + " AND (:#{#param.pickUpOrderId} IS NULL OR c.pickUpOrderId = :#{#param.pickUpOrderId}) "
            + " AND (:#{#param.eSampleNoteCode} IS NULL OR lower(c.eSampleNoteCode) LIKE lower(concat('%',CONCAT(:#{#param.eSampleNoteCode},'%'))))"
            + " AND (:#{#param.taxAuthorityCode} IS NULL OR lower(c.taxAuthorityCode) LIKE lower(concat('%',CONCAT(:#{#param.taxAuthorityCode},'%'))))"
            + " AND (:#{#param.eInvoiveResult} IS NULL OR lower(c.eInvoiveResult) LIKE lower(concat('%',CONCAT(:#{#param.eInvoiveResult},'%'))))"
            + " AND (:#{#param.nextPurchaseDate} IS NULL OR c.nextPurchaseDate >= :#{#param.nextPurchaseDateFrom}) "
            + " AND (:#{#param.nextPurchaseDate} IS NULL OR c.nextPurchaseDate <= :#{#param.nextPurchaseDateTo}) "
            + " AND (:#{#param.trackingIdZNS} IS NULL OR lower(c.trackingIdZNS) LIKE lower(concat('%',CONCAT(:#{#param.trackingIdZNS},'%'))))"
            + " AND (:#{#param.resultZNS} IS NULL OR lower(c.resultZNS) LIKE lower(concat('%',CONCAT(:#{#param.resultZNS},'%'))))"
            + " AND (:#{#param.idPaymentQR} IS NULL OR c.idPaymentQR = :#{#param.idPaymentQR}) "
            + " AND (:#{#param.targetManagementId} IS NULL OR c.targetManagementId = :#{#param.targetManagementId}) "
            + " ORDER BY c.id desc")
    Page<PhieuXuats> searchPage(@Param("param") PhieuXuatsReq param, Pageable pageable);

    @Query("SELECT c FROM PhieuXuats c " +
            " WHERE c.nhaThuocMaNhaThuoc = :#{#param.nhaThuocMaNhaThuoc}"
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.soPhieuXuat} IS NULL OR c.soPhieuXuat = :#{#param.soPhieuXuat}) "
            + " AND (:#{#param.dienGiai} IS NULL OR lower(c.dienGiai) LIKE lower(concat('%',CONCAT(:#{#param.dienGiai},'%'))))"
            + " AND (:#{#param.tongTien} IS NULL OR c.tongTien = :#{#param.tongTien}) "
            + " AND (:#{#param.daTra} IS NULL OR c.daTra = :#{#param.daTra}) "
            + " AND (:#{#param.maLoaiXuatNhap} IS NULL OR c.maLoaiXuatNhap = :#{#param.maLoaiXuatNhap}) "
            + " AND (:#{#param.khachHangMaKhachHang} IS NULL OR c.khachHangMaKhachHang = :#{#param.khachHangMaKhachHang}) "
            + " AND (:#{#param.nhaCungCapMaNhaCungCap} IS NULL OR c.nhaCungCapMaNhaCungCap = :#{#param.nhaCungCapMaNhaCungCap}) "
            + " AND (:#{#param.bacSyMaBacSy} IS NULL OR c.bacSyMaBacSy = :#{#param.bacSyMaBacSy}) "
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
            + " AND (:#{#param.connectivityNoteID} IS NULL OR lower(c.connectivityNoteID) LIKE lower(concat('%',CONCAT(:#{#param.connectivityNoteID},'%'))))"
            + " AND (:#{#param.connectivityStatusID} IS NULL OR c.connectivityStatusID = :#{#param.connectivityStatusID}) "
            + " AND (:#{#param.connectivityResult} IS NULL OR lower(c.connectivityResult) LIKE lower(concat('%',CONCAT(:#{#param.connectivityResult},'%'))))"
            + " AND (:#{#param.orderId} IS NULL OR c.orderId = :#{#param.orderId}) "
            + " AND (:#{#param.discount} IS NULL OR c.discount = :#{#param.discount}) "
            + " AND (:#{#param.score} IS NULL OR c.score = :#{#param.score}) "
            + " AND (:#{#param.preScore} IS NULL OR c.preScore = :#{#param.preScore}) "
            + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " AND (:#{#param.targetId} IS NULL OR c.targetId = :#{#param.targetId}) "
            + " AND (:#{#param.sourceId} IS NULL OR c.sourceId = :#{#param.sourceId}) "
            + " AND (:#{#param.sourceStoreId} IS NULL OR c.sourceStoreId = :#{#param.sourceStoreId}) "
            + " AND (:#{#param.targetStoreId} IS NULL OR c.targetStoreId = :#{#param.targetStoreId}) "
            + " AND (:#{#param.partnerId} IS NULL OR c.partnerId = :#{#param.partnerId}) "
            + " AND (:#{#param.prescriptionId} IS NULL OR c.prescriptionId = :#{#param.prescriptionId}) "
            + " AND (:#{#param.invoiceCode} IS NULL OR lower(c.invoiceCode) LIKE lower(concat('%',CONCAT(:#{#param.invoiceCode},'%'))))"
            + " AND (:#{#param.invoiceNo} IS NULL OR lower(c.invoiceNo) LIKE lower(concat('%',CONCAT(:#{#param.invoiceNo},'%'))))"
            + " AND (:#{#param.referenceKey} IS NULL OR lower(c.referenceKey) LIKE lower(concat('%',CONCAT(:#{#param.referenceKey},'%'))))"
            + " AND (:#{#param.paymentScore} IS NULL OR c.paymentScore = :#{#param.paymentScore}) "
            + " AND (:#{#param.paymentScoreAmount} IS NULL OR c.paymentScoreAmount = :#{#param.paymentScoreAmount}) "
            + " AND (:#{#param.bonusPaymentId} IS NULL OR c.bonusPaymentId = :#{#param.bonusPaymentId}) "
            + " AND (:#{#param.invoiceTemplateCode} IS NULL OR lower(c.invoiceTemplateCode) LIKE lower(concat('%',CONCAT(:#{#param.invoiceTemplateCode},'%'))))"
            + " AND (:#{#param.invoiceSeries} IS NULL OR lower(c.invoiceSeries) LIKE lower(concat('%',CONCAT(:#{#param.invoiceSeries},'%'))))"
            + " AND (:#{#param.invoiceType} IS NULL OR lower(c.invoiceType) LIKE lower(concat('%',CONCAT(:#{#param.invoiceType},'%'))))"
            + " AND (:#{#param.prePaymentScore} IS NULL OR c.prePaymentScore = :#{#param.prePaymentScore}) "
            + " AND (:#{#param.synStatusId} IS NULL OR c.synStatusId = :#{#param.synStatusId}) "
            + " AND (:#{#param.transPaymentAmount} IS NULL OR c.transPaymentAmount = :#{#param.transPaymentAmount}) "
            + " AND (:#{#param.paymentTypeId} IS NULL OR c.paymentTypeId = :#{#param.paymentTypeId}) "
            + " AND (:#{#param.debtPaymentAmount} IS NULL OR c.debtPaymentAmount = :#{#param.debtPaymentAmount}) "
            + " AND (:#{#param.backPaymentAmount} IS NULL OR c.backPaymentAmount = :#{#param.backPaymentAmount}) "
            + " AND (:#{#param.linkFile} IS NULL OR lower(c.linkFile) LIKE lower(concat('%',CONCAT(:#{#param.linkFile},'%'))))"
            + " AND (:#{#param.doctorComments} IS NULL OR lower(c.doctorComments) LIKE lower(concat('%',CONCAT(:#{#param.doctorComments},'%'))))"
            + " AND (:#{#param.keyNewEInvoice} IS NULL OR lower(c.keyNewEInvoice) LIKE lower(concat('%',CONCAT(:#{#param.keyNewEInvoice},'%'))))"
            + " AND (:#{#param.keyOldEInvoice} IS NULL OR lower(c.keyOldEInvoice) LIKE lower(concat('%',CONCAT(:#{#param.keyOldEInvoice},'%'))))"
            + " AND (:#{#param.signEInvoiceStatusID} IS NULL OR c.signEInvoiceStatusID = :#{#param.signEInvoiceStatusID}) "
            + " AND (:#{#param.pickUpOrderId} IS NULL OR c.pickUpOrderId = :#{#param.pickUpOrderId}) "
            + " AND (:#{#param.taxAuthorityCode} IS NULL OR lower(c.taxAuthorityCode) LIKE lower(concat('%',CONCAT(:#{#param.taxAuthorityCode},'%'))))"
            + " AND (:#{#param.trackingIdZNS} IS NULL OR lower(c.trackingIdZNS) LIKE lower(concat('%',CONCAT(:#{#param.trackingIdZNS},'%'))))"
            + " AND (:#{#param.resultZNS} IS NULL OR lower(c.resultZNS) LIKE lower(concat('%',CONCAT(:#{#param.resultZNS},'%'))))"
            + " AND (:#{#param.idPaymentQR} IS NULL OR c.idPaymentQR = :#{#param.idPaymentQR}) "
            + " AND (:#{#param.targetManagementId} IS NULL OR c.targetManagementId = :#{#param.targetManagementId}) "
            + " AND (:#{#param.ngayXuatTu} IS NULL OR c.ngayXuat >= :#{#param.ngayXuatTu}) "
            + " AND (:#{#param.ngayXuatDen} IS NULL OR c.ngayXuat <= :#{#param.ngayXuatDen}) "
            + " ORDER BY c.id desc")
    List<PhieuXuats> searchList(@Param("param") PhieuXuatsReq param);

    @Query("SELECT c FROM PhieuXuats c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.soPhieuXuat} IS NULL OR c.soPhieuXuat = :#{#param.soPhieuXuat}) "
            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR lower(c.nhaThuocMaNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.nhaThuocMaNhaThuoc},'%'))))"
            + " AND (:#{#param.ngayXuat} IS NULL OR c.ngayXuat = :#{#param.ngayXuat}) "
            + " AND (:#{#param.ngayXuatTu} IS NULL OR c.ngayXuat >= :#{#param.ngayXuatTu}) "
            + " AND (:#{#param.ngayXuatDen} IS NULL OR c.ngayXuat <= :#{#param.ngayXuatDen}) "
            + " ORDER BY c.id desc")
    Page<PhieuXuats> searchReportingDate(@Param("param") PhieuXuatsReq param, Pageable pageable);

    //    @Query("SELECT kh.tenKhachHang, " +
//            "       t.tenThuoc, " +
//            "       t.maThuoc, " +
//            "       dvt.tenDonViTinh, " +
//            "       dtl.soLuong, " +
//            "       dtl.giaXuat, " +
//            "       dtl.revenue, " +
//            "       SUM(ISNULL(dtl.soLuong, 0)) OVER() AS soLuong, " +
//            "       hdr " +
//            "FROM PhieuXuats hdr " +
//            "         JOIN PhieuXuatChiTiets dtl ON hdr.soPhieuXuat = dtl.phieuXuatMaPhieuXuat " +
//            "         JOIN KhachHangs kh ON kh.id = hdr.khachHangMaKhachHang " +
//            "         JOIN Thuocs t ON dtl.thuocThuocId = t.id " +
//            "         JOIN DonViTinhs dvt ON dvt.id = dtl.donViTinhMaDonViTinh " +
//            "WHERE 1=1 "
//            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR lower(hdr.nhaThuocMaNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.nhaThuocMaNhaThuoc},'%'))))"
//            + " AND (:#{#param.archivedDate} IS NULL OR hdr.archivedDate = :#{#param.archivedDate}) "
//            + " AND (:#{#param.archivedDate} IS NULL OR hdr.archivedDate = :#{#param.archivedDate}) "
//            + " AND (:#{#param.archivedDate} IS NULL OR hdr.archivedDate = :#{#param.archivedDate}) "
//            + " ORDER BY hdr.id desc")
//    Page<PhieuXuats> searchPage(@Param("param") PhieuXuatsReq param, Pageable pageable);
}