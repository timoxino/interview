package com.timoxino.interview.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataPatch {
    private DataPatchOperation op;
    private String path;
    private Object value;
}
