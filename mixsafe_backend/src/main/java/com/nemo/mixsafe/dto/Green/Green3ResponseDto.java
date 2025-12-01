package com.nemo.mixsafe.dto.Green;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "rows") // XML 루트 태그
public class Green3ResponseDto {

    @JacksonXmlProperty(localName = "count")
    private Integer count;

    @JacksonXmlProperty(localName = "resultcode")
    private String resultcode;

    @JacksonXmlProperty(localName = "pagenum")
    private Integer pagenum;

    @JacksonXmlProperty(localName = "pagesize")
    private Integer pagesize;

    // <row> </row> 반복
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "row")
    private List<Green3Row> rows;
}
