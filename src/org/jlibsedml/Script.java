/***************************************************************************************************
 * Copyright (c) 2016 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package org.jlibsedml;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Fsk-SEDML class Script to embeds or references an executable script containing
 * plotting commands to visualize the results of the simulation.
 *
 * @author Miguel de Alba
 */
public class Script extends Output {

    // MIME type of the scripting language
    private String type;

    // Whether the script is embedded or referenced. The script is defaulted to referenced.
    private boolean isEmbedded;

    // URI to external file if the script is referenced or contents of the script otherwise
    private String script;

    /**
     * @param id         A required <code>String</code> identifier for this element
     * @param name       optional, can be null
     * @param type       MIME type of the scripting language. E.g. "text/x-r"
     * @param script     URI to external file if referenced or contents of the script
     *                   otherwise
     * @param isEmbedded Boolean indicating whether the script is referenced or embedded
     */
    public Script(String id, String name, String type, String script, boolean isEmbedded) {
        super(id, name);
        this.type = type;
        this.script = script;
        this.isEmbedded = isEmbedded;
    }

    // Output

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKind() {
        return SEDMLTags.SCRIPT_KIND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllDataGeneratorReferences() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllIndependentDataGeneratorReferences() {
        return new ArrayList<>();
    }

    // SBase

    /**
     * {@inheritDoc}
     */
    @Override
    public String getElementName() {
        return SEDMLTags.OUTPUT_SCRIPT;
    }

    // Script

    /**
     * @return the MIME type of the scripting language used for the model
     */
    public String getType() {
        return type;
    }

    /**
     * @param type MIME type of the scripting language used for the model
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the contents of the script if embedded or the URI referencing to an external file
     * otherwise
     */
    public String getScript() {
        return script;
    }

    /**
     * @param script Contents of the script if embedded or the URI referencing to an external file
     *               otherwise
     */
    public void setScript(String script) {
        this.script = script;
    }

    /**
     * @return Whether the script is embedded into the Script element or referenced to an
     * external file.
     */
    public boolean isEmbedded() {
        return isEmbedded;
    }

    /**
     * @param embedded Whether the script is embedded into the Script element or referenced to an
     *                 external file.
     */
    public void setEmbedded(boolean embedded) {
        isEmbedded = embedded;
    }
}

