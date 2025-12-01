package com.nemo.mixsafe.dto.Green;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "rows")
public class Green4ResponseDto {

    @JacksonXmlProperty(localName = "count")
    private Integer count;

    @JacksonXmlProperty(localName = "resultcode")
    private String resultcode;

    @JacksonXmlProperty(localName = "pagenum")
    private Integer pagenum;

    @JacksonXmlProperty(localName = "pagesize")
    private Integer pagesize;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "row")
    private List<Green4Row> rows;
}
