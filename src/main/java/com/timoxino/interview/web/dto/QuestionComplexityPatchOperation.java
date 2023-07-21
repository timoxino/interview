package com.timoxino.interview.web.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timoxino.interview.web.model.DataNode;
import com.timoxino.interview.web.model.QuestionComplexityNode;

public enum QuestionComplexityPatchOperation {

    @JsonProperty("add")
    ADD() {
        @Override
        public void execute(QuestionComplexityNode node, Map<String, Object> valueMap) {
            DataNode question = (DataNode) valueMap.get("questionNode");
            node.getQuestions().add(question);
        }
    },
    @JsonProperty("delete")
    DELETE() {
        @Override
        public void execute(QuestionComplexityNode node, Map<String, Object> valueMap) {
            DataNode question = (DataNode) valueMap.get("questionNode");
            node.getQuestions().remove(question);
        }
    };

    public abstract void execute(QuestionComplexityNode node, Map<String, Object> valueMap);
}
