package vn.com.gsoft.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.report.constant.PathContains;
import vn.com.gsoft.report.model.dto.ReportReq;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.model.system.BaseResponse;
import vn.com.gsoft.report.service.ReportingDate.PhieuXuatsService;
import vn.com.gsoft.report.service.impl.ReportServiceImpl;
import vn.com.gsoft.report.util.system.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportServiceImpl service;

    @PostMapping(value = PathContains.URL_THU_CHI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> inOutCommingDetailsByDayResponse(@RequestBody ReportReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.inOutCommingDetailsByDayResponse(objReq)));
    }

//    @PostMapping(value = PathContains.URL_BAO_CAO_THEO_NHAN_VIEN, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> getReportByUser(@RequestBody ReportReq objReq) throws Exception {
//        return ResponseEntity.ok(ResponseUtils.ok(service.inOutCommingDetailsByDayResponse(objReq)));
//    }
//
//
//    @PostMapping(value = PathContains.URL_DOANH_THU_DAILY, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> getRevenueDrugSynthesis(@RequestBody ReportReq objReq) throws Exception {
//        return ResponseEntity.ok(ResponseUtils.ok(service.getRevenueDrugSynthesis(objReq)));
//    }

//    @PostMapping(value = PathContains.URL_REPORT_BY_DATA, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<BaseResponse> getReportByData(@RequestBody ReportReq objReq) throws Exception {
////        return ResponseEntity.ok(ResponseUtils.ok(service.getReportByData(objReq)));
//    }

    @PostMapping(value = PathContains.URL_BAO_CAO_TONG_HOP_THU_CHI, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getInOutCommingNoteReportData(@RequestBody ReportReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.getInOutCommingNoteReportData(objReq)));
    }

    @PostMapping(value = PathContains.URL_BAO_CAO_KIEM_KE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> getInventoryWarehouseData(@RequestBody ReportReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.getInventoryWarehouseData(objReq)));
    }

}