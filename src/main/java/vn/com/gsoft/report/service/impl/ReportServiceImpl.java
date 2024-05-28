package vn.com.gsoft.report.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.com.gsoft.report.constant.AppConstants;
import vn.com.gsoft.report.constant.ENoteType;
import vn.com.gsoft.report.constant.InOutCommingType;
import vn.com.gsoft.report.constant.RecordStatusContains;
import vn.com.gsoft.report.entity.*;
import vn.com.gsoft.report.model.dto.*;
import vn.com.gsoft.report.model.dto.ReportingDate.*;
import vn.com.gsoft.report.entity.NhanVienNhaThuocs;
import vn.com.gsoft.report.model.system.Profile;
import vn.com.gsoft.report.repository.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl {
    @Autowired
    private DonViTinhsRepository donViTinhsRepository;
    @Autowired
    private ThuocsRepository thuocsRepository;

    @Autowired
    private PhieuXuatsRepository phieuXuatsRepository;

    @Autowired
    private PhieuXuatChiTietsRepository phieuXuatChiTietsRepository;

    @Autowired
    private PhieuNhapsRepository phieuNhapsRepository;

    @Autowired
    private PhieuNhapChiTietsRepository phieuNhapChiTietsRepository;

    @Autowired
    private PhieuThuChisRepository phieuThuChisRepository;
    
    @Autowired
    private ReceiptDrugPriceRefRepository receiptDrugPriceRefRepository;

    @Autowired
    private NhaThuocsRepository nhaThuocsRepository;

    @Autowired
    private NhanVienNhaThuocsRepository nhanVienNhaThuocsRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PhieuKiemKesRepository phieuKiemKesRepository;


    public Profile getLoggedUser() throws Exception {
        try {
            return (Profile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            throw new Exception("Token invalid!");
        }
    }


    public RevenueDrugSynthesisResponse getRevenueDrugSynthesis(ReportReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        RevenueDrugSynthesisResponse result = new RevenueDrugSynthesisResponse();
        List<PhieuXuats> phieuXuats = getValidDeliveryNotes(req);
//        List<PhieuNhaps> phieuNhaps = GetValidReceiptNotes(req);
//        List<PhieuThuChis> phieuThuChis = GetValidInOutCommingNotes(req);

        List<PhieuXuats> deliverNoteQable = phieuXuats.stream().filter(item -> Objects.equals(item.getMaLoaiXuatNhap(), ENoteType.Delivery)).collect(Collectors.toList());

        var totalCount = deliverNoteQable.size();
//        if (totalCount <= 0 || (!filter.ExportData && totalCount > AppConstants.MaxViewResultRecords))
//        {
//            if(totalCount > AppConstants.MaxViewResultRecords)
//            {
//                throw new ValidationException(ExceptionMessage.ErrorBigData.Key,
//                        string.Format(ExceptionMessage.ErrorBigData.Value, AppConstants.MaxViewResultRecords));
//            }
//            result.PagingResultModel = new PagingResultModel<RevenueDrugItem>(revenueDrugItems, totalCount);
//            return result;
//        }


        var deliveryItems = getDeliveryNoteItems(req);

//        var candidates = deliveryItems.OrderBy(i => i.NoteNumber).GroupBy(i => i.NoteId);
//        var order = 0;
//        var discountByValue = session.Settings.DeliveryNoteDiscountByValue;
//        var exportData = filter.ExportData;
//        var prodRepo = IoC.Resolve<BaseRepositoryV2<MedDbContext, Thuoc>>(); ;
//        var lstDrugId = deliveryItems.Select(x => x.DrugId).Distinct();
//        var lstDrugConsultingGoodsId = prodRepo.GetAll().Where(x => lstDrugId.Contains(x.ThuocId)
//                && x.NhaThuoc_MaNhaThuoc == session.DSInfo.ParentCode && x.HangTuVan.Value).Select(x => x.ThuocId);
//        foreach (var cand in candidates)
//        {
//            order++;
//            var idx = 0;
//            var subItems = cand.ToList();
//            var discountNote = subItems.FirstOrDefault().DiscountNote;
//            var paymentNote = subItems.FirstOrDefault().PaymentScoreAmount;
//            var explain = notes.ContainsKey(cand.Key) ? notes[cand.Key].Explain : "";
//            var hasConsultingGoods = lstDrugConsultingGoodsId.Any() ? subItems.Any(x=> lstDrugConsultingGoodsId.Contains(x.DrugId)) : false;
//            result.NoteDiscountTotal += (double)discountNote;
//            result.PaymentScoreAmountTotal += (double)paymentNote;
//            foreach (var di in subItems)
//            {
//                var revenueDrugItem = new RevenueDrugItem()
//                {
//                    DrugId = di.DrugId,
//                    Order = idx == 0 ? order.ToString() : string.Empty,
//                    ItemDateText = idx == 0 ? di.NoteDate.Value.ToString("dd/MM/yyyy HH:mm") : string.Empty,
//                    DeliveryNoteId = di.NoteId,
//                    CustomerName = idx == 0 ? di.CustomerName : string.Empty,
//                    SellerName = (idx == 0 || exportData) ? di.StaffName : string.Empty,
//                    DoctorName = (idx == 0 || exportData) ? di.DoctorName : string.Empty,
//                    NoteDiscountText = idx == 0 ? discountNote.ToString("#,##0") : string.Empty,
//                    NoteDiscount = idx == 0 ? (double)discountNote : 0,
//                    PaymentScoreAmountText = idx == 0 ? paymentNote.ToString("#,##0") : string.Empty,
//                    DrugCode = di.DrugCode,
//                    DrugName = di.DrugName,
//                    UnitName = di.UnitName,
//                    //Quantity = di.FinalRealQuantity,
//                    Quantity = di.Quantity,
//                    Price = di.Price,
//                    Discount = di.Discount,
//                    Amount = di.FinalAmount, // Effected by discount & VAT
//                    Revenue = di.Revenue,
//                    NoteNumber = idx == 0 ? di.NoteNumber.ToString() : string.Empty,
//                    VAT = (double?)di.VAT,
//                    DebtAmount = idx == 0 ? di.DebtAmount + (double)di.DebtPaymentAmount : 0,
//                    IsRefFromDrug = di.IsRefFromDrug,
//                    RefReceiptNoteId = di.RefReceiptNoteId,
//                    CustomerNoteId = di.CustomerNoteId,
//                    ReceiptRefs = di.ReceiptRefs,
//                    InPrice = di.InPrice,
//                    PaymentTypeId = di.PaymentTypeId,
//                    IsEqualDrugPrice = (int)di.DrugOutPrice == di.RetailPrice,
//                    PaymentScore = di.PaymentScoreAmount,
//                    TypeNote = di.NoteTypeID,
//                    RetailPrice = di.RetailPrice,
//                    DrugOutPrice = di.DrugOutPrice,
//                    DrugInPrice = di.DrugInPrice,
//                    HasConsultingGoods = idx == 0 && hasConsultingGoods,
//                    Explain = idx == 0 ? explain : ""
//                };
//                if (discountByValue && revenueDrugItem.Discount > AppConstants.EspDiscount)
//                {
//                    revenueDrugItem.Discount = revenueDrugItem.Price * revenueDrugItem.Discount / 100;
//                }
//                revenueDrugItems.Add(revenueDrugItem);
//                if (revenueDrugItem.PaymentTypeId == (int)OptionPayment.PaymentMoney)
//                {
//                    result.DeliveryTotalPayMoney += revenueDrugItem.Amount - revenueDrugItem.NoteDiscount - revenueDrugItem.DebtAmount -
//                            (string.IsNullOrEmpty(revenueDrugItem.PaymentScoreAmountText) ? 0 : Convert.ToDouble(revenueDrugItem.PaymentScoreAmountText));
//                    result.DebtTotalPaymentMoney += revenueDrugItem.DebtAmount;
//                }
//                else
//                {
//                    result.DeliveryTotalPayTranfer += revenueDrugItem.Amount - revenueDrugItem.NoteDiscount - revenueDrugItem.DebtAmount -
//                            (string.IsNullOrEmpty(revenueDrugItem.PaymentScoreAmountText) ? 0 : Convert.ToDouble(revenueDrugItem.PaymentScoreAmountText));
//                    result.DebtTotalPaymentTranfer += revenueDrugItem.DebtAmount;
//                }
//                result.DeliveryTotal += revenueDrugItem.Amount;
//                result.Revenue += revenueDrugItem.Revenue;
//                //result.DebtTotal += revenueDrugItem.DebtAmount;
//
//                idx++;
//            }
//        }
//        result.DebtTotal = result.DebtTotalPaymentMoney + result.DebtTotalPaymentTranfer;
//
//        var receiptNoteService = IoC.Resolve<IReceiptNoteService>();
//        var noteItemsReturnFromCustomers = receiptNoteService.GetReceiptNoteItems(drugStoreCode, filter,
//                new int[] { (int)ENoteType.ReturnFromCustomer });
//        var returnedCandidates = noteItemsReturnFromCustomers.OrderBy(i => i.NoteNumber).GroupBy(i => i.NoteId);
//        //var returnedItemIds = noteItemsReturnFromCustomers.Select(i => i.NoteItemId).Distinct().ToArray();
//        foreach (var cand in returnedCandidates)
//        {
//            order++;
//            var idx = 0;
//            var subItems = cand.ToList();
//            foreach (var di in subItems)
//            {
//                var revenueDrugItem = new RevenueDrugItem()
//                {
//                    DrugId = di.DrugId,
//                    Order = idx == 0 ? order.ToString() : string.Empty,
//                    ItemDateText = idx == 0 ? di.NoteDate.Value.ToString("dd/MM/yyyy HH:mm") : string.Empty,
//                    DeliveryNoteId = di.NoteId,
//                    CustomerName = idx == 0 ? String.Format("{0} (trả hàng)", di.CustomerName) : string.Empty,
//                    DrugCode = di.DrugCode,
//                    DrugName = di.DrugName,
//                    UnitName = di.UnitName,
//                    Quantity = di.Quantity,
//                    Price = di.Price,
//                    Discount = di.Discount,
//                    Amount = di.FinalAmount,
//                    Revenue = -(di.RetailPrice - (double)di.RefPrice) * di.RetailQuantity,
//                    NoteNumber = idx == 0 ? di.NoteNumber.ToString() : string.Empty,
//                    VAT = idx == 0 ? (double?)di.VAT : null,
//                    IsReturnFromCustomer = true,
//                    ReceiptRefs = new List<ReceiptItemReference>(),
//                    TypeNote = di.NoteTypeID
//                };
//                result.ReturnItemTotal += revenueDrugItem.Amount;
//                //result.DeliveryTotal -= revenueDrugItem.Amount;
//                result.DeliveryTotalPayMoney -= revenueDrugItem.Amount;
//                revenueDrugItems.Add(revenueDrugItem);
//                result.Revenue += revenueDrugItem.Revenue;
//                idx++;
//            }
//        }
//        if (session.IsChildDrugStore && session.Settings.SettingOwnerPrices)
//        {
//            var drugIds = deliveryItems.Select(x => x.DrugId).Distinct();
//            var inventoryService = IoC.Resolve<IInventoryService>();
//            var ownerPrices = inventoryService.GetPriceInOut(drugStoreCode, drugIds.ToArray());
//            revenueDrugItems.ForEach(x =>
//                    {
//            if (ownerPrices.ContainsKey(x.DrugId) && !x.IsReturnFromCustomer)
//            {
//                x.IsEqualDrugPrice = (int)ownerPrices[x.DrugId].OutPrice == x.RetailPrice;
//            }
//                });
//        }
//        result.TotalPaymentMoney = result.DeliveryTotalPayMoney;
//        result.TotalPaymentTranfer = result.DeliveryTotalPayTranfer;
//        result.Total = result.TotalPaymentMoney + result.TotalPaymentTranfer + result.DebtTotal;
//
//        result.PagingResultModel = new PagingResultModel<RevenueDrugItem>(revenueDrugItems, totalCount);
//
        return result;
    }

    public InOutCommingDetailsByDayResponse inOutCommingDetailsByDayResponse(ReportReq req) throws Exception {

        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        InOutCommingDetailsByDayResponse result = new InOutCommingDetailsByDayResponse();
        List<InOutCommingDetailsByDayModel> lstReportModel = new ArrayList<>();
        List<PhieuXuats> phieuXuats = getValidDeliveryNotes(req);
        List<PhieuNhaps> phieuNhaps = getValidReceiptNotes(req);
        List<PhieuThuChis> phieuThuChis = getValidInOutCommingNotes(req);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        //bán hàng
        List<PhieuXuats> banHang = phieuXuats.stream().filter(item -> Objects.equals(item.getMaLoaiXuatNhap(), ENoteType.Delivery)).collect(Collectors.toList());
        BigDecimal sellCashAmount = banHang.stream()
                .filter(row -> row.getPaymentTypeId() == 0 && row.getDaTra() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal sellTransferAmount = banHang.stream()
                .filter(row -> row.getPaymentTypeId() == 1 && row.getDaTra() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal sellTotalAmount = banHang.stream()
                .map(PhieuXuats::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        InOutCommingDetailsByDayModel reportSellModel = new InOutCommingDetailsByDayModel();
        reportSellModel.setOrder(1);
        reportSellModel.setNoteType("Bán hàng");
        reportSellModel.setInCommingCashAmount(sellCashAmount);
        reportSellModel.setInCommingTransferAmount(sellTransferAmount);
        reportSellModel.setSellTotalAmount(sellTotalAmount);
        reportSellModel.setDescription(String.format("Tổng doanh số: %s",decimalFormat.format(sellTotalAmount)));
        reportSellModel.Link = "/NoteManagement/NotesListing?##noteTypeId=2";
        lstReportModel.add(reportSellModel);

        // Nhập hàng
        List<PhieuNhaps> nhapHang = phieuNhaps.stream().filter(item -> Objects.equals(item.getLoaiXuatNhapMaLoaiXuatNhap(), ENoteType.Receipt) && item.getSourceStoreId() != null && item.getSourceId() != null && item.getSourceId() == 0L && item.getSourceStoreId() == 0L).collect(Collectors.toList());
        BigDecimal buyCashAmount = nhapHang.stream()
                .filter(row -> row.getPaymentTypeId() == 0)
                .map(PhieuNhaps::getDaTra)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal buyTransferAmount = nhapHang.stream()
                .filter(row -> row.getPaymentTypeId() == 1)
                .map(PhieuNhaps::getDaTra)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal buyTotalAmount = nhapHang.stream()
                .map(PhieuNhaps::getTongTien)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        InOutCommingDetailsByDayModel reportBuyModel = new InOutCommingDetailsByDayModel();
        reportBuyModel.setOrder(2);
        reportBuyModel.setNoteType("Mua hàng");
        reportBuyModel.setOutCommingCashAmount(buyCashAmount);
        reportBuyModel.setOutCommingTransferAmount(buyTransferAmount);
        reportBuyModel.setBuyTotalAmount(buyTotalAmount);
        reportBuyModel.setDescription(String.format("Tổng phí mua hàng:  %s",decimalFormat.format(buyTotalAmount)));
        reportBuyModel.setLink("/NoteManagement/NotesListing?##noteTypeId=1");
        lstReportModel.add(reportBuyModel);

        // Thu nợ khách hàng
        List<PhieuThuChis> thuNoKh = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.Incomming)).collect(Collectors.toList());
//        if (filter.HasStaffIds) inCommingNote = inCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal inCommingCashAmount = thuNoKh.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal inCommingTransferAmount = thuNoKh.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        long totalCustomerDebt = thuNoKh.stream()
                .filter(row -> row.getKhachHangMaKhachHang() != null)
                .map(PhieuThuChis::getKhachHangMaKhachHang)
                .distinct()
                .count();
        InOutCommingDetailsByDayModel reportInCommingModel = new InOutCommingDetailsByDayModel();
        reportInCommingModel.setOrder(3);
        reportInCommingModel.setNoteType("Thu nợ khách hàng");
        reportInCommingModel.setInCommingCashAmount(inCommingCashAmount);
        reportInCommingModel.setInCommingTransferAmount(inCommingTransferAmount);
        reportInCommingModel.setTotalCustomerDebt(Integer.parseInt(String.valueOf(totalCustomerDebt)));
        reportInCommingModel.setDescription(String.format("Đã thu nợ số khách hàng: %s", reportInCommingModel.getTotalCustomerDebt()));
        reportInCommingModel.setLink("/InOutCommingNote?noteTypeId=1");
        lstReportModel.add(reportInCommingModel);

        //trả nợ nhà cung cấp
        List<PhieuThuChis> traNoNhaCc = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.Outcomming)).collect(Collectors.toList());
//        if (filter.HasStaffIds) outCommingNote = outCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal outCommingCashAmount = traNoNhaCc.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal outCommingTransferAmount = traNoNhaCc.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        long totalSupplierDebt = traNoNhaCc.stream()
                .filter(row -> row.getKhachHangMaKhachHang() != null)
                .map(PhieuThuChis::getKhachHangMaKhachHang)
                .distinct()
                .count();
        var reportOutCommingModel = new InOutCommingDetailsByDayModel();

        reportOutCommingModel.setOrder(4);
        reportOutCommingModel.setNoteType("Trả nợ nhà cung cấp");
        reportOutCommingModel.setOutCommingCashAmount(outCommingCashAmount);
        reportOutCommingModel.setOutCommingTransferAmount(outCommingTransferAmount);
        reportOutCommingModel.setTotalSupplierDebt(Integer.parseInt(String.valueOf(totalSupplierDebt)));
        reportOutCommingModel.setDescription(String.format("Đã trả nợ số nhà cung cấp: %s", reportOutCommingModel.getTotalSupplierDebt()));
        reportOutCommingModel.setLink("/InOutCommingNote?noteTypeId=2");
        lstReportModel.add(reportOutCommingModel);

        //khách hàng trả lại
        List<PhieuNhaps> khachHangTl = phieuNhaps.stream().filter(item -> Objects.equals(item.getLoaiXuatNhapMaLoaiXuatNhap(), ENoteType.ReturnFromCustomer)).collect(Collectors.toList());
        BigDecimal returnFromCusCashAmount = khachHangTl.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuNhaps::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal returnFromCusTransferAmount = khachHangTl.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuNhaps::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        long totalCustomerReturn = khachHangTl.stream()
                .filter(row -> row.getKhachHangMaKhachHang() != null)
                .map(PhieuNhaps::getKhachHangMaKhachHang)
                .distinct()
                .count();

        var reportReturnFromCusModel = new InOutCommingDetailsByDayModel();

        reportReturnFromCusModel.setOrder(5);
        reportReturnFromCusModel.setNoteType("Khách hàng trả lại");
        reportReturnFromCusModel.setOutCommingCashAmount(returnFromCusCashAmount);
        reportReturnFromCusModel.setOutCommingTransferAmount(returnFromCusTransferAmount);
        reportReturnFromCusModel.setTotalCustomerReturn(Integer.parseInt(String.valueOf(totalCustomerReturn)));
        reportReturnFromCusModel.setDescription(String.format("Số khách trả lại hàng: %s", reportReturnFromCusModel.getTotalCustomerReturn()));
        reportReturnFromCusModel.setLink("/NoteManagement/NotesListing?##noteTypeId=3");

        lstReportModel.add(reportReturnFromCusModel);
//
        //trả lại nhà cung cấp
        List<PhieuXuats> traLaiNhaCc = phieuXuats.stream().filter(item -> Objects.equals(item.getMaLoaiXuatNhap(), ENoteType.ReturnToSupplier)).collect(Collectors.toList());
        BigDecimal returnToSupCashAmount = traLaiNhaCc.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal returnToSupTransferAmount = traLaiNhaCc.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        long totalSupplierReturn = traLaiNhaCc.stream()
                .filter(row -> row.getNhaCungCapMaNhaCungCap() != null)
                .map(PhieuXuats::getNhaCungCapMaNhaCungCap)
                .distinct()
                .count();
        var reportReturnToSuplModel = new InOutCommingDetailsByDayModel();

        reportReturnToSuplModel.setOrder(6);
        reportReturnToSuplModel.setNoteType("Trả lại nhà cung cấp");
        reportReturnToSuplModel.setInCommingCashAmount(returnToSupCashAmount);
        reportReturnToSuplModel.setInCommingTransferAmount(returnToSupTransferAmount);
        reportReturnToSuplModel.setTotalSupplierReturn(Integer.parseInt(String.valueOf(totalSupplierReturn)));
        reportReturnToSuplModel.setDescription(String.format("Trả lại hàng số nhà cung cấp: %s", reportReturnToSuplModel.getTotalSupplierReturn()));
        reportReturnToSuplModel.setLink("/NoteManagement/NotesListing?##noteTypeId=4");
        lstReportModel.add(reportReturnToSuplModel);

        // Thu khác
        List<PhieuThuChis> thuKhac = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.OtherIncomming)).collect(Collectors.toList());
