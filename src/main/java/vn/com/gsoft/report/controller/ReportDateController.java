package vn.com.gsoft.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.report.constant.PathContains;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.model.system.BaseResponse;
import vn.com.gsoft.report.service.ReportingDate.PhieuXuatsService;
import vn.com.gsoft.report.util.system.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/report-date")
public class ReportDateController {

    @Autowired
    PhieuXuatsService service;

    @PostMapping(value = PathContains.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> response(@RequestBody PhieuXuatsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchReportingDate(objReq)));
    }
}