package org.openapitools.configuration;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class TesseractOcrConfig {

    private String dataPath = "/OCR/tess4j_data";

    @Bean
    public Tesseract tesseract() {

        Tesseract tesseract = new Tesseract();
        // 设置训练数据文件夹路径
        tesseract.setDatapath(dataPath);
        // 设置为中文简体
        tesseract.setLanguage("chi_sim");
        return tesseract;
    }

}