////        if (filter.HasStaffIds) otherInCommingNote = otherInCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal otherInCommingCashAmount = thuKhac.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal otherInCommingTransferAmount = thuKhac.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        var reportOtherInCommingModel = new InOutCommingDetailsByDayModel();
        reportOtherInCommingModel.setOrder(7);
        reportOtherInCommingModel.setNoteType("Thu khác");
        reportOtherInCommingModel.setInCommingCashAmount(otherInCommingCashAmount);
        reportOtherInCommingModel.setInCommingTransferAmount(otherInCommingTransferAmount);
        reportOtherInCommingModel.setLink("/InOutCommingNote?noteTypeId=3");
        lstReportModel.add(reportOtherInCommingModel);

        //chi khác
        List<PhieuThuChis> chiKhac = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.OtherOutcomming)).collect(Collectors.toList());
//        if (filter.HasStaffIds) outCommingNote = outCommingNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal otherOutCommingCashAmount = chiKhac.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal otherOutCommingTransferAmount = chiKhac.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        var reportOtherOutCommingModel = new InOutCommingDetailsByDayModel();
        reportOtherOutCommingModel.setOrder(8);
        reportOtherOutCommingModel.setNoteType("Chi khác");
        reportOtherOutCommingModel.setOutCommingCashAmount(otherOutCommingCashAmount);
        reportOtherOutCommingModel.setOutCommingTransferAmount(otherOutCommingTransferAmount);
        reportOtherOutCommingModel.setLink("/InOutCommingNote?noteTypeId=4");
        lstReportModel.add(reportOtherOutCommingModel);

        //chi phí kinh doanh
        List<PhieuThuChis> chiPhiKinhDoanh = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.BusinessCosts)).collect(Collectors.toList());
//        if (filter.HasStaffIds) businessCostsNote = businessCostsNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal businessCostsCashAmount = chiPhiKinhDoanh.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal businessCostsTransferAmount = chiPhiKinhDoanh.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        var reportBusinessCostsModel = new InOutCommingDetailsByDayModel();
        reportBusinessCostsModel.setOrder(9);
        reportBusinessCostsModel.setNoteType("Chi phí kinh doanh");
        reportOtherOutCommingModel.setOutCommingCashAmount(businessCostsCashAmount);
        reportOtherOutCommingModel.setOutCommingTransferAmount(businessCostsTransferAmount);
        reportOtherOutCommingModel.setLink("/InOutCommingNote?noteTypeId=5") ;
        lstReportModel.add(reportBusinessCostsModel);

        //chi trả lại khách hàng
        List<PhieuThuChis> chiTraLaiKh = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.OutReturnCustomer)).collect(Collectors.toList());
