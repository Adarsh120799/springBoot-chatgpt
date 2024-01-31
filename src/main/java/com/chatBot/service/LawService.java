package com.chatBot.service;

import java.util.List;

import com.chatBot.dto.LawDataRequestDTO;
import com.chatBot.model.LawData;

public interface LawService {

	LawData saveLawData(LawDataRequestDTO lawDataRequestDTO);

	List<LawData> getDataByIpcSection(LawDataRequestDTO lawDataRequestDTO);

}
