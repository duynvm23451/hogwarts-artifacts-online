package edu.tcu.cs.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        wizards = new ArrayList<>();
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("name 1");
        wizards.add(wizard);

        Wizard wizard2 = new Wizard();
        wizard2.setId(2);
        wizard2.setName("name 2");
        wizards.add(wizard2);

        Wizard wizard3 = new Wizard();
        wizard3.setId(3);
        wizard3.setName("name 3");
        wizards.add(wizard3);
    }

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {
        // given
        given(wizardService.findAll()).willReturn(wizards);
        // then
        this.mockMvc.perform(get(baseUrl+ "/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("name 1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("name 2"));

    }

    @Test
    void testAddWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(null, "Duy Potter", 0);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard newWizard = new Wizard();
        newWizard.setId(1);
        newWizard.setName("Duy Potter");

        given(wizardService.save(Mockito.any(Wizard.class))).willReturn(newWizard);
        // Then
        this.mockMvc.perform(post(baseUrl + "/wizards")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Duy Potter"));

    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        // given
        given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));

        // when and then
        this.mockMvc.perform(get(baseUrl+ "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("name 1"));

    }

    @Test
    void testFindWizardByIdFail() throws Exception {
        // Given
        given(this.wizardService.findById(1)).willThrow(new ObjectNotFoundException("wizard",1));

        // When and Then
        this.mockMvc.perform(get(baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(null, "Duy Potter", 0);
        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("Duy Potter");

        given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(updatedWizard);
        // When and Then
        this.mockMvc.perform(put(baseUrl + "/wizards/1")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Duy Potter"));

    }

    @Test
    void testUpdateWizardWithNonExistentId() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(
                null, "Duy Potter", 0
        );

        String json = this.objectMapper.writeValueAsString(wizardDto);

        given(wizardService.update(eq(1), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard",1));
        //
        this.mockMvc.perform(put(baseUrl + "/wizards/1")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteWizard() throws Exception {
        // Given
        doNothing().when(this.wizardService).delete(1);

        // When and Then
        this.mockMvc.perform(delete(baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardErrorWithNoneExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("wizard",1)).when(this.wizardService).delete(1);

        // When and then
        this.mockMvc.perform(delete(baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.wizardService).assignArtifact(2, "123");

        // When and then
        this.mockMvc.perform(put(baseUrl + "/wizards/2/artifacts/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizard() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("wizard",2)).when(this.wizardService).assignArtifact(2, "123");

        // When and then
        this.mockMvc.perform(put(baseUrl + "/wizards/2/artifacts/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 2 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifact() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("artifact","123")).when(this.wizardService).assignArtifact(2, "123");

        // When and then
        this.mockMvc.perform(put(baseUrl + "/wizards/2/artifacts/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 123 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}