package com.thirdeye3.messagebroker.externalcontrollers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.thirdeye3.messagebroker.dtos.Response;

@FeignClient(name = "${spring.application.name}")
public interface SelfClient {
    @GetMapping("/api/statuschecker/{id}/{code}")
	Response<String> statusChecker(@PathVariable("id") Integer id, @PathVariable("code") String code);
}

