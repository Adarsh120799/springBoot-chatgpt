package com.chatBot.controller;



import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.chatBot.dto.APIResponseDTO;
import com.chatBot.dto.LawDataRequestDTO;
import com.chatBot.model.LawData;
import com.chatBot.service.LawService;

@Controller
@RequestMapping("/api/law")
public class LawController {

 @Autowired
 private LawService lawServiceImpl;

 @PostMapping("/saveFormData")
 public ResponseEntity<APIResponseDTO> saveFormData(@RequestBody LawDataRequestDTO lawDataRequestDTO) {
     LawData savedData = lawServiceImpl.saveLawData(lawDataRequestDTO);
     return ResponseEntity.ok().body(APIResponseDTO.builder().status(1)
             .data(savedData)
             .message("Data Saved Sucessfully").tag("saveFormData").build());
 }

 @PostMapping("/saveFromPdf")
 public ResponseEntity<APIResponseDTO> saveFromPdf(@RequestBody MultipartFile pdfFile) throws IOException {
     if (pdfFile == null || pdfFile.isEmpty()) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(APIResponseDTO.builder().status(0)
                         .message("PDF file is required in the request body.")
                         .tag("saveFromPdf").build());
     }

     // Extract text from PDF
     String pdfText = extractTextFromPdf(pdfFile);

     if (pdfText == null || pdfText.isEmpty()) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                 .body(APIResponseDTO.builder().status(0)
                         .message("Unable to extract text from the PDF.")
                         .tag("saveFromPdf").build());
     }

     LawDataRequestDTO lawDataRequestDTO = new LawDataRequestDTO();
     lawDataRequestDTO.setDescription(pdfText);

     // Extract name without extension
     String originalFilename = pdfFile.getOriginalFilename();
     String ipcSection = originalFilename != null ? originalFilename.replaceAll("\\..*$", "") : "";

     lawDataRequestDTO.setIpcSection(ipcSection);

     LawData savedData = lawServiceImpl.saveLawData(lawDataRequestDTO);

     return ResponseEntity.ok().body(APIResponseDTO.builder().status(1)
             .data(savedData)
             .message("Data Saved Successfully")
             .tag("saveFromPdf").build());
 }

 private String extractTextFromPdf(MultipartFile pdfFile) throws IOException {
     try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
         PDFTextStripper stripper = new PDFTextStripper();
         return stripper.getText(document);
     }
 }
}

