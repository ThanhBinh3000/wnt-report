package vn.com.gsoft.report.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.com.gsoft.report.constant.ENoteType;
import vn.com.gsoft.report.constant.InOutCommingType;
import vn.com.gsoft.report.constant.RecordStatusContains;
import vn.com.gsoft.report.entity.ReportingDate.*;
import vn.com.gsoft.report.model.dto.*;
import vn.com.gsoft.report.model.dto.ReportingDate.*;
import vn.com.gsoft.report.model.system.Profile;
import vn.com.gsoft.report.repository.BaseRepository;
import vn.com.gsoft.report.repository.ReportingDate.*;
import vn.com.gsoft.report.util.system.DataUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl {

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


    private List <PhieuNhapChiTiets> getValidReceiptNoteItems(ReportReq req){
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
    }

