package com.nemo.mixsafe.dto.Green;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Green3Row {

    @JacksonXmlProperty(localName = "knd")
    private String knd;

    @JacksonXmlProperty(localName = "prdt_mstr_no")
    private String prdtMstrNo;

    @JacksonXmlProperty(localName = "prdtarm")
    private String prdtarm;

    @JacksonXmlProperty(localName = "barcd_info")
    private String barcdInfo;

    @JacksonXmlProperty(localName = "prdtnm_eng")
    private String prdtnmEng;

    @JacksonXmlProperty(localName = "prdtnm_kor")
    private String prdtnmKor;

    @JacksonXmlProperty(localName = "prdtn_incme_cmpnynm")
    private String prdtnIncmeCmpnynm;

    @JacksonXmlProperty(localName = "slfsfcfst_no")
    private String slfsfcfstNo;
}