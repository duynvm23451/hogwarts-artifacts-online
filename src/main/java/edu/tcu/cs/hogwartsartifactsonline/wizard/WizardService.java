package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
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

    public void assignArtifact(int wizardId, String artifactId) {
        Artifact artifact = artifactRepository.findById(artifactId).orElseThrow(
                () -> new ObjectNotFoundException("artifact",artifactId)
        );

        Wizard wizard = wizardRepository.findById(wizardId).orElseThrow(
                () -> new ObjectNotFoundException("wizard",wizardId)
        );

//        if (artifact.getOwner() != null) {
//            Wizard currentOwner = artifact.getOwner();
//            currentOwner.removeArtifact(artifact);
//        }

        wizard.addArtifact(artifact);
        wizardRepository.save(wizard);

    }
}
