package org.jlibsedml;

/**
 * Encapsulates a FskSimulation of a model. It can be of three types:
 * <ul>
 * <li>deterministic</li>
 * <li>statistic</li>
 * <li>probabilistic</li>
 * </ul>
 *
 * @author Miguel de Alba
 */
public class FskSimulation extends Simulation {

    private enum Type {deterministic, statistic, probabilistic}

    private String language;
    private Type type;

    /**
     * @param id        A required <code>String</code> identifier for this element
     * @param name      Optional, can be null
     * @param language, Optional, can be null
     * @param type      A required <code>String</code> with the simulation type
     */
    public FskSimulation(String id, String name, String language, String type) {
        super(id, name, new Algorithm("_"));
        this.language = language;
        this.type = Type.valueOf(type);
    }

    @Override
    public String toString() {
        return "FskSimulation [id=" + getId() + ", name=" + getName() + ", language=" + language + ", type=" + type +
                "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSimulationKind() {
        return SEDMLTags.SIMUL_FSK_KIND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElementName() {
        return SEDMLTags.SIM_FSK;
    }

    // FskSimulation

    /**
     * @return the language of the scripting language used for the model
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language language of the scripting language used for the model
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the type of the simulation: 'deterministic", 'statistic' or 'probabilistic'
     */
    public String getType() {
        return type.name();
    }

    /**
     * @param type of the simulation: 'deterministic', 'statistic' or 'probabilistic'
     */
    public void setType(String type) {
        this.type = Type.valueOf(type);
    }
}
