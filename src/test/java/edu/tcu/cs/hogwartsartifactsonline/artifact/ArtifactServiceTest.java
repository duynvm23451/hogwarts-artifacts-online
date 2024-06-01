package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // 1 Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl",
         */
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        Wizard wizard = new Wizard();
        wizard.setId(2);
        wizard.setName("Harry Potter");

        artifact.setOwner(wizard);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        // Define the behavior of Mock object

        // 2 When. Action on the target behavior. When steps should cover the method to be tested
        Artifact returnedArtifact = artifactService.findById("1250808601744904192");

        // 3 Then. Assert expected outcomes
        assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());
        verify(artifactRepository, times(1)).findById("1250808601744904192");

    }

    @Test
    void testFindByIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class)))
                .willReturn(Optional.empty());

        //When
        Throwable throwable = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        //THen
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id 1250808601744904192 :(");
        verify(artifactRepository, times(1)).findById("1250808601744904192");

    }

    @Test
    void testFindAllSuccess() {
        // given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        // when
        List<Artifact> actualArtifacts = artifactService.findAll();

        // then
        assertThat(actualArtifacts).hasSize(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveASuccess() {
        // given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact Name");
        newArtifact.setDescription("Artifact Description");
        newArtifact.setImageUrl("Image Url");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        // when
        Artifact saveArtifact =artifactService.save(newArtifact);

        // then
        assertThat(saveArtifact.getId()).isEqualTo("123456");
        assertThat(saveArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(saveArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(saveArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess() {
        // given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        // when
        Artifact returnedArtifact = artifactService.update("1250808601744904192", update);

        // then
        assertThat(returnedArtifact.getId()).isEqualTo(update.getId());
        assertThat(returnedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
        verify(artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, ()-> {
            artifactService.update("1250808601744904192", update);
        });

        // Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");

    }

    @Test
    void testDeleteSuccess() {
        // Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904192");

        // When
        artifactService.delete(artifact.getId());

        // Then
        verify(artifactRepository, times(1)).deleteById(artifact.getId());
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            artifactService.delete("1250808601744904192");
        });

        // Then
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }
}