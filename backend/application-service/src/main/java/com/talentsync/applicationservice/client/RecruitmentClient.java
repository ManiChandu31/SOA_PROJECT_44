package com.talentsync.applicationservice.client;
import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*;
@FeignClient(name="recruitment-service") public interface RecruitmentClient { @PostMapping("/api/recruitment/records") void createRecord(@RequestBody RecordRequest request); record RecordRequest(Long applicationId,Long candidateId,Long jobId){} }
