package vn.com.gsoft.report.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.com.gsoft.report.constant.RecordStatusContains;
import vn.com.gsoft.report.entity.ReportingDate.PhieuNhaps;
import vn.com.gsoft.report.entity.ReportingDate.PhieuThuChis;
import vn.com.gsoft.report.entity.ReportingDate.PhieuXuatChiTiets;
import vn.com.gsoft.report.entity.ReportingDate.PhieuXuats;
import vn.com.gsoft.report.model.dto.InOutCommingDetailsByDayModel;
import vn.com.gsoft.report.model.dto.InOutCommingDetailsByDayResponse;
import vn.com.gsoft.report.model.dto.ReportReq;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuNhapsReq;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuThuChisReq;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatChiTietsReq;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.model.system.Profile;
import vn.com.gsoft.report.repository.BaseRepository;
import vn.com.gsoft.report.repository.ReportingDate.PhieuNhapsRepository;
import vn.com.gsoft.report.repository.ReportingDate.PhieuThuChisRepository;
import vn.com.gsoft.report.repository.ReportingDate.PhieuXuatsRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl {

    @Autowired
    private PhieuXuatsRepository phieuXuatsRepository;

    @Autowired
    private PhieuNhapsRepository phieuNhapsRepository;

    @Autowired
    private PhieuThuChisRepository phieuThuChisRepository;


    public Profile getLoggedUser() throws Exception {
        try {
            return (Profile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            throw new Exception("Token invalid!");
        }
    }


    public InOutCommingDetailsByDayResponse InOutCommingDetailsByDayResponse(ReportReq req) throws Exception {

        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        InOutCommingDetailsByDayResponse result = new InOutCommingDetailsByDayResponse();
        List<InOutCommingDetailsByDayModel> lstReportModel = new ArrayList<>();
        List<PhieuXuats> phieuXuats = GetValidDeliveryNotes(req);
        List<PhieuNhaps> phieuNhaps = GetValidReceiptNotes(req);

        //bán hàng
        BigDecimal sellCashAmount = phieuXuats.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal sellTransferAmount = phieuXuats.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal sellTotalAmount = phieuXuats.stream()
                .map(PhieuXuats::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        InOutCommingDetailsByDayModel reportSellModel = new InOutCommingDetailsByDayModel();
        reportSellModel.setOrder(1);
        reportSellModel.setNoteType("Bán hàng");
        reportSellModel.setInCommingCashAmount(sellCashAmount);
        reportSellModel.setInCommingTransferAmount(sellTransferAmount);
        reportSellModel.setSellTotalAmount(sellTotalAmount);
        reportSellModel.setDescription(String.format("Tổng doanh số: {0:n0}", sellTotalAmount));
        reportSellModel.Link = "/NoteManagement/NotesListing?##noteTypeId=2";
        lstReportModel.add(reportSellModel);

        // Nhập hàng
        BigDecimal buyCashAmount = phieuNhaps.stream()
                .filter(row -> row.getPaymentTypeId() == 0)
                .map(PhieuNhaps::getDaTra)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal buyTransferAmount = phieuNhaps.stream()
                .filter(row -> row.getPaymentTypeId() == 1)
                .map(PhieuNhaps::getDaTra)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal buyTotalAmount = phieuNhaps.stream()
                .map(PhieuNhaps::getTongTien)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        InOutCommingDetailsByDayModel reportBuyModel = new InOutCommingDetailsByDayModel();
        reportBuyModel.Order = 2;
        reportBuyModel.NoteType = "Mua hàng";
        reportBuyModel.setOutCommingCashAmount(buyCashAmount);
        reportBuyModel.setOutCommingTransferAmount(buyTransferAmount);
        reportBuyModel.setBuyTotalAmount(buyTotalAmount);
        reportBuyModel.Description = String.format("Tổng phí mua hàng: {0:n0}", buyTotalAmount);
//        reportBuyModel.setLink();
        lstReportModel.add(reportBuyModel);

//        // Thu nợ khách hàng
//        List<PhieuThuChis> phieuThuChis = GetValidInOutCommingNotes(req);
//        if (filter.HasStaffIds) inCommingNote = inCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        BigDecimal inCommingCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal inCommingTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        BigDecimal totalCustomerDebt = phieuThuChis.stream()
////                .map(PhieuThuChis::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
////                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        var totalCustomerDebt = inCommingNote.Any() ? inCommingNote.Select(x => x.KhachHang_MaKhachHang).Distinct().Count() : 0;
//
//        InOutCommingDetailsByDayModel reportBuyModel = new InOutCommingDetailsByDayModel();
//        var reportInCommingModel = new InOutCommingDetailsByDayModel();
//
//        reportInCommingModel.Order = 3;
//        reportInCommingModel.NoteType = "Thu nợ khách hàng";
//        reportInCommingModel.InCommingCashAmount = (double)inCommingCashAmount;
//        reportInCommingModel.InCommingTransferAmount = (double)inCommingTransferAmount;
//        reportInCommingModel.TotalCustomerDebt = totalCustomerDebt;
//        reportInCommingModel.Description = string.Format("Đã thu nợ số khách hàng: {0}", (double)totalCustomerDebt);
//        reportInCommingModel.Link = "/InOutCommingNote?noteTypeId=1";
//        lstReportModel.add(reportBuyModel);
//
//
//        //trả lại nhà cung cấp
//        List<PhieuThuChis> outCommingNote = GetValidInOutCommingNotes(req);
//        if (filter.HasStaffIds) outCommingNote = outCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        BigDecimal outCommingCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal outCommingTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        BigDecimal totalSupplierDebt = phieuThuChis.stream()
////                .map(PhieuThuChis::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
////                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        var totalSupplierDebt = outCommingNote.Any() ? outCommingNote.Select(x => x.NhaCungCap_MaNhaCungCap).Distinct().Count() : 0;
//        var reportOutCommingModel = new InOutCommingDetailsByDayModel();
//
//        reportOutCommingModel.Order = 4;
//        reportOutCommingModel.NoteType = "Trả nợ nhà cung cấp";
//        reportOutCommingModel.OutCommingCashAmount = (double)outCommingCashAmount;
//        reportOutCommingModel.OutCommingTransferAmount = (double)outCommingTransferAmount;
//        reportOutCommingModel.TotalSupplierDebt = totalSupplierDebt;
//        reportOutCommingModel.Description = string.Format("Đã trả nợ số nhà cung cấp: {0}", (double)totalSupplierDebt);
//        reportOutCommingModel.Link = "/InOutCommingNote?noteTypeId=2";
//
//        lstReportModel.add(reportOutCommingModel);
//        //khách hàng trả lại
//        BigDecimal returnFromCusCashAmount = phieuXuats.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal returnFromCusTransferAmount = phieuXuats.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal totalCustomerReturn = phieuXuats.stream()
//                .map(PhieuXuats::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//
//        var reportReturnFromCusModel = new InOutCommingDetailsByDayModel()
//
//        reportReturnFromCusModel.Order = 5;
//        reportReturnFromCusModel.NoteType = "Khách hàng trả lại";
//        reportReturnFromCusModel.OutCommingCashAmount = (double)returnFromCusCashAmount;
//        reportReturnFromCusModel.OutCommingTransferAmount = (double)returnFromCusTransferAmount;
//        reportReturnFromCusModel.TotalCustomerReturn = totalCustomerReturn;
//        reportReturnFromCusModel.Description = string.Format("Số khách trả lại hàng: {0}", (double)totalCustomerReturn);
//        reportReturnFromCusModel.Link = "/NoteManagement/NotesListing?##noteTypeId=3";
//
//        lstReportModel.Add(reportReturnFromCusModel);
//
//        //trả lại nhà cung cấp
//        BigDecimal returnToSupCashAmount = phieuXuats.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal returnToSupTransferAmount = phieuXuats.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal totalSupplierReturn = phieuXuats.stream()
//                .map(PhieuXuats::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        var reportReturnToSuplModel = new InOutCommingDetailsByDayModel()
//
//        reportReturnToSuplModel.Order = 6,
//        reportReturnToSuplModel.NoteType = "Trả lại nhà cung cấp";
//        reportReturnToSuplModel.InCommingCashAmount = (double)returnToSupCashAmount;
//        reportReturnToSuplModel.InCommingTransferAmount = (double)returnToSupTransferAmount;
//        reportReturnToSuplModel.TotalSupplierReturn = totalSupplierReturn;
//        reportReturnToSuplModel.Description = string.Format("Trả lại hàng số nhà cung cấp: {0}", (double)totalSupplierReturn);
//        reportReturnToSuplModel.Link = "/NoteManagement/NotesListing?##noteTypeId=4";
//
//        lstReportModel.add(reportReturnToSuplModel);
//        // Thu khác
////        if (filter.HasStaffIds) otherInCommingNote = otherInCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        BigDecimal otherInCommingCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal otherInCommingTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//
//        var reportOtherInCommingModel = new InOutCommingDetailsByDayModel();
//        reportOtherInCommingModel.Order = 7;
//        reportOtherInCommingModel.NoteType = "Thu khác";
//        reportOtherInCommingModel.InCommingCashAmount = (double)otherInCommingCashAmount;
//        reportOtherInCommingModel.InCommingTransferAmount = (double)otherInCommingTransferAmount;
//        reportOtherInCommingModel.Description = "";
//        reportOtherInCommingModel.Link = "/InOutCommingNote?noteTypeId=3";
//
//        lstReportModel.add(reportOtherInCommingModel);
//        //chi khác
//        if (filter.HasStaffIds) outCommingNote = outCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        BigDecimal otherOutCommingCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal otherOutCommingTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        var reportOtherOutCommingModel = new InOutCommingDetailsByDayModel()
//        reportOtherOutCommingModel.Order = 8;
//        reportOtherOutCommingModel.NoteType = "Chi khác";
//        reportOtherOutCommingModel.OutCommingCashAmount = (double)otherOutCommingCashAmount;
//        reportOtherOutCommingModel.OutCommingTransferAmount = (double)otherOutCommingTransferAmount;
//        reportOtherOutCommingModel.Description = "";
//        reportOtherOutCommingModel.Link = "/InOutCommingNote?noteTypeId=4";
//        lstReportModel.add(reportOtherOutCommingModel);
//
//        //chi phí kinh doanh
////        var businessCostsNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.BusinessCosts });
////        if (filter.HasStaffIds) businessCostsNote = businessCostsNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        BigDecimal businessCostsCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal businessCostsTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        var reportBusinessCostsModel = new InOutCommingDetailsByDayModel();
//        reportOtherOutCommingModel.Order = 9;
//        reportOtherOutCommingModel.NoteType = "Chi phí kinh doanh";
//        reportOtherOutCommingModel.OutCommingCashAmount = (double)businessCostsCashAmount;
//        reportOtherOutCommingModel.OutCommingTransferAmount = (double)businessCostsTransferAmount;
//        reportOtherOutCommingModel.Description = "";
//        reportOtherOutCommingModel.Link = "/InOutCommingNote?noteTypeId=5";
//        lstReportModel.add(reportBusinessCostsModel);
//
//        //chi trả lại khách hàng
//        var outReturnCustomerNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.OutReturnCustomer });
//        if (filter.HasStaffIds) outReturnCustomerNote = outReturnCustomerNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//
//        BigDecimal outReturnCustomerCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal outReturnCustomerTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        BigDecimal totalOutReturnCustomer = phieuThuChis.stream()
////                .map(PhieuThuChis::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
////                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        var totalSupplierDebt = outCommingNote.Any() ? outCommingNote.Select(x => x.NhaCungCap_MaNhaCungCap).Distinct().Count() : 0;
//
//        var reportOutReturnCustomerModel = new InOutCommingDetailsByDayModel();
//
//        reportOutReturnCustomerModel.Order = 10,
//        reportOutReturnCustomerModel.NoteType = "Chi trả lại khách hàng",
//        reportOutReturnCustomerModel.OutCommingCashAmount = (double)outReturnCustomerCashAmount,
//        reportOutReturnCustomerModel.OutCommingTransferAmount = (double)outReturnCustomerTransferAmount,
//        reportOutReturnCustomerModel.Description = string.Format("Chi trả lại cho số khách hàng: {0}", (double)totalOutReturnCustomer),
//        reportOutReturnCustomerModel.Link = "/InOutCommingNote?noteTypeId=7",
//
//        lstReportModel.add(reportOutReturnCustomerModel);
//        //thu lại nhà cung cấp
//        var inReturnSupplierNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.InReturnSupplier });
//        if (filter.HasStaffIds) inReturnSupplierNote = inReturnSupplierNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        BigDecimal inReturnSupplierCashAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        BigDecimal inReturnSupplierTransferAmount = phieuThuChis.stream()
//                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
//                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
//                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
////        BigDecimal totalInReturnSupplier = phieuThuChis.stream()
////                .map(PhieuThuChis::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
////                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
//        var reportInReturnSupplierModel = new InOutCommingDetailsByDayModel();
//
//        reportInReturnSupplierModel.Order = 11,
//        reportInReturnSupplierModel.NoteType = "Thu lại nhà cung cấp",
//        reportInReturnSupplierModel.InCommingCashAmount = (double)inReturnSupplierCashAmount,
//        reportInReturnSupplierModel.InCommingTransferAmount = (double)inReturnSupplierTransferAmount,
//        reportInReturnSupplierModel.Description = string.Format("Chi trả lại cho số khách hàng: {0}", (double)totalInReturnSupplier),
//        reportInReturnSupplierModel.Link = "/InOutCommingNote?noteTypeId=6",
//
//        lstReportModel.add(reportInReturnSupplierModel);
//        //tổng
//        var summaryModel = new InOutCommingDetailsByDayModel()
//
//        summaryModel.Order = 12;
//        summaryModel.NoteType = "Tổng";
//        summaryModel.InCommingCashAmount = (double)lstReportModel.Sum(x => x.InCommingCashAmount);
//        summaryModel.InCommingTransferAmount = (double)lstReportModel.Sum(x => x.InCommingTransferAmount);
//        summaryModel.OutCommingCashAmount = (double)lstReportModel.Sum(x => x.OutCommingCashAmount);
//        summaryModel.OutCommingTransferAmount = (double)lstReportModel.Sum(x => x.OutCommingTransferAmount);
//        summaryModel.Description = "",
//
//        lstReportModel.add(summaryModel);
//        //
        var totalCount = lstReportModel.size();
//        result.InCommingTotalAmount = lstReportModel.Where(x => x.Order < 12).Sum(x => x.InCommingCashAmount + x.InCommingTransferAmount);
//        result.OutCommingTotalAmount = lstReportModel.Where(x => x.Order < 12).Sum(x => x.OutCommingCashAmount + x.OutCommingTransferAmount);
//        result.DebtTotalAmount = dNote.Any() ? (double)dNote.Sum(x => x.TongTien - (x.DaTra > x.TongTien ? x.TongTien : x.DaTra) - x.Discount - x.PaymentScoreAmount) : 0;
//        result.PagingResultModel = new PagingResultModel<InOutCommingDetailsByDayModel>(lstReportModel, totalCount);
        return result;

    }


    private List<PhieuXuats> GetValidDeliveryNotes(ReportReq req){
        PhieuXuatsReq phieuXuatsReq = new PhieuXuatsReq();
        phieuXuatsReq.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        phieuXuatsReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        phieuXuatsReq.setNgayXuatTu(req.getReportFromDate());
        phieuXuatsReq.setNgayXuatDen(req.getReportToDate());
        List<PhieuXuats> phieuXuats = phieuXuatsRepository.searchList(phieuXuatsReq);
        return phieuXuats;
    };

    private List<PhieuNhaps> GetValidReceiptNotes(ReportReq req){
        PhieuNhapsReq phieuNhapReq = new PhieuNhapsReq();
        phieuNhapReq.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        phieuNhapReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        phieuNhapReq.setNgayNhapTu(req.getReportFromDate());
        phieuNhapReq.setNgayNhapDen(req.getReportToDate());
        List<PhieuNhaps> phieuNhaps = phieuNhapsRepository.searchList(phieuNhapReq);
        return phieuNhaps;
    };

    private List<PhieuThuChis> GetValidInOutCommingNotes(ReportReq req){
        PhieuThuChisReq PhieuNhapReq = new PhieuThuChisReq();
        List<PhieuThuChis> phieuThuChis = phieuThuChisRepository.searchList(PhieuNhapReq);
        return phieuThuChis;
    }



//    public InOutCommingDetailsByDayResponse GetInOutcommingDetailsByDayData(string drugStoreCode, FilterObject filter)
//    {
//        var session = StoreHelper.GetStoreSession(drugStoreCode);
//        if (session.Settings.ViewMultipleWarehousesFromReports)
//        {
//            if (string.IsNullOrEmpty(filter.StoreCode))
//            {
//                var storeCodes = _dataFilterService.GetAllStoreByCodeParent(session.DSInfo.ParentCode).Where(x => !x.IsConnectivity).Select(x => x.MaNhaThuoc);
//                return GetMultiReportDetailsByDay(storeCodes.ToArray(), filter);
//            }
//            else
//            {
//                drugStoreCode = filter.StoreCode;
//            }
//        }
//        var result = new InOutCommingDetailsByDayResponse();
//        var lstReportModel = new List<InOutCommingDetailsByDayModel>();
//        //bán hàng
//        var dNote = _dataFilterService.GetValidDeliveryNotes(drugStoreCode, filter, new int[] { (int)ENoteType.Delivery });
//        var sellCashAmount = dNote.Where(x => x.PaymentTypeId == 0).Any() ? dNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.DaTra) : 0;
//        var sellTransferAmount = dNote.Where(x => x.PaymentTypeId == 1).Any() ? dNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.DaTra) : 0;
//        var sellTotalAmount = dNote.Any() ? dNote.Sum(x => x.TongTien) : 0;
//        var reportSellModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 1,
//            NoteType = "Bán hàng",
//            InCommingCashAmount = (double)sellCashAmount,
//            InCommingTransferAmount = (double)sellTransferAmount,
//            SellTotalAmount = (double)sellTotalAmount,
//            Description = string.Format("Tổng doanh số: {0:n0}", (double)sellTotalAmount),
//            Link = "/NoteManagement/NotesListing?##noteTypeId=2",
//        };
//        lstReportModel.Add(reportSellModel);
//        //nhập hàng
//        var rNote = _dataFilterService.GetValidReceiptNotes(drugStoreCode, filter, new int[] { (int)ENoteType.Receipt }).Where(x => x.SourceId == 0 && x.SourceStoreId == 0);
//        var buyCashAmount = rNote.Where(x => x.PaymentTypeId == 0).Any() ? rNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.DaTra) : 0;
//        var buyTransferAmount = rNote.Where(x => x.PaymentTypeId == 1).Any() ? rNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.DaTra) : 0;
//        var buyTotalAmount = rNote.Any() ? rNote.Sum(x => x.TongTien) : 0;
//        var reportBuyModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 2,
//            NoteType = "Mua hàng",
//            OutCommingCashAmount = (double)buyCashAmount,
//            OutCommingTransferAmount = (double)buyTransferAmount,
//            BuyTotalAmount = (double)buyTotalAmount,
//            Description = string.Format("Tổng phí mua hàng: {0:n0}", (double)buyTotalAmount),
//            Link = "/NoteManagement/NotesListing?##noteTypeId=1",
//        };
//        lstReportModel.Add(reportBuyModel);
//        //thu nợ khách hàng
//        var inCommingNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.Incomming });
//        if (filter.HasStaffIds) inCommingNote = inCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var inCommingCashAmount = inCommingNote.Where(x => x.PaymentTypeId == 0).Any() ? inCommingNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var inCommingTransferAmount = inCommingNote.Where(x => x.PaymentTypeId == 1).Any() ? inCommingNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var totalCustomerDebt = inCommingNote.Any() ? inCommingNote.Select(x => x.KhachHang_MaKhachHang).Distinct().Count() : 0;
//        var reportInCommingModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 3,
//            NoteType = "Thu nợ khách hàng",
//            InCommingCashAmount = (double)inCommingCashAmount,
//            InCommingTransferAmount = (double)inCommingTransferAmount,
//            TotalCustomerDebt = totalCustomerDebt,
//            Description = string.Format("Đã thu nợ số khách hàng: {0}", (double)totalCustomerDebt),
//            Link = "/InOutCommingNote?noteTypeId=1",
//        };
//        lstReportModel.Add(reportInCommingModel);
//        //trả nợ nhà cung cấp
//        var outCommingNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.Outcomming });
//        if (filter.HasStaffIds) outCommingNote = outCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var outCommingCashAmount = outCommingNote.Where(x => x.PaymentTypeId == 0).Any() ? outCommingNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var outCommingTransferAmount = outCommingNote.Where(x => x.PaymentTypeId == 1).Any() ? outCommingNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var totalSupplierDebt = outCommingNote.Any() ? outCommingNote.Select(x => x.NhaCungCap_MaNhaCungCap).Distinct().Count() : 0;
//        var reportOutCommingModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 4,
//            NoteType = "Trả nợ nhà cung cấp",
//            OutCommingCashAmount = (double)outCommingCashAmount,
//            OutCommingTransferAmount = (double)outCommingTransferAmount,
//            TotalSupplierDebt = totalSupplierDebt,
//            Description = string.Format("Đã trả nợ số nhà cung cấp: {0}", (double)totalSupplierDebt),
//            Link = "/InOutCommingNote?noteTypeId=2",
//        };
//        lstReportModel.Add(reportOutCommingModel);
//        //khách hàng trả lại
//        var returnFromCusNote = _dataFilterService.GetValidReceiptNotes(drugStoreCode, filter, new int[] { (int)ENoteType.ReturnFromCustomer });
//        var returnFromCusCashAmount = returnFromCusNote.Where(x => x.PaymentTypeId == 0).Any() ? returnFromCusNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.DaTra) : 0;
//        var returnFromCusTransferAmount = returnFromCusNote.Where(x => x.PaymentTypeId == 1).Any() ? returnFromCusNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.DaTra) : 0;
//        var totalCustomerReturn = returnFromCusNote.Any() ? returnFromCusNote.Select(x => x.KhachHang_MaKhachHang).Distinct().Count() : 0;
//        var reportReturnFromCusModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 5,
//            NoteType = "Khách hàng trả lại",
//            OutCommingCashAmount = (double)returnFromCusCashAmount,
//            OutCommingTransferAmount = (double)returnFromCusTransferAmount,
//            TotalCustomerReturn = totalCustomerReturn,
//            Description = string.Format("Số khách trả lại hàng: {0}", (double)totalCustomerReturn),
//            Link = "/NoteManagement/NotesListing?##noteTypeId=3",
//        };
//        lstReportModel.Add(reportReturnFromCusModel);
//        //trả lại nhà cung cấp
//        var returnToSupNote = _dataFilterService.GetValidDeliveryNotes(drugStoreCode, filter, new int[] { (int)ENoteType.ReturnToSupplier });
//        var returnToSupCashAmount = returnToSupNote.Where(x => x.PaymentTypeId == 0).Any() ? returnToSupNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.DaTra) : 0;
//        var returnToSupTransferAmount = returnToSupNote.Where(x => x.PaymentTypeId == 1).Any() ? returnToSupNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.DaTra) : 0;
//        var totalSupplierReturn = returnToSupNote.Any() ? returnToSupNote.Select(x => x.NhaCungCap_MaNhaCungCap).Distinct().Count() : 0;
//        var reportReturnToSuplModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 6,
//            NoteType = "Trả lại nhà cung cấp",
//            InCommingCashAmount = (double)returnToSupCashAmount,
//            InCommingTransferAmount = (double)returnToSupTransferAmount,
//            TotalSupplierReturn = totalSupplierReturn,
//            Description = string.Format("Trả lại hàng số nhà cung cấp: {0}", (double)totalSupplierReturn),
//            Link = "/NoteManagement/NotesListing?##noteTypeId=4",
//        };
//        lstReportModel.Add(reportReturnToSuplModel);
//        //thu khác
//        var otherInCommingNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.OtherIncomming });
//        if (filter.HasStaffIds) otherInCommingNote = otherInCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var otherInCommingCashAmount = otherInCommingNote.Where(x => x.PaymentTypeId == 0).Any() ? otherInCommingNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var otherInCommingTransferAmount = otherInCommingNote.Where(x => x.PaymentTypeId == 1).Any() ? otherInCommingNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var reportOtherInCommingModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 7,
//            NoteType = "Thu khác",
//            InCommingCashAmount = (double)otherInCommingCashAmount,
//            InCommingTransferAmount = (double)otherInCommingTransferAmount,
//            Description = "",
//            Link = "/InOutCommingNote?noteTypeId=3",
//        };
//        lstReportModel.Add(reportOtherInCommingModel);
//        //chi khác
//        var otherOutCommingNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.OtherOutcomming });
//        if (filter.HasStaffIds) otherOutCommingNote = otherOutCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var otherOutCommingCashAmount = otherOutCommingNote.Where(x => x.PaymentTypeId == 0).Any() ? otherOutCommingNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var otherOutCommingTransferAmount = otherOutCommingNote.Where(x => x.PaymentTypeId == 1).Any() ? otherOutCommingNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var reportOtherOutCommingModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 8,
//            NoteType = "Chi khác",
//            OutCommingCashAmount = (double)otherOutCommingCashAmount,
//            OutCommingTransferAmount = (double)otherOutCommingTransferAmount,
//            Description = "",
//            Link = "/InOutCommingNote?noteTypeId=4",
//        };
//        lstReportModel.Add(reportOtherOutCommingModel);
//        //chi phí kinh doanh
//        var businessCostsNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.BusinessCosts });
//        if (filter.HasStaffIds) businessCostsNote = businessCostsNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var businessCostsCashAmount = businessCostsNote.Where(x => x.PaymentTypeId == 0).Any() ? businessCostsNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var businessCostsTransferAmount = businessCostsNote.Where(x => x.PaymentTypeId == 1).Any() ? businessCostsNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var reportBusinessCostsModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 9,
//            NoteType = "Chi phí kinh doanh",
//            OutCommingCashAmount = (double)businessCostsCashAmount,
//            OutCommingTransferAmount = (double)businessCostsTransferAmount,
//            Description = "",
//            Link = "/InOutCommingNote?noteTypeId=5",
//        };
//        lstReportModel.Add(reportBusinessCostsModel);
//        //chi trả lại khách hàng
//        var outReturnCustomerNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.OutReturnCustomer });
//        if (filter.HasStaffIds) outReturnCustomerNote = outReturnCustomerNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var outReturnCustomerCashAmount = outReturnCustomerNote.Where(x => x.PaymentTypeId == 0).Any() ? outReturnCustomerNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var outReturnCustomerTransferAmount = outReturnCustomerNote.Where(x => x.PaymentTypeId == 1).Any() ? outReturnCustomerNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var totalOutReturnCustomer = outReturnCustomerNote.Any() ? outReturnCustomerNote.Select(x => x.KhachHang_MaKhachHang).Distinct().Count() : 0;
//        var reportOutReturnCustomerModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 10,
//            NoteType = "Chi trả lại khách hàng",
//            OutCommingCashAmount = (double)outReturnCustomerCashAmount,
//            OutCommingTransferAmount = (double)outReturnCustomerTransferAmount,
//            Description = string.Format("Chi trả lại cho số khách hàng: {0}", (double)totalOutReturnCustomer),
//            Link = "/InOutCommingNote?noteTypeId=7",
//        };
//        lstReportModel.Add(reportOutReturnCustomerModel);
//        //thu lại nhà cung cấp
//        var inReturnSupplierNote = _dataFilterService.GetValidInOutCommingNotes(drugStoreCode, filter, new int[] { (int)InOutCommingType.InReturnSupplier });
//        if (filter.HasStaffIds) inReturnSupplierNote = inReturnSupplierNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
//        var inReturnSupplierCashAmount = inReturnSupplierNote.Where(x => x.PaymentTypeId == 0).Any() ? inReturnSupplierNote.Where(x => x.PaymentTypeId == 0).Sum(x => x.Amount) : 0;
//        var inReturnSupplierTransferAmount = inReturnSupplierNote.Where(x => x.PaymentTypeId == 1).Any() ? inReturnSupplierNote.Where(x => x.PaymentTypeId == 1).Sum(x => x.Amount) : 0;
//        var totalInReturnSupplier = inReturnSupplierNote.Any() ? inReturnSupplierNote.Select(x => x.NhaCungCap_MaNhaCungCap).Distinct().Count() : 0;
//        var reportInReturnSupplierModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 11,
//            NoteType = "Thu lại nhà cung cấp",
//            InCommingCashAmount = (double)inReturnSupplierCashAmount,
//            InCommingTransferAmount = (double)inReturnSupplierTransferAmount,
//            Description = string.Format("Chi trả lại cho số khách hàng: {0}", (double)totalInReturnSupplier),
//            Link = "/InOutCommingNote?noteTypeId=6",
//        };
//        lstReportModel.Add(reportInReturnSupplierModel);
//        //tổng
//        var summaryModel = new InOutCommingDetailsByDayModel()
//        {
//            Order = 12,
//            NoteType = "Tổng",
//            InCommingCashAmount = (double)lstReportModel.Sum(x => x.InCommingCashAmount),
//            InCommingTransferAmount = (double)lstReportModel.Sum(x => x.InCommingTransferAmount),
//            OutCommingCashAmount = (double)lstReportModel.Sum(x => x.OutCommingCashAmount),
//            OutCommingTransferAmount = (double)lstReportModel.Sum(x => x.OutCommingTransferAmount),
//            Description = "",
//        };
//        lstReportModel.Add(summaryModel);
//        //
//        var totalCount = lstReportModel.Count();
//        result.InCommingTotalAmount = lstReportModel.Where(x => x.Order < 12).Sum(x => x.InCommingCashAmount + x.InCommingTransferAmount);
//        result.OutCommingTotalAmount = lstReportModel.Where(x => x.Order < 12).Sum(x => x.OutCommingCashAmount + x.OutCommingTransferAmount);
//        result.DebtTotalAmount = dNote.Any() ? (double)dNote.Sum(x => x.TongTien - (x.DaTra > x.TongTien ? x.TongTien : x.DaTra) - x.Discount - x.PaymentScoreAmount) : 0;
//        result.PagingResultModel = new PagingResultModel<InOutCommingDetailsByDayModel>(lstReportModel, totalCount);
//        return result;
    }

