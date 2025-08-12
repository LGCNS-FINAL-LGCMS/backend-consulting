package com.lgcms.consulting.api;

import com.lgcms.consulting.common.dto.BaseResponse;
import com.lgcms.consulting.dto.response.dashboard.DashBoardResponse.*;
import com.lgcms.consulting.service.dashboard.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/consulting/dashboard")
@RestController
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @GetMapping("/status/month")
    public ResponseEntity<BaseResponse<MonthlyStatusResponse>> getMonthlyStatus(@RequestHeader("X-USER-ID") Long id) {
        return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getMonthlyStatus(id)));
    }

    @GetMapping("/profit/distribution")
    public ResponseEntity<BaseResponse<ProfitDistributionResponse>> getProfitDistribution(@RequestHeader("X-USER-ID") Long id) {
        return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getProfitDistribution(id)));
    }

    @GetMapping("/profit/overview")
    public ResponseEntity<BaseResponse<ProfitOverviewResponse>> getProfitOverview(
            @RequestHeader("X-USER-ID") Long id,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getProfitOverview(startDate, endDate, id)));
    }

    @GetMapping("/progress/complete")

    public ResponseEntity<BaseResponse<CompleteProgressResponse>> getCompleteProgress(@RequestHeader("X-USER-ID") Long id) {
        return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getCompleteProgress(id)));
    }

    @GetMapping("/progress/group")

    public ResponseEntity<BaseResponse<ProgressGroupResponse>> getProgressGroup(
            @RequestHeader("X-USER-ID") Long id,
            @RequestParam(value = "title", defaultValue = "all" , required = false) String title
    ) {
        if (title.equals("all")) {
            return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getProgressGroup(id)));
        }
        else{
            return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getProgressGroupAll(id, title)));
        }
    }

    @GetMapping("/lecture/count")

    public ResponseEntity<BaseResponse<StudentLectureCountResponse>> getStudentLectureCount(@RequestHeader("X-USER-ID") Long id) {
        return ResponseEntity.ok(BaseResponse.ok(dashBoardService.getStudentLectureCount(id)));
    }

}
