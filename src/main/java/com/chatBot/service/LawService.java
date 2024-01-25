package com.chatBot.service;

import com.chatBot.dto.LawDataRequestDTO;
import com.chatBot.model.LawData;

public interface LawService {

	LawData saveLawData(LawDataRequestDTO lawDataRequestDTO);

}
