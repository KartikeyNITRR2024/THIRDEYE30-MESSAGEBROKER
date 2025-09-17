package com.thirdeye3.messagebroker.externalcontrollers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.thirdeye3.messagebroker.dtos.MachineInfo;
import com.thirdeye3.messagebroker.dtos.Response;

@FeignClient(name = "THIRDEYE30-PROPERTYMANAGER")
public interface PropertyManagerClient {

    @GetMapping("/pm/machines/telegrambot")
    Response<MachineInfo> getMachines();
}
