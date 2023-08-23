package com.timoxino.interview.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CandidateEstimatedMessage extends CandidateBaseMessage {
    private Integer lvlEstimated;
}
