package com.crlink.smartpark.customer.controller.response.editor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EditorChoiceAnalysisDTO extends EditorAnalysisDTO {
    private List<EditorChoiceItemDTO> choices;

    public EditorChoiceAnalysisDTO(String id, String name, String elementType) {
        super(id, name, elementType);
    }
}
