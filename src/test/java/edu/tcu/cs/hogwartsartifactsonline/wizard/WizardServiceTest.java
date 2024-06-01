package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;


    List<Wizard> wizards = new ArrayList<>();


    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(wizardRepository.findAll()).willReturn(wizards);
        // When
        List<Wizard> result = wizardService.findAll();
        // Then
        assertThat(result).hasSize(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setName("Duy Potter");

        given(wizardRepository.save(wizard)).willReturn(wizard);
        // When
        Wizard result = wizardService.save(wizard);
        // Then
        assertThat(result.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository, times(1)).save(wizard);
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");
        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));

        // When
        Wizard result = wizardService.findById(1);

        // Then
        assertThat(result.getName()).isEqualTo(wizard.getName());
        verify(wizardRepository, times(1)).findById(1);

    }

    @Test
    void testFindByFail() {
        //
        given(wizardRepository.findById(Mockito.any(Integer.class)))
                .willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.findById(1);
        });

        // Then
        verify(wizardRepository, times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Harry Potter");

        given(wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Wizard result = wizardService.update(1, update);
        // Then
        assertThat(result.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateFail() {
        // Given
        Wizard update = new Wizard();
        update.setName("Harry Duy");
        given(wizardRepository.findById(Mockito.any(Integer.class)))
                .willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.update(1, update);
        });

        // Then
        verify(wizardRepository, times(1)).findById(Mockito.any(Integer.class));
    }

    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1);

        // When
        wizardService.delete(1);

        // Then
        verify(wizardRepository, times(1)).findById(1);
        verify(wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.delete(1);
        });

        // Then
        verify(wizardRepository, times(1)).findById(1);
    }
}