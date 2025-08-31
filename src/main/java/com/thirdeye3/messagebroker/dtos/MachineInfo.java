package com.thirdeye3.messagebroker.dtos;

import java.util.Map;

import com.thirdeye3.messagebroker.dtos.Machine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MachineInfo {
	Map<String, Machine> machineDtos;
	Integer telegramBotMachines;
}
 