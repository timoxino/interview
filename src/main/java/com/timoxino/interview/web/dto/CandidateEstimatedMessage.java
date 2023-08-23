package com.timoxino.interview.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CandidateEstimatedMessage extends CandidateBaseMessage {
    private Integer lvlEstimated;
}
