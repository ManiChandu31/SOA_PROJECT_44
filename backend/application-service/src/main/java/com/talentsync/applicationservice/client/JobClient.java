package com.talentsync.applicationservice.client;
import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*;
@FeignClient(name="job-service") public interface JobClient { @GetMapping("/api/jobs/{id}") Object getJob(@PathVariable("id") Long id); }
