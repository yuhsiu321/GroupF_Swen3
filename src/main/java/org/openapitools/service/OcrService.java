package org.openapitools.service;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrService {
    String recognizeText(MultipartFile imageFile) throws IOException, TesseractException ;
}
