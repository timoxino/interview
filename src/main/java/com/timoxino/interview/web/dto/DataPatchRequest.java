package com.timoxino.interview.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class DataPatchRequest {
    private String uuid;
    private List<DataPatch> patch;
}