//        if (filter.HasStaffIds) outReturnCustomerNote = outReturnCustomerNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal outReturnCustomerCashAmount = chiTraLaiKh.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal outReturnCustomerTransferAmount = chiTraLaiKh.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        long totalOutReturnCustomer = chiTraLaiKh.stream()
                .filter(row -> row.getNhaCungCapMaNhaCungCap() != null)
                .map(PhieuThuChis::getNhaCungCapMaNhaCungCap)
                .distinct()
                .count();
        var reportOutReturnCustomerModel = new InOutCommingDetailsByDayModel();
        reportOutReturnCustomerModel.setOrder(10);
        reportOutReturnCustomerModel.setNoteType("Chi trả lại khách hàng");
        reportOutReturnCustomerModel.setOutCommingCashAmount(outReturnCustomerCashAmount);
        reportOutReturnCustomerModel.setOutCommingTransferAmount(outReturnCustomerTransferAmount);
        reportOutReturnCustomerModel.setTotalOutReturnCustomer(Integer.parseInt(String.valueOf(totalOutReturnCustomer)));
        reportOutReturnCustomerModel.setDescription(String.format("Chi trả lại cho số khách hàng: %s", reportOutReturnCustomerModel.getTotalOutReturnCustomer()));
        reportOutReturnCustomerModel.setLink("/InOutCommingNote?noteTypeId=7");
        lstReportModel.add(reportOutReturnCustomerModel);

        //thu lại nhà cung cấp
        List<PhieuThuChis> inReturnSupplierNote = phieuThuChis.stream().filter(item -> Objects.equals(item.getLoaiPhieu(), InOutCommingType.InReturnSupplier)).collect(Collectors.toList());
