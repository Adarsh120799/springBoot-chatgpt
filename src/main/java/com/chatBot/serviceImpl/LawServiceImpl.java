package com.chatBot.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatBot.dto.LawDataRequestDTO;
import com.chatBot.model.LawData;
import com.chatBot.repository.LawRepository;
import com.chatBot.service.LawService;

@Service
public class LawServiceImpl implements LawService {

	@Autowired
    private LawRepository lawRepository;

    public LawData saveLawData(LawDataRequestDTO lawDataRequestDTO) {
    	LawData lawData = new LawData();
        lawData.setIpcSection(lawDataRequestDTO.getIpcSection());
        lawData.setDescription(lawDataRequestDTO.getDescription());
        return lawRepository.save(lawData);
    }

}
