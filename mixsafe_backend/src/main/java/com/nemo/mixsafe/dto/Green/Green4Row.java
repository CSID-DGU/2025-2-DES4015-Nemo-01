package com.nemo.mixsafe.dto.Green;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Green4Row {

    @JacksonXmlProperty(localName = "knd")
    private String knd;

    @JacksonXmlProperty(localName = "prdt_mstr_no")
    private String prdtMstrNo;

    @JacksonXmlProperty(localName = "stdusewt")
    private String stdusewt;

    @JacksonXmlProperty(localName = "prdtnm_kor")
    private String prdtnmKor;

    @JacksonXmlProperty(localName = "prdtn_incme_cmpnynm")
    private String prdtnIncmeCmpnynm;

    @JacksonXmlProperty(localName = "useuppt_atpn")
    private String useupptAtpn;

    @JacksonXmlProperty(localName = "barcd_info")
    private String barcdInfo;

    @JacksonXmlProperty(localName = "prdt_photo_url")
    private String prdtPhotoUrl;   // "url1||url2" 형태 그대로 문자열로 받음

    @JacksonXmlProperty(localName = "prdtnm_eng")
    private String prdtnmEng;

    @JacksonXmlProperty(localName = "childprtcpackng_trget_no")
    private String childprtcpackngTrgetNo;

    @JacksonXmlProperty(localName = "prdtarm_middlcl_nm")
    private String prdtarmMiddlclNm;

    @JacksonXmlProperty(localName = "adres")
    private String adres;

    @JacksonXmlProperty(localName = "etc_info")
    private String etcInfo;

    @JacksonXmlProperty(localName = "wt")
    private String wt;

    @JacksonXmlProperty(localName = "slfsfcfst_no")
    private String slfsfcfstNo;
}
