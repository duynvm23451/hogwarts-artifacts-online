package edu.tcu.cs.hogwartsartifactsonline.wizard.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record WizardDto(
        Integer id,
        @NotEmpty(message = "name is required.") String name,
        Integer numberOfArtifacts) {
}