//        if (filter.HasStaffIds) inReturnSupplierNote = inReturnSupplierNote.Where(x => filter.StaffIds.Contains(x.CreatedBy_UserId.Value));
        BigDecimal inReturnSupplierCashAmount = inReturnSupplierNote.stream()
                .filter(row -> row.getPaymentTypeId() == 0) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal inReturnSupplierTransferAmount = inReturnSupplierNote.stream()
                .filter(row -> row.getPaymentTypeId() == 1) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuThuChis::getAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        long totalInReturnSupplier = inReturnSupplierNote.stream()
                .filter(row -> row.getNhaCungCapMaNhaCungCap() != null)
                .map(PhieuThuChis::getNhaCungCapMaNhaCungCap)
                .distinct()
                .count();
        var reportInReturnSupplierModel = new InOutCommingDetailsByDayModel();
        reportInReturnSupplierModel.setOrder(11);
        reportInReturnSupplierModel.setNoteType("Thu lại nhà cung cấp");
        reportInReturnSupplierModel.setInCommingCashAmount(inReturnSupplierCashAmount);
        reportInReturnSupplierModel.setInCommingTransferAmount(inReturnSupplierTransferAmount);
        reportInReturnSupplierModel.setTotalSupplierReturn(Integer.parseInt(String.valueOf(totalInReturnSupplier)));
        reportInReturnSupplierModel.setDescription(String.format("Chi trả lại cho số khách hàng: %s", reportInReturnSupplierModel.getTotalInReturnSupplier()));
        reportInReturnSupplierModel.setLink("/InOutCommingNote?noteTypeId=6");
        lstReportModel.add(reportInReturnSupplierModel);

        //tổng
        var summaryModel = new InOutCommingDetailsByDayModel();
        summaryModel.setOrder(12);
        summaryModel.setNoteType("Tổng");
        BigDecimal inCommingCashAmountTotal = lstReportModel.stream()
                .filter(row -> row.getInCommingCashAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getInCommingCashAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal inCommingTransferAmountTotal = lstReportModel.stream()
                .filter(row -> row.getInCommingTransferAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getInCommingTransferAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal outCommingCashAmountTotal = lstReportModel.stream()
                .filter(row -> row.getOutCommingCashAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getOutCommingCashAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal outCommingTransferAmountTotal = lstReportModel.stream()
                .filter(row -> row.getOutCommingTransferAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getOutCommingTransferAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        summaryModel.setInCommingCashAmount(inCommingCashAmountTotal);
        summaryModel.setInCommingTransferAmount(inCommingTransferAmountTotal);
        summaryModel.setOutCommingCashAmount(outCommingCashAmountTotal);
        summaryModel.setOutCommingTransferAmount(outCommingTransferAmountTotal);
        lstReportModel.add(summaryModel);
        result.setInCommingTotalAmount(inCommingCashAmountTotal.add(inCommingTransferAmountTotal));
        result.setOutCommingTotalAmount(outCommingCashAmountTotal.add(outCommingTransferAmountTotal));
        result.setDebtTotalAmount(banHang.stream()
                .map(x -> x.getTongTien().subtract(x.getDaTra().max(x.getTongTien())).subtract(x.getDiscount()).subtract(x.getPaymentScoreAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        result.setListDetail(lstReportModel);
        return result;
    }


    public InOutCommingDetailsByDayResponse getReportByUser(ReportReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        InOutCommingDetailsByDayResponse result = new InOutCommingDetailsByDayResponse();
        List<InOutCommingDetailsByDayModel> lstReportModel = new ArrayList<>();
        List<PhieuXuats> phieuXuats = getValidDeliveryNotes(req);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");

        //bán hàng
        List<PhieuXuats> banHang = phieuXuats.stream().filter(item -> Objects.equals(item.getMaLoaiXuatNhap(), ENoteType.Delivery)).collect(Collectors.toList());
        BigDecimal sellCashAmount = banHang.stream()
                .filter(row -> row.getPaymentTypeId() == 0 && row.getDaTra() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal sellTransferAmount = banHang.stream()
                .filter(row -> row.getPaymentTypeId() == 1 && row.getDaTra() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(PhieuXuats::getDaTra) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal sellTotalAmount = banHang.stream()
                .map(PhieuXuats::getTongTien) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        InOutCommingDetailsByDayModel reportSellModel = new InOutCommingDetailsByDayModel();
        reportSellModel.setOrder(1);
        reportSellModel.setNoteType("Bán hàng");
        reportSellModel.setInCommingCashAmount(sellCashAmount);
        reportSellModel.setInCommingTransferAmount(sellTransferAmount);
        reportSellModel.setSellTotalAmount(sellTotalAmount);
        reportSellModel.setDescription(String.format("Tổng doanh số: %s",decimalFormat.format(sellTotalAmount)));
        reportSellModel.Link = "/NoteManagement/NotesListing?##noteTypeId=2";
        lstReportModel.add(reportSellModel);


        //tổng
        var summaryModel = new InOutCommingDetailsByDayModel();
        summaryModel.setOrder(12);
        summaryModel.setNoteType("Tổng");
        BigDecimal inCommingCashAmountTotal = lstReportModel.stream()
                .filter(row -> row.getInCommingCashAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getInCommingCashAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal inCommingTransferAmountTotal = lstReportModel.stream()
                .filter(row -> row.getInCommingTransferAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getInCommingTransferAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal outCommingCashAmountTotal = lstReportModel.stream()
                .filter(row -> row.getOutCommingCashAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getOutCommingCashAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal outCommingTransferAmountTotal = lstReportModel.stream()
                .filter(row -> row.getOutCommingTransferAmount() != null) // Ví dụ lọc dựa trên một điều kiện (cột 1 lớn hơn 2)
                .map(InOutCommingDetailsByDayModel::getOutCommingTransferAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        summaryModel.setInCommingCashAmount(inCommingCashAmountTotal);
        summaryModel.setInCommingTransferAmount(inCommingTransferAmountTotal);
        summaryModel.setOutCommingCashAmount(outCommingCashAmountTotal);
        summaryModel.setOutCommingTransferAmount(outCommingTransferAmountTotal);
        lstReportModel.add(summaryModel);
        result.setInCommingTotalAmount(inCommingCashAmountTotal.add(inCommingTransferAmountTotal));
        result.setOutCommingTotalAmount(outCommingCashAmountTotal.add(outCommingTransferAmountTotal));
        result.setDebtTotalAmount(banHang.stream()
                .map(x -> x.getTongTien().subtract(x.getDaTra().max(x.getTongTien())).subtract(x.getDiscount()).subtract(x.getPaymentScoreAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        result.setListDetail(lstReportModel);
        return result;
    }


    private List<PhieuXuats> getValidDeliveryNotes(ReportReq req){
        PhieuXuatsReq phieuXuatsReq = new PhieuXuatsReq();
        phieuXuatsReq.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        phieuXuatsReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        phieuXuatsReq.setNgayXuatTu(req.getReportFromDate());
        phieuXuatsReq.setNgayXuatDen(req.getReportToDate());
        List<PhieuXuats> phieuXuats = phieuXuatsRepository.searchList(phieuXuatsReq);
        return phieuXuats.stream().filter(row -> row.getPaymentTypeId() != null && row.getMaLoaiXuatNhap() != null).collect(Collectors.toList());
    };

    private List<PhieuNhaps> getValidReceiptNotes(ReportReq req){
        PhieuNhapsReq phieuNhapReq = new PhieuNhapsReq();
        phieuNhapReq.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        phieuNhapReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        phieuNhapReq.setNgayNhapTu(req.getReportFromDate());
        phieuNhapReq.setNgayNhapDen(req.getReportToDate());
        phieuNhapReq.setLoaiXuatNhapMaLoaiXuatNhap(req.getLoaiXuatNhapMaLoaiXuatNhap());
        List<PhieuNhaps> phieuNhaps = phieuNhapsRepository.searchList(phieuNhapReq);
        return phieuNhaps.stream().filter(row -> row.getPaymentTypeId() != null && row.getLoaiXuatNhapMaLoaiXuatNhap() != null).collect(Collectors.toList());
    };

    private List<PhieuThuChis> getValidInOutCommingNotes(ReportReq req){
        PhieuThuChisReq phieuThuChisReq = new PhieuThuChisReq();
        phieuThuChisReq.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        phieuThuChisReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        phieuThuChisReq.setFromDate(req.getReportFromDate());
        phieuThuChisReq.setToDate(req.getReportToDate());
        List<PhieuThuChis> phieuThuChis = phieuThuChisRepository.searchList(phieuThuChisReq);
        return phieuThuChis.stream().filter(row -> row.getPaymentTypeId() != null).collect(Collectors.toList());
    }

    public List<PhieuXuatChiTiets> getDeliveryNoteItems(ReportReq req){

        var items = getValidDeliveryNoteItems(req);

        if(items.size() == 0){
            return items;
        }

        List<ReceiptDrugPriceRef> validReceiptPriceRefs = receiptDrugPriceRefRepository.findAll();
        var deliveryItemIds = items.stream().map(PhieuXuatChiTiets::getId).distinct().toList();
        var drugIds = items.stream().map(PhieuXuatChiTiets::getThuocThuocId).distinct().toList();
        var priceRefs = validReceiptPriceRefs.stream().filter(row -> deliveryItemIds.contains(row.getDeliveryNoteItemId()) && drugIds.contains(row.getDrugId())).toList();

        List<PhieuNhapChiTiets> receiptItems = getValidReceiptNoteItems(req);
        List<Long> refReceiptItemIds = priceRefs.stream().map(ReceiptDrugPriceRef::getReceiptNoteItemId).distinct().toList();
        List<PhieuNhapChiTiets> refReceiptItems = receiptItems.stream().filter(item -> refReceiptItemIds.contains(item.getId())).toList();
        List<ReceiptDrugPriceRef> priceRefItems = new ArrayList<>();

        for (ReceiptDrugPriceRef p : priceRefs) {
            Optional<PhieuNhapChiTiets> first = refReceiptItems.stream().filter(r -> Objects.equals(p.getReceiptNoteItemId(), r.getId())).findFirst();
            if(first.isPresent()){
                PhieuNhapChiTiets phieuNhapChiTiets = first.get();
                p.setFinalRetailPrice(phieuNhapChiTiets.getFinalRetailPrice());
//                p.setNoteTypeId(phieuNhapChiTiets.getNhaThuocMaNhaThuoc());
                p.setNoteId(phieuNhapChiTiets.getPhieuNhapMaPhieuNhap());
                priceRefItems.add(p);
            }
        }
        //TODO
//        var missingRefIds = deliveryItemIds.Except(priceRefItems.Select(i => i.DeliveryNoteItemId).Distinct()).ToArray();
//        if (missingRefIds.Any())
//        {
//            var missingRefItems = items.Where(i => missingRefIds.Contains(i.NoteItemId)).ToList();
//            var prodIds = missingRefItems.Select(i => i.DrugId).Distinct().ToArray();
//            var rNoteService = IoC.Resolve<IReceiptNoteService>();
//            var inPrices = rNoteService.GetNearestRetailInPrices(drugStoreId, prodIds);
//            var missingPriceRefItems = missingRefItems.Select(i => new PriceReferenceItem
//            {
//                DeliveryNoteItemId = i.NoteItemId,
//                        Quantity = i.Quantity,
//                        FinalRetailPrice = (double)inPrices[i.DrugId],
//                        NoteTypeID = (int)ENoteType.InitialInventory,
//                        NoteId = 0,
//                        ReceiptNoteItemId = 0
//            }).ToList();
//            priceRefItems.AddRange(missingPriceRefItems);
//            Log.DebugFormat("GetDeliveryNoteItems- DS: {0}, Number missing price ref items: {1}, DeliveryItemIds: {2}", drugStoreId, missingPriceRefItems.Count, string.Join(",", missingRefIds));
//        }




//        var priceRefsDict = priceRefItems.GroupBy(i => i.DeliveryNoteItemId).ToDictionary(i => i.Key,
//                i => new
//        {
//            TotalAmount = i.Sum(it => it.Quantity * it.FinalRetailPrice),
//            IsRefFromDrug = i.Last().NoteTypeID == (int)ENoteType.InitialInventory,
//                    ReceiptNoteId = i.Last().NoteId,
//                    ReceiptRefs = i.Select(r => new ReceiptItemReference()
//                    {
//                        ReceiptNoteId = r.NoteId,
//                        IsRefFromDrug = r.NoteTypeID == (int)ENoteType.InitialInventory,
//                        ReceiptNoteItemId = r.ReceiptNoteItemId,
//                        InPrice = r.FinalRetailPrice,
//                        Quantity = r.Quantity,
//                    }).ToList()
//        });


        for (PhieuXuatChiTiets item : items) {
//            if (priceRefsDict.ContainsKey(item.NoteItemId))
//            {
//                var priceRef = priceRefsDict[item.NoteItemId];
//                //item.ReceiptRefs = priceRef.ReceiptRefs;
//                item.FinalRetailAmount = item.FinalPrice * item.Quantity;
//                //item.Revenue = item.FinalRetailAmount < MedConstants.EspAmount ? 0: item.FinalRetailAmount - priceRef.TotalAmount;
//                item.Revenue = item.FinalRetailAmount - priceRef.TotalAmount;
//                item.IsRefFromDrug = priceRef.IsRefFromDrug;
//                item.RefReceiptNoteId = priceRef.ReceiptNoteId;
//                item.ReceiptRefs = priceRef.ReceiptRefs;
//                item.InPrice = priceRef.ReceiptRefs.First().InPrice;
//            }
//            if(item.RefReceiptNoteId == 0 && item.IsUnitName)
//            {
//                item.ReceiptRefs.ForEach(x =>
//                        {
//                                x.Quantity = x.Quantity * (double)item.DrugUnitFactors;
//                    });
//                item.Revenue = item.Price - (item.DrugInPrice * item.DrugUnitFactors);
//            }
        }
        return items;
    }

    private List <PhieuXuatChiTiets> getValidDeliveryNoteItems(ReportReq req){
        PhieuXuatChiTietsReq reqPx = new PhieuXuatChiTietsReq();
        reqPx.setFromDateNgayXuat(req.getReportFromDate());
        reqPx.setFromDateNgayXuat(req.getReportToDate());
        reqPx.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        reqPx.setRecordStatusId(RecordStatusContains.ACTIVE);
        reqPx.setListIdPhieuXuat(req.getListIdPhieuXuat());
        List<PhieuXuatChiTiets> phieuXuatChiTiets = phieuXuatChiTietsRepository.searchListCustom(reqPx);
        return phieuXuatChiTiets;
    }


    private List<PhieuNhapChiTiets> getValidReceiptNoteItems(ReportReq req){
        PhieuNhapChiTietsReq reqPx = new PhieuNhapChiTietsReq();
//        reqPx.setFromDateNgayXuat(req.getReportFromDate());
//        reqPx.setFromDateNgayXuat(req.getReportToDate());
        reqPx.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        reqPx.setRecordStatusId(RecordStatusContains.ACTIVE);
//        reqPx.setListIdPhieuXuat(req.getListIdPhieuXuat());
        List<PhieuNhapChiTiets> phieuNhapChiTiets = phieuNhapChiTietsRepository.searchList(reqPx);
        return phieuNhapChiTiets;
    }




//    public RevenueDrugSynthesisResponse GetRevenueDrugSynthesis(string drugStoreCode, FilterObject filter)
//    {
//        var revenueDrugItems = new List<RevenueDrugItem>();
//        var result = new RevenueDrugSynthesisResponse()
//        {
//            Total = 0.0,
//            Revenue = 0.0,
//            DeliveryTotal = 0.0,
//            DebtTotal = 0.0,
//            DebtTotalPaymentMoney = 0.0,
//            DebtTotalPaymentTranfer = 0.0,
//            DeliveryTotalPayMoney = 0.0,
//            DeliveryTotalPayTranfer = 0.0,
//            ReturnItemTotal = 0.0
//        };
//        var session = StoreHelper.GetStoreSession(drugStoreCode);
//        if (session.Settings.ViewMultipleWarehousesFromReports)
//        {
//            if (string.IsNullOrEmpty(filter.StoreCode))
//            {
//                var storeCodes = _dataFilterService.GetAllStoreByCodeParent(session.DSInfo.ParentCode).Where(x => !x.IsConnectivity).Select(x => x.MaNhaThuoc);
//                return GetMultipleRevenueDrugSynthesis(storeCodes.ToArray(), filter);
//            }
//            else
//            {
//                drugStoreCode = filter.StoreCode;
//            }
//        }
//        TryToGenerateReportData(drugStoreCode);
//        var deliveryService = IoC.Resolve<IDeliveryNoteService>();
//        var noteTypeIds = new int[] { (int)ENoteType.Delivery };
//        var rcStatusIds = new int[] { (int)RecordStatus.Activated, (int)RecordStatus.Archived };
//        if (filter == null)
//        {
//            filter = new FilterObject();
//        }
//        filter.FillCustomerInfos = true;
//        var deliverNoteQable = _dataFilterService.GetValidDeliveryNotes(drugStoreCode, filter, noteTypeIds, rcStatusIds)
//                .Select(i => new { NoteId = i.MaPhieuXuat, NoteDate = i.NgayXuat, i.Discount, i.PaymentScoreAmount, Explain = i.DienGiai });
//        var totalCount = deliverNoteQable.Count();
//        if (totalCount <= 0 || (!filter.ExportData && totalCount > AppConstants.MaxViewResultRecords))
//        {
//            if(totalCount > AppConstants.MaxViewResultRecords)
//            {
//                throw new ValidationException(ExceptionMessage.ErrorBigData.Key,
//                        string.Format(ExceptionMessage.ErrorBigData.Value, AppConstants.MaxViewResultRecords));
//            }
//            result.PagingResultModel = new PagingResultModel<RevenueDrugItem>(revenueDrugItems, totalCount);
//            return result;
//        }
//        var notes = deliverNoteQable.OrderBy(i => i.NoteDate).ToPagedQueryable(filter.PageIndex, filter.PageSize, totalCount)
//            .ToDictionary(i => i.NoteId, i => i);
//        filter.NoteIds = notes.Keys.ToArray();
//
//        var deliveryItems = deliveryService.GetDeliveryNoteItems(drugStoreCode, filter, noteTypeIds, rcStatusIds);
//
//        var candidates = deliveryItems.OrderBy(i => i.NoteNumber).GroupBy(i => i.NoteId);
//        var order = 0;
//        var discountByValue = session.Settings.DeliveryNoteDiscountByValue;
//        var exportData = filter.ExportData;
//        var prodRepo = IoC.Resolve<BaseRepositoryV2<MedDbContext, Thuoc>>(); ;
//        var lstDrugId = deliveryItems.Select(x => x.DrugId).Distinct();
//        var lstDrugConsultingGoodsId = prodRepo.GetAll().Where(x => lstDrugId.Contains(x.ThuocId)
//                && x.NhaThuoc_MaNhaThuoc == session.DSInfo.ParentCode && x.HangTuVan.Value).Select(x => x.ThuocId);
//        foreach (var cand in candidates)
//        {
//            order++;
//            var idx = 0;
//            var subItems = cand.ToList();
//            var discountNote = subItems.FirstOrDefault().DiscountNote;
//            var paymentNote = subItems.FirstOrDefault().PaymentScoreAmount;
//            var explain = notes.ContainsKey(cand.Key) ? notes[cand.Key].Explain : "";
//            var hasConsultingGoods = lstDrugConsultingGoodsId.Any() ? subItems.Any(x=> lstDrugConsultingGoodsId.Contains(x.DrugId)) : false;
//            result.NoteDiscountTotal += (double)discountNote;
//            result.PaymentScoreAmountTotal += (double)paymentNote;
//            foreach (var di in subItems)
//            {
//                var revenueDrugItem = new RevenueDrugItem()
//                {
//                    DrugId = di.DrugId,
//                    Order = idx == 0 ? order.ToString() : string.Empty,
//                    ItemDateText = idx == 0 ? di.NoteDate.Value.ToString("dd/MM/yyyy HH:mm") : string.Empty,
//                    DeliveryNoteId = di.NoteId,
//                    CustomerName = idx == 0 ? di.CustomerName : string.Empty,
//                    SellerName = (idx == 0 || exportData) ? di.StaffName : string.Empty,
//                    DoctorName = (idx == 0 || exportData) ? di.DoctorName : string.Empty,
//                    NoteDiscountText = idx == 0 ? discountNote.ToString("#,##0") : string.Empty,
//                    NoteDiscount = idx == 0 ? (double)discountNote : 0,
//                    PaymentScoreAmountText = idx == 0 ? paymentNote.ToString("#,##0") : string.Empty,
//                    DrugCode = di.DrugCode,
//                    DrugName = di.DrugName,
//                    UnitName = di.UnitName,
//                    //Quantity = di.FinalRealQuantity,
//                    Quantity = di.Quantity,
//                    Price = di.Price,
//                    Discount = di.Discount,
//                    Amount = di.FinalAmount, // Effected by discount & VAT
//                    Revenue = di.Revenue,
//                    NoteNumber = idx == 0 ? di.NoteNumber.ToString() : string.Empty,
//                    VAT = (double?)di.VAT,
//                    DebtAmount = idx == 0 ? di.DebtAmount + (double)di.DebtPaymentAmount : 0,
//                    IsRefFromDrug = di.IsRefFromDrug,
//                    RefReceiptNoteId = di.RefReceiptNoteId,
//                    CustomerNoteId = di.CustomerNoteId,
//                    ReceiptRefs = di.ReceiptRefs,
//                    InPrice = di.InPrice,
//                    PaymentTypeId = di.PaymentTypeId,
//                    IsEqualDrugPrice = (int)di.DrugOutPrice == di.RetailPrice,
//                    PaymentScore = di.PaymentScoreAmount,
//                    TypeNote = di.NoteTypeID,
//                    RetailPrice = di.RetailPrice,
//                    DrugOutPrice = di.DrugOutPrice,
//                    DrugInPrice = di.DrugInPrice,
//                    HasConsultingGoods = idx == 0 && hasConsultingGoods,
//                    Explain = idx == 0 ? explain : ""
//                };
//                if (discountByValue && revenueDrugItem.Discount > AppConstants.EspDiscount)
//                {
//                    revenueDrugItem.Discount = revenueDrugItem.Price * revenueDrugItem.Discount / 100;
//                }
//                revenueDrugItems.Add(revenueDrugItem);
//                if (revenueDrugItem.PaymentTypeId == (int)OptionPayment.PaymentMoney)
//                {
//                    result.DeliveryTotalPayMoney += revenueDrugItem.Amount - revenueDrugItem.NoteDiscount - revenueDrugItem.DebtAmount -
//                            (string.IsNullOrEmpty(revenueDrugItem.PaymentScoreAmountText) ? 0 : Convert.ToDouble(revenueDrugItem.PaymentScoreAmountText));
//                    result.DebtTotalPaymentMoney += revenueDrugItem.DebtAmount;
//                }
//                else
//                {
//                    result.DeliveryTotalPayTranfer += revenueDrugItem.Amount - revenueDrugItem.NoteDiscount - revenueDrugItem.DebtAmount -
//                            (string.IsNullOrEmpty(revenueDrugItem.PaymentScoreAmountText) ? 0 : Convert.ToDouble(revenueDrugItem.PaymentScoreAmountText));
//                    result.DebtTotalPaymentTranfer += revenueDrugItem.DebtAmount;
//                }
//                result.DeliveryTotal += revenueDrugItem.Amount;
//                result.Revenue += revenueDrugItem.Revenue;
//                //result.DebtTotal += revenueDrugItem.DebtAmount;
//
//                idx++;
//            }
//        }
//        result.DebtTotal = result.DebtTotalPaymentMoney + result.DebtTotalPaymentTranfer;
//
//        var receiptNoteService = IoC.Resolve<IReceiptNoteService>();
//        var noteItemsReturnFromCustomers = receiptNoteService.GetReceiptNoteItems(drugStoreCode, filter,
//                new int[] { (int)ENoteType.ReturnFromCustomer });
//        var returnedCandidates = noteItemsReturnFromCustomers.OrderBy(i => i.NoteNumber).GroupBy(i => i.NoteId);
//        //var returnedItemIds = noteItemsReturnFromCustomers.Select(i => i.NoteItemId).Distinct().ToArray();
//        foreach (var cand in returnedCandidates)
//        {
//            order++;
//            var idx = 0;
//            var subItems = cand.ToList();
//            foreach (var di in subItems)
//            {
//                var revenueDrugItem = new RevenueDrugItem()
//                {
//                    DrugId = di.DrugId,
//                    Order = idx == 0 ? order.ToString() : string.Empty,
//                    ItemDateText = idx == 0 ? di.NoteDate.Value.ToString("dd/MM/yyyy HH:mm") : string.Empty,
//                    DeliveryNoteId = di.NoteId,
//                    CustomerName = idx == 0 ? String.Format("{0} (trả hàng)", di.CustomerName) : string.Empty,
//                    DrugCode = di.DrugCode,
//                    DrugName = di.DrugName,
//                    UnitName = di.UnitName,
//                    Quantity = di.Quantity,
//                    Price = di.Price,
//                    Discount = di.Discount,
//                    Amount = di.FinalAmount,
//                    Revenue = -(di.RetailPrice - (double)di.RefPrice) * di.RetailQuantity,
//                    NoteNumber = idx == 0 ? di.NoteNumber.ToString() : string.Empty,
//                    VAT = idx == 0 ? (double?)di.VAT : null,
//                    IsReturnFromCustomer = true,
//                    ReceiptRefs = new List<ReceiptItemReference>(),
//                    TypeNote = di.NoteTypeID
//                };
//                result.ReturnItemTotal += revenueDrugItem.Amount;
//                //result.DeliveryTotal -= revenueDrugItem.Amount;
//                result.DeliveryTotalPayMoney -= revenueDrugItem.Amount;
//                revenueDrugItems.Add(revenueDrugItem);
//                result.Revenue += revenueDrugItem.Revenue;
//                idx++;
//            }
//        }
//        if (session.IsChildDrugStore && session.Settings.SettingOwnerPrices)
//        {
//            var drugIds = deliveryItems.Select(x => x.DrugId).Distinct();
//            var inventoryService = IoC.Resolve<IInventoryService>();
//            var ownerPrices = inventoryService.GetPriceInOut(drugStoreCode, drugIds.ToArray());
//            revenueDrugItems.ForEach(x =>
//                    {
//            if (ownerPrices.ContainsKey(x.DrugId) && !x.IsReturnFromCustomer)
//            {
//                x.IsEqualDrugPrice = (int)ownerPrices[x.DrugId].OutPrice == x.RetailPrice;
//            }
//                });
//        }
//        result.TotalPaymentMoney = result.DeliveryTotalPayMoney;
//        result.TotalPaymentTranfer = result.DeliveryTotalPayTranfer;
//        result.Total = result.TotalPaymentMoney + result.TotalPaymentTranfer + result.DebtTotal;
//
//        result.PagingResultModel = new PagingResultModel<RevenueDrugItem>(revenueDrugItems, totalCount);
//
//        return result;
//    }



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


//    public ReportByResponse getReportByData(ReportReq req){
//        if (Objects.equals(req.getReportByTypeId(), ReportByType.ByStaff)){
//            return getReportByStaff(req);
//        }
//        if (Objects.equals(req.getReportByTypeId(), ReportByType.ByGoodsByStaff) || Objects.equals(req.getReportByTypeId(), ReportByType.ByGoodsByDoctor)){
//            return getReportByGoods(req);
//        }
//        if (Objects.equals(req.getReportByTypeId(), ReportByType.BySupplyer)){
//            return GetReportBySupplier(req);
//        }
//        if (Objects.equals(req.getReportByTypeId(), ReportByType.ByStaffDate)){
////            return GetDataByStaff(drugStoreCode, filter);
//        }
//        if (Objects.equals(req.getReportByTypeId(), ReportByType.ByCustomer)){
////            return GetReportByDataCustomer(drugStoreCode, filter);
//        }
//        if (Objects.equals(req.getReportByTypeId(), ReportByType.ByGoodDoctor)){
////            return GetReportDoctorByGoods(drugStoreCode, filter);
//        }
//        return null;
//    }
//
//    private ReportByResponse getReportByStaff(ReportReq req) {
////        ReportByResponse result = new ReportByResponse();
////        req.setLoaiXuatNhapMaLoaiXuatNhap(ENoteType.ReturnFromCustomer);
////        List<PhieuNhaps> receiptNotesFromCustomerQable = getValidReceiptNotes(req).stream().filter(row -> row.getKhachHangMaKhachHang() != null && row.getKhachHangMaKhachHang() > 0L).toList();
////        List<PhieuNhapChiTiets> deliveryItemCands = getValidReceiptNoteItems(req);
////        List<PhieuNhapChiTiets> deliveryItems = deliveryItemCands;
////        List<Long> noteIds = deliveryItems.stream().map(PhieuNhapChiTiets::getPhieuNhapMaPhieuNhap).toList();
////        if (!req.filterByAll && req.hasStaffIds)
////        {
////            Map<Long, Double> revenReturnCus = getValidReceiptNoteItems(req).stream().collect(
////                    Collectors.groupingBy(PhieuNhapChiTiets::getPhieuNhapMaPhieuNhap,
////                            Collectors.summingDouble(item -> item.getRetailQuantity().multiply(item.getRetailPrice().subtract(item.getRefPrice())).doubleValue())));
//////            deliveryItems.addAll(noteItemsReturnFromCustomers.ToArray());
//////            deliveryItemsGroups = deliveryItems.GroupBy(i => new ReportBaseNoteItem()
//////            { OwnerId = i.StaffId.Value, NoteId = i.NoteId }).DistinctBy(i => i.Key).ToList();
////        }
////        else
////        {
////            Map<Long, Double> revenReturnCus = getValidReceiptNoteItems(req).stream().collect(
////                    Collectors.groupingBy(PhieuNhapChiTiets::getPhieuNhapMaPhieuNhap,
////                            Collectors.summingDouble(item -> item.getRetailQuantity().multiply(item.getRetailPrice().subtract(item.getRefPrice())).doubleValue())));
////            returnedNotesFromCustomers = receiptNotesFromCustomerQable.Select(i => new ReportDeliveryItem()
////            {
////                //PaidAmount = (double)(i.DaTra + i.DebtPaymentAmount),
////                PaidAmount = (double)i.DaTra,
////                TotalAmount = (double)i.TongTien,
////                OwnerId = i.CreatedBy_UserId.Value,
////                ReturnedItem = true
////            }).ToList();
////            deliveryItemsGroupsByOwners = deliveryItems.GroupBy(i => i.StaffId).ToList();
////            deliveryNotes = deliveryNotesQable.Where(i => noteIds.Contains(i.MaPhieuXuat))
////                            .Select(i => new ReportDeliveryItem()
////        {
////            //PaidAmount = (double)(i.DaTra + i.DebtPaymentAmount),
////            PaidAmount = i.DaTra == i.TongTien && (i.Discount > 0 || i.PaymentScore > 0) ? (double)(i.DaTra - i.Discount - i.PaymentScoreAmount) : (double)i.DaTra,
////            TotalAmount = (double)i.TongTien,
////            Discount = (double)i.Discount,
////            PaymentScore = (double)i.PaymentScoreAmount,
////            OwnerId = i.CreatedBy_UserId.Value,
////            Payment = i.PaymentTypeId == 0
////                ? i.DaTra == i.TongTien && (i.Discount > 0 || i.PaymentScore > 0) ? (double)(i.DaTra - i.Discount - i.PaymentScoreAmount)
////                : (double)i.DaTra : 0,
////            PaymentCard = i.PaymentTypeId == 1
////                ? i.DaTra == i.TongTien && (i.Discount > 0 || i.PaymentScore > 0) ? (double)(i.DaTra - i.Discount - i.PaymentScoreAmount)
////                : (double)i.DaTra : 0
////        }).ToList();
////        }
//        return null;
//    }


//    private ReportByResponse getReportByGoods(ReportReq req)
//    {
////
////        ReportByResponse result = new ReportByResponse();
////        List<ReportByBaseItem> reportByItems = new ArrayList<>();
////        var totalCount = 0;
////
////        List <PhieuXuatChiTiets> deliveryCands = getValidDeliveryItems(req);
////
////        var deliveryDrugs = deliveryCands.stream().map(PhieuXuatChiTiets::getThuocThuocId).distinct().toList();
////        totalCount = deliveryDrugs.size();
////        if (totalCount < 1)
////        {
////            return result;
////        }
////
////        var drugIds = deliveryDrugs.OrderBy(i => i)
////                .ToPagedQueryable(filter.PageIndex, filter.PageSize, totalCount).ToList();
////        var drugUnitRep = IoC.Resolve<BaseRepositoryV2<MedDbContext, DonViTinh>>();
////        var drugQueryable = (from dr in _dataFilterService.GetValidProducts(drugStoreCode, null, false)
////        join u in drugUnitRep.GetAll() on dr.DonViXuatLe_MaDonViTinh equals u.MaDonViTinh
////        where drugIds.Contains(dr.ThuocId)
////        select new
////        {
////            DrugId = dr.ThuocId,
////                    DrugCode = dr.MaThuoc,
////                    DrugName = dr.TenThuoc,
////                    DrugRetailUnitName = u.TenDonViTinh,
////                    DrugGroupId = dr.NhomThuoc_MaNhomThuoc,
////                    dr.Discount,
////                    dr.DiscountByRevenue,
////                    InPrice = dr.GiaNhap,
////                    OutPrice = dr.GiaBanLe
////        });
////
////        var drugs = drugQueryable.ToDictionary(i => i.DrugId, i => i);
////        filter.DrugIds = drugIds.Select(i => i.Value).ToArray();
////        var deliveryService = IoC.Resolve<IDeliveryNoteService>();
////
////        var deliveryItems = deliveryService.GetDeliveryNoteItems(drugStoreCode, filter, deliveryStatuses);
////        var deliveryItemsByDrugs = deliveryItems.GroupBy(i => i.DrugId).ToDictionary(i => i.Key, i => i.ToList());
////
////        var receiptCands = _dataFilterService.GetValidReceiptItemsByCustomer(drugStoreCode, filter, receiptSatuses);
////        var order = filter.PageIndex * filter.PageSize;
////        if (filter.HasStaffIds)
////        {
////            var deliveryDrugIds = deliveryItemsByDrugs.Select(i => i.Key).ToList();
////            drugs = drugs.Where(i => deliveryDrugIds.Contains(i.Key)).ToDictionary(i => i.Key, i => i.Value);
////            receiptCands = receiptCands.Where(x => filter.StaffIds.Contains(x.CreatedById));
////        }
////        if(filter.HasCustomerIds)
////        {
////            receiptCands = receiptCands.Where(x => filter.CustomerIds.Contains(x.CusId));
////        }
////        //tính khách trả lại
////        var idDrugsReturn = new Dictionary<int, ItemCusReturn>();
////        if (receiptCands.Any())
////        {
////            idDrugsReturn = receiptCands.GroupBy(i => i.DrugId, (k, c) => new
////            {
////                Code = k,
////                        Item = new ItemCusReturn()
////                        {
////                            Amount = c.Sum(x => x.RetailQuantity),
////                            PriceIn = c.Sum(x => (double)x.RefPrice * x.RetailQuantity),
////                            PriceOut = c.Sum(x => x.RetailOutPrice * x.RetailQuantity),
////                            StaffId = c.Select(x => x.CreatedById).FirstOrDefault(),
////                            ItemReturn = c.Select(b => new CusIdReturn()
////                        {
////                            CusId = b.CusId,
////                                    RetailQuantity = b.RetailQuantity,
////                                    ImportDate = b.ImportDate,
////                        }).ToList(),
////                        },
////            }).ToDictionary(i => i.Code, i => i.Item);
////        }
////        drugs.ForEach(i =>
////                {
////                        var drug = drugs[i.Key];
////        var rptItem = new ReportByGoodsItem()
////        {
////            Order = ++order,
////            ItemId = drug.DrugId,
////            ItemName = drug.DrugName,
////            ItemNumber = drug.DrugCode,
////            DrugUnit = drug.DrugRetailUnitName,
////            Discount = (double)drug.Discount,
////            InPrice = (double)drug.InPrice,
////            OutPrice = (double)drug.OutPrice
////        };
////        if (deliveryItemsByDrugs.ContainsKey(i.Key))
////        {
////            var items = deliveryItemsByDrugs[i.Key];
////            var tunrsCusBuy = items.Select(ii => ii.CustomerId.Value).Distinct();
////            rptItem.TurnsCusBuy = tunrsCusBuy.Count();
////            rptItem.DeliveryQuantity = items.Sum(ii => ii.RetailQuantity);
////
////            rptItem.Quantity = items.Sum(ii => ii.FinalRetailQuantity);
////            rptItem.TotalAmount = items.Sum(ii => ii.FinalRetailAmount);
////            rptItem.PaidAmount = items.Sum(ii => ii.FinalRetailAmount);
////            rptItem.Revenue = items.Sum(ii => ii.Revenue);
////            rptItem.TurnsBuy = items.Count();
////            rptItem.DiscountCustomerAmount = items.Sum(ii => (ii.Price * (ii.Discount / 100)) * ii.Quantity);
////
////            if (idDrugsReturn.Any() && idDrugsReturn.ContainsKey(i.Key))
////            {
////                rptItem.ReturnedQuantity = idDrugsReturn[i.Key].Amount;
////                rptItem.PaidAmount = rptItem.TotalAmount - (idDrugsReturn[i.Key].PriceOut);
////                rptItem.Revenue = rptItem.Revenue - ((idDrugsReturn[i.Key].PriceOut - idDrugsReturn[i.Key].PriceIn));
////                rptItem.ReturnedAmount = idDrugsReturn[i.Key].PriceOut;
////                var itemCusReturn = idDrugsReturn[i.Key].ItemReturn.OrderByDescending(x => x.ImportDate);
////                rptItem.TurnsCusReturn = itemCusReturn.Select(ii => ii.CusId).Distinct().Count();
////                foreach (var itemReturn in itemCusReturn)
////                {
////                    var itemCusBuy = items.OrderByDescending(x => x.NoteDate)
////                            .Select((value, index) => new { Value = value, Index = index })
////                            .FirstOrDefault(x => x.Value.CustomerId == itemReturn.CusId &&
////                        !x.Value.Flag && x.Value.NoteDate <= itemReturn.ImportDate
////                        && x.Value.RetailQuantity == itemReturn.RetailQuantity);
////                    if (itemCusBuy != null)
////                    {
////                        rptItem.TurnsBuy -= 1;
////                        items[itemCusBuy.Index].Flag = true;
////                    }
////                }
////            }
////            if (rptItem.ReturnedQuantity > AppConstants.EspQuantity)
////            {
////                rptItem.ReturnedItem = true;
////                rptItem.Quantity -= rptItem.ReturnedQuantity;
////            }
////        }
////
////        rptItem.DiscountAmount = Math.Round((drug.DiscountByRevenue ? rptItem.Revenue : rptItem.PaidAmount) * (rptItem.Discount / 100), 2);
////        reportByItems.Add(rptItem);
////            });
////
////        result.TotalAmount = reportByItems.Sum(i => i.TotalAmount);
////        result.TotalRevenue = reportByItems.Sum(i => i.Revenue);
////        result.TotalDiscount = reportByItems.Sum(i => i.DiscountAmount);
////        result.TotalReturnAmount = reportByItems.Sum(i => i.ReturnedAmount);
////        result.TotalPayment = reportByItems.Sum(i => i.PaidAmount);
////        result.TotalCustomerDiscount = reportByItems.Sum(i => i.DiscountCustomerAmount);
////
////        result.PagingResultModel = new PagingResultModel<ReportByBaseItem>(reportByItems, totalCount);
//
//        return null;
//    }


    private List <PhieuXuatChiTiets> getValidDeliveryItems(ReportReq req){
        PhieuXuatChiTietsReq reqPx = new PhieuXuatChiTietsReq();
        reqPx.setFromDateNgayXuat(req.getReportFromDate());
        reqPx.setFromDateNgayXuat(req.getReportToDate());
        reqPx.setNhaThuocMaNhaThuoc(req.getDrugStoreId());
        reqPx.setRecordStatusId(RecordStatusContains.ACTIVE);
        reqPx.setListIdPhieuXuat(req.getListIdPhieuXuat());
        List<PhieuXuatChiTiets> phieuXuatChiTiets = phieuXuatChiTietsRepository.searchListCustom(reqPx);
        return phieuXuatChiTiets;
    }


    public InOutCommingNoteReportResponse getInOutCommingNoteReportData(ReportReq req) throws Exception {

        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        var fromDateToEnable = new Date(2022, 10, 10);

        InOutCommingNoteReportResponse result = new InOutCommingNoteReportResponse();
        List<InOutCommingNoteReportModel> lstReportModel = new ArrayList<>();
        if(req.getReportFromDate().before(fromDateToEnable)){
            req.setReportToDate(fromDateToEnable);
        }
        List<PhieuXuats> phieuXuats = getValidDeliveryNotes(req);
        List<PhieuThuChis> phieuThuChis = getValidInOutCommingNotes(req);


        ReportReq firstAmountFilter = new ReportReq();
        BeanUtils.copyProperties(req,firstAmountFilter);
        firstAmountFilter.setReportFromDate(fromDateToEnable);
        firstAmountFilter.setReportToDate(req.getReportFromDate());
        List<PhieuThuChis> phieuThuChisFirstAmount = getValidInOutCommingNotes(req);



        NhaThuocsReq reqNt =  new NhaThuocsReq();
        reqNt.setMaNhaThuocCha(userInfo.getNhaThuoc().getMaNhaThuoc());
        reqNt.setRecordStatusId(RecordStatusContains.ACTIVE);
        List<NhaThuocs> nhaThuocs = nhaThuocsRepository.searchList(reqNt);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        for (NhaThuocs nhaThuoc : nhaThuocs ) {
            if(lstReportModel.size() == req.getPaggingReq().getLimit()){
                break;
            }
            List<PhieuThuChis> firstInCommingNotesDictionary = phieuThuChisFirstAmount.stream().filter(item -> item.getMaCoSo() != null && item.getMaCoSo().equals(nhaThuoc.getMaNhaThuoc())
                    && Objects.equals(item.getLoaiPhieu(), InOutCommingType.OtherIncomming)).toList();
            Map<Long, BigDecimal> firstInCommingNotesDictionaryMap = firstInCommingNotesDictionary.stream().collect(Collectors.groupingBy(
                    PhieuThuChis::getNhanVienId,
                    Collectors.reducing(BigDecimal.ZERO, PhieuThuChis::getAmount, BigDecimal::add))
            );


            List<PhieuThuChis> firstOutCommingNotesDictionary = phieuThuChisFirstAmount.stream().filter(item -> item.getMaCoSo() != null && item.getMaCoSo().equals(nhaThuoc.getMaNhaThuoc())
                    && Objects.equals(item.getLoaiPhieu(), InOutCommingType.OtherOutcomming) || Objects.equals(item.getLoaiPhieu(), InOutCommingType.BusinessCosts) ).toList();
            Map<Long, BigDecimal> firstOutCommingNotesDictionaryMap = firstOutCommingNotesDictionary.stream().collect(Collectors.groupingBy(
                    PhieuThuChis::getNhanVienId,
                    Collectors.reducing(BigDecimal.ZERO, PhieuThuChis::getAmount, BigDecimal::add))
            );

            Map<Long, BigDecimal> dNotesDictionaryMap = phieuXuats.stream()
                    .filter(item -> item.getCreatedByUserId() != null).collect(Collectors.groupingBy(
                    PhieuXuats::getCreatedByUserId,
                    Collectors.reducing(BigDecimal.ZERO, PhieuXuats::getTongTien, BigDecimal::add))
            );

            List<PhieuThuChis> inCommingNotesDictionary = phieuThuChis.stream().filter(item -> item.getMaCoSo() != null && item.getMaCoSo().equals(nhaThuoc.getMaNhaThuoc())
                    && Objects.equals(item.getLoaiPhieu(), InOutCommingType.OtherIncomming)).toList();
            Map<Long, BigDecimal> inCommingNotesDictionaryMap = inCommingNotesDictionary.stream().collect(Collectors.groupingBy(
                    PhieuThuChis::getNhanVienId,
                    Collectors.reducing(BigDecimal.ZERO, PhieuThuChis::getAmount, BigDecimal::add))
            );

            List<PhieuThuChis> outCommingNotesDictionary = phieuThuChis.stream().filter(item -> item.getMaCoSo() != null && item.getMaCoSo().equals(nhaThuoc.getMaNhaThuoc())
                    && Objects.equals(item.getLoaiPhieu(), InOutCommingType.OtherOutcomming) || Objects.equals(item.getLoaiPhieu(), InOutCommingType.BusinessCosts)).toList();
            Map<Long, BigDecimal> outCommingNotesDictionaryMap = outCommingNotesDictionary.stream().collect(Collectors.groupingBy(
                    PhieuThuChis::getNhanVienId,
                    Collectors.reducing(BigDecimal.ZERO, PhieuThuChis::getAmount, BigDecimal::add))
            );


            NhanVienNhaThuocsReq nhanVienNhaThuocsReq = new NhanVienNhaThuocsReq();
            nhanVienNhaThuocsReq.setNhaThuocMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());

            List<NhanVienNhaThuocs> nhanVienNhaThuocs = nhanVienNhaThuocsRepository.searchList(nhanVienNhaThuocsReq);

            nhanVienNhaThuocs.forEach(nvien ->{

                BigDecimal firstInCommingAmount = firstInCommingNotesDictionaryMap.getOrDefault(nvien.getUserUserId(), BigDecimal.ZERO);
                BigDecimal firstOutCommingAmount = firstOutCommingNotesDictionaryMap.getOrDefault(nvien.getUserUserId(), BigDecimal.ZERO);
                BigDecimal sellAmount = dNotesDictionaryMap.getOrDefault(nvien.getUserUserId(), BigDecimal.ZERO);
                BigDecimal inCommingAmount = inCommingNotesDictionaryMap.getOrDefault(nvien.getUserUserId(), BigDecimal.ZERO);
                BigDecimal outCommingAmount = outCommingNotesDictionaryMap.getOrDefault(nvien.getUserUserId(), BigDecimal.ZERO);



                InOutCommingNoteReportModel model = new InOutCommingNoteReportModel();
                model.setFirstAmount(firstInCommingAmount.subtract(firstOutCommingAmount));
                model.setSellAmount(sellAmount);
                model.setInCommingAmount(inCommingAmount);
                model.setOutCommingAmount(outCommingAmount);

                if (model.getFirstAmount().compareTo(BigDecimal.ZERO) > 0 ||  model.getSellAmount().compareTo(BigDecimal.ZERO) > 0 ||
                    model.getInCommingAmount().compareTo(BigDecimal.ZERO) > 0 ||
                    model.getOutCommingAmount().compareTo(BigDecimal.ZERO) > 0 || model.getEndAmount().compareTo(BigDecimal.ZERO) > 0)
                {
                    Optional<UserProfile> byId = userProfileRepository.findById(nvien.getUserUserId());
                    model.setChildStoreCode(nhaThuoc.getMaNhaThuoc());
                    model.setChildStoreName(nhaThuoc.getTenNhaThuoc());
                    if(byId.isPresent()){
                        model.setStaffId(byId.get().getId());
                        model.setStaffName(byId.get().getTenDayDu());
                    }
                    lstReportModel.add(model);
                }
            });
        };

        if(lstReportModel.isEmpty()){
            return result;
        }
        BigDecimal getFirstAmount = lstReportModel.stream()
                .map(InOutCommingNoteReportModel::getFirstAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getSellAmount = lstReportModel.stream()
                .map(InOutCommingNoteReportModel::getSellAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getInCommingAmount = lstReportModel.stream()
                .map(InOutCommingNoteReportModel::getInCommingAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getOutCommingAmount = lstReportModel.stream()
                .map(InOutCommingNoteReportModel::getOutCommingAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getEndAmount = lstReportModel.stream()
                .map(InOutCommingNoteReportModel::getEndAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        result.setFirstTotalAmount(getFirstAmount);
        result.setSellTotalAmount(getSellAmount);
        result.setInCommingTotalAmount(getInCommingAmount);
        result.setOutCommingTotalAmount(getOutCommingAmount);
        result.setEndTotalAmount(getEndAmount);
        result.setListDetail(lstReportModel);
        return result;
    }

    public InventoryWarehouseResponse getInventoryWarehouseData(ReportReq req) throws Exception {

        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }

        InventoryWarehouseResponse result = new InventoryWarehouseResponse();
        List<InventoryWarehouseItem> lstReportModel = new ArrayList<>();

        PhieuKiemKesReq phieuKiemKesReq = new PhieuKiemKesReq();
        phieuKiemKesReq.setNhaThuocMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
        phieuKiemKesReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        phieuKiemKesReq.setDaCanKho(true);
        phieuKiemKesReq.setFromDate(req.getReportFromDate());
        phieuKiemKesReq.setToDate(req.getReportToDate());
        List<PhieuKiemKes> phieuKiemKesList = phieuKiemKesRepository.searchList(phieuKiemKesReq);

        if(phieuKiemKesList.isEmpty()){
            return result;
        }
        // Phiếu bù nhập
        if(req.getNoteTypeId().equals(ENoteType.Receipt)){
            for (PhieuKiemKes phieuKiemKe: phieuKiemKesList ) {
                if(lstReportModel.size() == req.getPaggingReq().getLimit()){
                    break;
                }
                if(phieuKiemKe.getPhieuNhapMaPhieuNhap() != null){
                    Optional<PhieuNhaps> phieuNhapsOtp = phieuNhapsRepository.findById(phieuKiemKe.getPhieuNhapMaPhieuNhap());
                    if(phieuNhapsOtp.isPresent()){
                        PhieuNhaps phieuNhaps = phieuNhapsOtp.get();
                        List<PhieuNhapChiTiets> allByPhieuNhapMaPhieuNhap = phieuNhapChiTietsRepository.findAllByPhieuNhapMaPhieuNhap(phieuNhaps.getId());
                        if(!allByPhieuNhapMaPhieuNhap.isEmpty()){
                            for (PhieuNhapChiTiets ctiet: allByPhieuNhapMaPhieuNhap) {
                                Optional<Thuocs> byId = thuocsRepository.findById(ctiet.getThuocThuocId());
                                if(byId.isPresent()){
                                    BigDecimal multiply = ctiet.getSoLuong().multiply(byId.get().getGiaBanLe());
                                    ctiet.setTotalAmount(multiply);
                                }
                            }
                            for (PhieuNhapChiTiets ctiet: allByPhieuNhapMaPhieuNhap) {
                                if(lstReportModel.size() == req.getPaggingReq().getLimit()){
                                    break;
                                }
                                Optional<Thuocs> byId = thuocsRepository.findById(ctiet.getThuocThuocId());
                                if(byId.isPresent()){
                                    Thuocs thuocs = byId.get();
                                    Optional<DonViTinhs> byId1 = donViTinhsRepository.findById(thuocs.getDonViXuatLeMaDonViTinh());
                                    InventoryWarehouseItem inventoryWarehouseItem = new InventoryWarehouseItem();
                                    inventoryWarehouseItem.setNoteNumber(phieuNhaps.getSoPhieuNhap());
                                    inventoryWarehouseItem.setNoteId(phieuNhaps.getId());
                                    inventoryWarehouseItem.setItemDate(phieuKiemKe.getCreated());
                                    inventoryWarehouseItem.setInventoryItemId(phieuKiemKe.getId());
                                    inventoryWarehouseItem.setItemId(thuocs.getId());
                                    inventoryWarehouseItem.setItemCode(thuocs.getMaThuoc());
                                    inventoryWarehouseItem.setItemName(thuocs.getTenThuoc());
                                    byId1.ifPresent(donViTinhs -> inventoryWarehouseItem.setUnitName(donViTinhs.getTenDonViTinh()));
                                    inventoryWarehouseItem.setQuantity(ctiet.getSoLuong());
                                    inventoryWarehouseItem.setPrice(ctiet.getGiaNhap());
                                    inventoryWarehouseItem.setOutPrice(thuocs.getGiaBanLe());
                                    inventoryWarehouseItem.setAmount(phieuNhaps.getTongTien());
                                    BigDecimal reduce = allByPhieuNhapMaPhieuNhap.stream().map(PhieuNhapChiTiets::getTotalAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                                            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
                                    inventoryWarehouseItem.setTotalAmount(reduce);
                                    lstReportModel.add(inventoryWarehouseItem);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Phiếu bù xuất
        else if(req.getNoteTypeId().equals(ENoteType.Delivery)){
            for (PhieuKiemKes phieuKiemKe: phieuKiemKesList ) {
                if(lstReportModel.size() == req.getPaggingReq().getLimit()){
                    break;
                }
                if(phieuKiemKe.getPhieuXuatMaPhieuXuat() != null){
                    Optional<PhieuXuats> phieuXuatsOtp = phieuXuatsRepository.findById(phieuKiemKe.getPhieuXuatMaPhieuXuat());
                    if(phieuXuatsOtp.isPresent()){
                        PhieuXuats phieuNhaps = phieuXuatsOtp.get();
                        List<PhieuXuatChiTiets> allByPhieuXuatMaPhieuXuat = phieuXuatChiTietsRepository.findAllByPhieuXuatMaPhieuXuat(phieuNhaps.getId());
                        if(!allByPhieuXuatMaPhieuXuat.isEmpty()){
                            for (PhieuXuatChiTiets ctiet: allByPhieuXuatMaPhieuXuat) {
                                Optional<Thuocs> byId = thuocsRepository.findById(ctiet.getThuocThuocId());
                                if(byId.isPresent()){
                                    BigDecimal multiply = ctiet.getSoLuong().multiply(byId.get().getGiaBanLe());
                                    ctiet.setTotalAmount(multiply);
                                }
                            }
                            for (PhieuXuatChiTiets ctiet: allByPhieuXuatMaPhieuXuat) {
                                if(lstReportModel.size() == req.getPaggingReq().getLimit()){
                                    break;
                                }
                                Optional<Thuocs> byId = thuocsRepository.findById(ctiet.getThuocThuocId());
                                if(byId.isPresent()){
                                    Thuocs thuocs = byId.get();
                                    Optional<DonViTinhs> byId1 = donViTinhsRepository.findById(thuocs.getDonViXuatLeMaDonViTinh());
                                    InventoryWarehouseItem inventoryWarehouseItem = new InventoryWarehouseItem();
                                    inventoryWarehouseItem.setNoteNumber(phieuNhaps.getSoPhieuXuat());
                                    inventoryWarehouseItem.setNoteId(phieuNhaps.getId());
                                    inventoryWarehouseItem.setItemDate(phieuKiemKe.getCreated());
                                    inventoryWarehouseItem.setInventoryItemId(phieuKiemKe.getId());
                                    inventoryWarehouseItem.setItemId(thuocs.getId());
                                    inventoryWarehouseItem.setItemCode(thuocs.getMaThuoc());
                                    inventoryWarehouseItem.setItemName(thuocs.getTenThuoc());
                                    byId1.ifPresent(donViTinhs -> inventoryWarehouseItem.setUnitName(donViTinhs.getTenDonViTinh()));
                                    inventoryWarehouseItem.setQuantity(ctiet.getSoLuong());
                                    inventoryWarehouseItem.setPrice(ctiet.getGiaXuat());
                                    inventoryWarehouseItem.setOutPrice(thuocs.getGiaBanLe());
                                    inventoryWarehouseItem.setAmount(phieuNhaps.getTongTien());
                                    BigDecimal reduce = allByPhieuXuatMaPhieuXuat.stream().map(PhieuXuatChiTiets::getTotalAmount) // Chọn cột để tính tổng (ví dụ là cột 2)
                                            .reduce(BigDecimal.valueOf(0), BigDecimal::add);
                                    inventoryWarehouseItem.setTotalAmount(reduce);
                                    lstReportModel.add(inventoryWarehouseItem);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(lstReportModel.isEmpty()){
            return result;
        }
        BigDecimal getTotalAmount = lstReportModel.stream()
                .map(item -> item.getQuantity().multiply(item.getPrice())) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getTotalAmountByOutPrice = lstReportModel.stream()
                .map(item -> item.getQuantity().multiply(item.getOutPrice())) // Chọn cột để tính tổng (ví dụ là cột 2)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        result.setTotalAmount(getTotalAmount);
        result.setTotalAmountByOutPrice(getTotalAmountByOutPrice);
        result.setListDetail(lstReportModel);
        return result;
    }

    public DrugWarehouseResponse getDrugWarehouses(ReportReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null){
            throw new Exception("Bad request.");
        }
        DrugWarehouseResponse result = new DrugWarehouseResponse();
        List<DrugWarehouseItem> lstReportModel = new ArrayList<>();
        ThuocsReq thuocsReq = new ThuocsReq();
        thuocsReq.setNhaThuocMaNhaThuoc(userInfo.getNhaThuoc().getMaNhaThuoc());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        Page<Thuocs> thuocPage = thuocsRepository.searchPage(thuocsReq,pageable);

        if(thuocPage.getContent().isEmpty()){
            return result;
        }

        for (Thuocs thuocs : thuocPage.getContent()) {
            Optional<DonViTinhs> byId1 = donViTinhsRepository.findById(thuocs.getDonViXuatLeMaDonViTinh());
            byId1.ifPresent(donViTinhs -> thuocs.setTenDonViTinhXuatLe(donViTinhs.getTenDonViTinh()));
            this.bindingDataThuoc(thuocs,req);
        }


        List<Thuocs> listItem = thuocPage.getContent();

//        result.DeliveryValueTotal = filter.GroupFilterTypeId == (int)GroupFilterType.All ? drugWarehouseItems.Sum(i => i.DeliveryInventoryValueInPeriod) - moneyCusreturn : drugWarehouseItems.Sum(i => i.DeliveryInventoryValueInPeriod);

        BigDecimal getFirsInventoryValueTotal = listItem.stream()
                .filter(item -> item.getFirstInventoryValue() != null)
                .map(item -> item.getFirstInventoryValue())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getLastInventoryValueTotal = lstReportModel.stream()
                .map(item -> item.getLastInventoryValue())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getReceiptValueTotal = lstReportModel.stream()
                .map(item -> item.getReceiptInventoryValueInPeriod())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getDeliveryValueTotal1 = lstReportModel.stream()
                .map(item -> item.getDeliveryInventoryValueInPeriod1())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getFirsInventoryValueTotal1 = lstReportModel.stream()
                .map(item -> item.getFirstInventoryValue1())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getLastInventoryValueTotal1 = lstReportModel.stream()
                .map(item -> item.getLastInventoryValue1())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);
        BigDecimal getReceiptValueTotal1 = lstReportModel.stream()
                .map(item -> item.getReceiptInventoryValueInPeriod1())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        result.setFirsInventoryValueTotal(getFirsInventoryValueTotal);
        result.setLastInventoryValueTotal(getLastInventoryValueTotal);
        result.setReceiptValueTotal(getReceiptValueTotal);
        result.setDeliveryValueTotal1(getDeliveryValueTotal1);
        result.setFirsInventoryValueTotal1(getFirsInventoryValueTotal1);
        result.setLastInventoryValueTotal1(getLastInventoryValueTotal1);
        result.setReceiptValueTotal1(getReceiptValueTotal1);

        result.setPageDetail(thuocPage);

        return result;
    }


    public void bindingDataThuoc(Thuocs thuocs,ReportReq reportReq){
        //Set thuoc
        thuocs.setFirstInventoryValue(thuocs.getSoDuDauKy().multiply(thuocs.getGiaDauKy()));
        thuocs.setInitReceiptQuantity(thuocs.getSoDuDauKy());
        thuocs.setInitReceiptValue(thuocs.getSoDuDauKy().multiply(thuocs.getGiaDauKy()));
        thuocs.setInitRetailPrice(thuocs.getGiaDauKy());
        thuocs.setLastReceiptRetailPrice(thuocs.getGiaDauKy());
        // START FirstInventoryQuantity = Math.Round(InitReceiptQuantity + FirstReceiptQuantity - FirstDeliveryQuantity, 1);
        thuocs.setInitReceiptQuantity(thuocs.getSoDuDauKy());
        // Phiếu nhập
        List<PhieuNhapChiTiets> receiptItems = getValidPhieuNhapChiTiet(thuocs,reportReq);
        //Phieesu xuất
        List<PhieuXuatChiTiets> deliveryItems = getValidPhieuXuatChiTiet(thuocs,reportReq);


        // Arena nhập
        List<PhieuNhapChiTiets> validReceiptItems = receiptItems.stream().filter(item ->
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.Receipt) || item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.InventoryAdjustment)).toList();
        BigDecimal receiptQuantity = validReceiptItems.stream()
                .filter(item -> item.getRetailQuantity() != null)
                .map(item -> item.getRetailQuantity())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        List<PhieuNhapChiTiets> returnedFromCustomerItems = receiptItems.stream().filter(item ->
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.ReturnFromCustomer)).toList();
        BigDecimal returnedFromCustomerQuantity = returnedFromCustomerItems.stream()
                .filter(item -> item.getRetailQuantity() != null)
                .map(item -> item.getRetailQuantity())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);


        // Arena xuất
        List<PhieuXuatChiTiets> validDeliveryItems = deliveryItems.stream().filter(item ->
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.Delivery) ||
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.InventoryAdjustment) ||
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.WarehouseTransfer) ||
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.CancelDelivery)).toList();
        BigDecimal deliveryQuantity = validDeliveryItems.stream()
                .filter(item -> item.getRetailQuantity() != null)
                .map(item -> item.getRetailQuantity())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        List<PhieuXuatChiTiets> returnedToSupplyerItems = deliveryItems.stream().filter(item ->
                item.getLoaiXuatNhapMaLoaiXuatNhap().equals(ENoteType.ReturnFromCustomer)).toList();
        BigDecimal returnedToSupplyerQuantity = returnedToSupplyerItems.stream()
                .filter(item -> item.getRetailQuantity() != null)
                .map(item -> item.getRetailQuantity())
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        thuocs.setFirstDeliveryQuantity(deliveryQuantity.add(returnedToSupplyerQuantity));
        thuocs.setFirstReceiptQuantity(receiptQuantity.add(returnedFromCustomerQuantity));


        // START ReceiptInventoryQuantityInPeriod  = LastReceiptQuantity - FirstReceiptQuantity
        thuocs.setLastReceiptQuantity(receiptQuantity.add(returnedFromCustomerQuantity));
        // FirstReceiptQuantity đã set ở trên


        // START DeliveryInventoryQuantityInPeriod = LastDeliveryQuantity - FirstDeliveryQuantity ;
        thuocs.setLastDeliveryQuantity(deliveryQuantity.add(returnedToSupplyerQuantity));
        // FirstDeliveryQuantity set ở trên;



        // Set FirstInventoryValue
        BigDecimal inventoryQuantity = thuocs.getFirstInventoryQuantity().subtract(thuocs.getInitReceiptQuantity());
        if (inventoryQuantity.compareTo(AppConstants.EspQuantity)>0)
        {
            // TODO
//            BigDecimal quantity = inventoryQuantity;
//
//            List<PhieuNhapChiTiets> validReceiptItems1 = validReceiptItems;
//
//            while (quantity.compareTo(AppConstants.EspQuantity) > 0 && validReceiptItems1.size() > 0)
//            {
//                var usedQuantity = Math.Min(quantity, receiptItemsByDrug[0].RetailQuantity);
//                var firstItem = receiptItemsByDrug[0];
//                quantity -= usedQuantity;
//                inventoryValue += usedQuantity * firstItem.RetailPrice;
//                receiptItemsByDrug.RemoveAt(0);
//            }
//            if (quantity > AppConstants.EspQuantity)
//            {
//                inventoryValue += quantity * thuocs.getInitRetailPrice();
//            }
//            inventoryValue += drugWarehouse.InitReceiptValue;
        }
        else
        {
            thuocs.setFirstInventoryValue(thuocs.getFirstInventoryQuantity().multiply(thuocs.getInitReceiptQuantity()));
        }
        if (thuocs.getFirstInventoryQuantity().compareTo(AppConstants.EspQuantity)<0)
        {
            thuocs.setFirstInventoryValue(BigDecimal.ZERO);
        }

        // Set LastInventoryValue
        var lastInventoryQuantity = thuocs.getLastInventoryQuantity().subtract(thuocs.getInitReceiptQuantity());
        BigDecimal inventoryValue = BigDecimal.ZERO;
        if (lastInventoryQuantity.compareTo(AppConstants.EspQuantity) > 0)
        {
            // TODO
//            if (receiptItemsByDrug != null)
//            {
//                var quantity = inventoryQuantity;
//                while (quantity > AppConstants.EspQuantity && receiptItemsByDrug.Count > 0)
//                {
//                    var usedQuantity = Math.Min(quantity, receiptItemsByDrug[0].RetailQuantity);
//                    var firstItem = receiptItemsByDrug[0];
//                    quantity -= usedQuantity;
//                    inventoryValue += usedQuantity * firstItem.RetailPrice * (1 - firstItem.Discount / 100) * (1 + (firstItem.VAT / 100));
//                    receiptItemsByDrug.RemoveAt(0);
//                }
//                if (quantity > AppConstants.EspQuantity)
//                {
//                    inventoryValue += quantity * drugWarehouse.InitRetailPrice;
//                }
//                inventoryValue += drugWarehouse.InitReceiptValue;
//            }
        }
        else
        {
            inventoryValue = thuocs.getLastInventoryQuantity().multiply(thuocs.getInitRetailPrice());
        }
        thuocs.setLastInventoryValue(inventoryValue);
        if (thuocs.getLastInventoryValue().compareTo(AppConstants.EspQuantity) < 0)
        {
            thuocs.setLastInventoryValue(BigDecimal.ZERO);
        }
    }


    private List<PhieuNhapChiTiets> getValidPhieuNhapChiTiet(Thuocs thuocs,ReportReq req){
        PhieuNhapChiTietsReq phieuNhapChiTietsReq = new PhieuNhapChiTietsReq();
        phieuNhapChiTietsReq.setNhaThuocMaNhaThuoc(thuocs.getNhaThuocMaNhaThuoc());
        phieuNhapChiTietsReq.setThuocThuocId(10714554L);
        phieuNhapChiTietsReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        List<PhieuNhapChiTiets> phieuNhapChiTietsList = phieuNhapChiTietsRepository.searchListReport(phieuNhapChiTietsReq);

        for (PhieuNhapChiTiets ctiet:phieuNhapChiTietsList) {
            Optional<PhieuNhaps> byId = phieuNhapsRepository.findById(ctiet.getPhieuNhapMaPhieuNhap());
            if(byId.isPresent()){
                ctiet.setLoaiXuatNhapMaLoaiXuatNhap(byId.get().getLoaiXuatNhapMaLoaiXuatNhap());
            }
        }

        return phieuNhapChiTietsList;
    }

    private List<PhieuXuatChiTiets> getValidPhieuXuatChiTiet(Thuocs thuocs,ReportReq req){
        PhieuXuatChiTietsReq pxCtietReq = new PhieuXuatChiTietsReq();
        pxCtietReq.setNhaThuocMaNhaThuoc(thuocs.getNhaThuocMaNhaThuoc());
        pxCtietReq.setThuocThuocId(thuocs.getId());
        pxCtietReq.setRecordStatusId(RecordStatusContains.ACTIVE);
        List<PhieuXuatChiTiets> phieuXuatChiTietsList = phieuXuatChiTietsRepository.searchListReport(pxCtietReq);

        for (PhieuXuatChiTiets ctiet:phieuXuatChiTietsList) {
            Optional<PhieuXuats> byId = phieuXuatsRepository.findById(ctiet.getPhieuXuatMaPhieuXuat());
            if(byId.isPresent()){
                ctiet.setLoaiXuatNhapMaLoaiXuatNhap(byId.get().getMaLoaiXuatNhap());
            }
        }

        return phieuXuatChiTietsList;
    }

    }

