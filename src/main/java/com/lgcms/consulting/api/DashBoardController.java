package com.lgcms.consulting.api;

import com.lgcms.consulting.common.dto.BaseResponse;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.*;
import com.lgcms.consulting.service.dashboard.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dashboard")
@RestController
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @PostMapping("/status")
    public ResponseEntity<BaseResponse<MonthlyStatusResponse>> getMonthlyStatus(@RequestHeader("X-USER-ID") Long id) {
        return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getMonthlyStatus(id)));
    }
 }
