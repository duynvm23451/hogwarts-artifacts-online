package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {
    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String id) {
        return this.artifactRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("artifact",id)
        );
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact artifact) {
        artifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(artifact);
    }

    public Artifact update(String artifactId,Artifact artifact) {
        Artifact oldArtifact = this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));
        oldArtifact.setName(artifact.getName());
        oldArtifact.setDescription(artifact.getDescription());
        oldArtifact.setImageUrl(artifact.getImageUrl());

        return this.artifactRepository.save(oldArtifact);
    }

    public void delete(String artifactId) {
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));
        this.artifactRepository.deleteById(artifactId);
    }
}
