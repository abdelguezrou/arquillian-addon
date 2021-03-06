package org.jboss.forge.arquillian.api.core;

import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.DependencyResolver;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyQueryBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.facets.DependencyFacet;
import org.jboss.forge.arquillian.util.DependencyUtil;

import javax.inject.Inject;
import java.util.List;

public abstract class AbstractVersionedFacet extends AbstractFacet<Project> implements ProjectFacet {

    @Inject
    private DependencyResolver resolver;

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean install(String version) {
        setVersion(version);
        return install();
    }

    public String getDefaultVersion() {
        return DependencyUtil.getLatestNonSnapshotVersion(getAvailableVersions());
    }

    public List<String> getAvailableVersions() {
        return DependencyUtil.toVersionString(
            resolver.resolveVersions(
                DependencyQueryBuilder.create(getVersionedCoordinate())));
    }

    protected abstract Coordinate getVersionedCoordinate();

    protected boolean hasEffectiveDependency(DependencyBuilder dependency) {
        final DependencyFacet dependencyFacet = getFaceted().getFacet(DependencyFacet.class);
        return dependencyFacet.hasEffectiveDependency(dependency);
    }

    protected String wrap(String versionPropertyName) {
        return "${" + versionPropertyName + "}";
    }
}
