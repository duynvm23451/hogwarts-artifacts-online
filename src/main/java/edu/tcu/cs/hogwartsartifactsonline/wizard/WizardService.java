package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    public List<Wizard> findAll() {
        return  wizardRepository.findAll();
    }

    public Wizard save(Wizard wizard) {
        return wizardRepository.save(wizard);
    }

    public Wizard findById(int id) {
        return wizardRepository.findById(id).orElseThrow(() ->
            new ObjectNotFoundException("wizard",id)
        );
    }

    public Wizard update(int id, Wizard wizard) {
        Wizard wizardToUpdate = wizardRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("wizard",id)
        );
        wizardToUpdate.setName(wizard.getName());
        return wizardRepository.save(wizardToUpdate);
    }

    public void delete(int id) {
        Wizard wizard = wizardRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("wizard",id));

        wizard.removeAllArtifacts();
        wizardRepository.deleteById(id);
    }
}
