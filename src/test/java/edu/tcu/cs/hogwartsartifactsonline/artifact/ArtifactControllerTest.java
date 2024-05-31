package edu.tcu.cs.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<Artifact>();

        Artifact artifact1 = new Artifact();
        artifact1.setId("1");
        artifact1.setName("artifact1");
        artifact1.setDescription("artifact1 description");
        artifact1.setImageUrl("artifact1 image url");
        artifacts.add(artifact1);

        Artifact artifact2 = new Artifact();
        artifact2.setId("2");
        artifact2.setName("artifact2");
        artifact2.setDescription("artifact2 description");
        artifact2.setImageUrl("artifact2 image url");
        artifacts.add(artifact2);

        Artifact artifact3 = new Artifact();
        artifact3.setId("3");
        artifact3.setName("artifact3");
        artifact3.setDescription("artifact3 description");
        artifact3.setImageUrl("artifact3 image url");
        artifacts.add(artifact3);

        Artifact artifact4 = new Artifact();
        artifact4.setId("4");
        artifact4.setName("artifact4");
        artifact4.setDescription("artifact4 description");
        artifact4.setImageUrl("artifact4 image url");
        artifacts.add(artifact4);

        Artifact artifact5 = new Artifact();
        artifact5.setId("5");
        artifact5.setName("artifact5");
        artifact5.setDescription("artifact5 description");
        artifact5.setImageUrl("artifact5 image url");
        artifacts.add(artifact5);

        Artifact artifact6 = new Artifact();
        artifact6.setId("6");
        artifact6.setName("artifact6");
        artifact6.setDescription("artifact6 description");
        artifact6.setImageUrl("artifact6 image url");
        artifacts.add(artifact6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //Given
        given(this.artifactService.findById("1")).willReturn(this.artifacts.get(0));

        //When and then
        this.mockMvc.perform(get("/api/v1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("artifact1"));

    }

    @Test
     void testFindArtifactByIdFail() throws Exception {
        //given
        given(this.artifactService.findById("1")).willThrow(new ArtifactNotFoundException("1"));

        // when and then
        this.mockMvc.perform(get("/api/v1/artifacts/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // given
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        // when and then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("artifact1"))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("artifact2"));

    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        // given
        ArtifactDto artifactDto = new ArtifactDto(
                null,
                "Remembrall",
                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("1250808601744904197");
        savedArtifact.setName("Remembrall");
        savedArtifact.setDescription("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        savedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        // when and then
        this.mockMvc.perform(post("/api/v1/artifacts")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));

    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904197",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904197");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("A new description.");
        updatedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq("1250808601744904197"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);

        // when and then
        this.mockMvc.perform(put("/api/v1/artifacts/1250808601744904197")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904197"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactErrorWithNonexistentId() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(
                "1250808601744904197",
                "Invisibility Cloak",
                "A new description.",
                "ImageUrl",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        given(this.artifactService.update(eq("1250808601744904197"), Mockito.any(Artifact.class)))
                .willThrow(new ArtifactNotFoundException("1250808601744904197"));

        // when and then
        this.mockMvc.perform(put("/api/v1/artifacts/1250808601744904197")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 1250808601744904197 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.artifactService).delete("1250808601744904191");

        // When and then
        this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNoneExistentId() throws Exception {
        // Given
        doThrow(new ArtifactNotFoundException("1250808601744904191")).when(this.artifactService).delete("1250808601744904191");

        // When and then
        this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 1250808601744904191 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}