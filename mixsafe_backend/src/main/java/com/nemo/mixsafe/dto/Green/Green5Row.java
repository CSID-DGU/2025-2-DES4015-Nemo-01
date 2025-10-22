package com.nemo.mixsafe.dto.Green;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Green5Row {

    @JacksonXmlProperty(localName = "bi_mttrnm_eng")
    private String biMttrnmEng;

    @JacksonXmlProperty(localName = "bi_mttrnm_kor")
    private String biMttrnmKor;

    @JacksonXmlProperty(localName = "prdt_mstr_no")
    private String prdtMstrNo;

    @JacksonXmlProperty(localName = "gci_pictrchrctr")
    private String gciPictrchrctr;

    @JacksonXmlProperty(localName = "prdtnm_eng")
    private String prdtnmEng;

    @JacksonXmlProperty(localName = "prdtnm_kor")
    private String prdtnmKor;

    @JacksonXmlProperty(localName = "gci_hrmflnsriskwords")
    private String gciHrmflnsriskwords;

    @JacksonXmlProperty(localName = "chm_mttr_fnc")
    private String chmMttrFnc;

    @JacksonXmlProperty(localName = "cas_no")
    private String casNo;
}
