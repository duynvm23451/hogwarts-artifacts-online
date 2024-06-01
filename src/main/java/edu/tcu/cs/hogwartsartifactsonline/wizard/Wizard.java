package edu.tcu.cs.hogwartsartifactsonline.wizard;


import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artifact> artifacts = new ArrayList<Artifact>();

    public Wizard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact) {
        artifact.setOwner(this);
        this.artifacts.add(artifact);

    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }
}