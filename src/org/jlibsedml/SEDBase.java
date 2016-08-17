package org.jlibsedml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Base class of SEDML elements that can be annotated or contain notes.
 *
 * @author anu/radams
 */
public abstract class SEDBase {

    private String metaId = null;

    private Annotation annotation = null;

    private Notes note;

    public Notes getNote() {
        return note;
    }

    /**
     * Setter for a {@link Notes}.
     *
     * @param note Can be null, if the Notes are to be removed from this object.
     */
    public void setNote(Notes note) {
        this.note = note;
    }

    /**
     * Getter for the metaid attribute of this element.
     *
     * @return A <code>String</code> of the meta_id attribute, or an empty <code>String</code> if not set.
     */
    public String getMetaId() {
        return metaId;
    }

    /**
     * Setter for the metaid attribute of this element.
     *
     * @param metaId A non-null <code>String</code> of the meta_id attribute.
     */
    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }

    /**
     * Gets a read-only view of the Notes for this element.
     *
     * @return An unmodifiable <code>List</code> of <code>Notes</code>.
     * @deprecated Use getNote() from now on.
     */
    public List<Notes> getNotes() {
        if (note == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(Arrays.asList(new Notes[]{note}));
        }

    }

    /**
     * Adds a <code>Note</code>, if this note does not already exist.
     *
     * @param note A non-null {@link Notes}
     * @return <code>true</code> if <code>note</code> added, <code>false</code> otherwise.
     * @deprecated From now on, setNote(Note note) should be used.
     */
    public boolean addNote(Notes note) {
        this.note = note;
        return false;
    }

    /**
     * Removes a <code>Note</code> from this element's list of {@link Notes} objects.
     *
     * @param note A non-null {@link Notes}
     * @return <code>true</code> if <code>note</code> removed, <code>false</code> otherwise.
     * @deprecated Use setNote(null) from now on, this method will have the same effect
     */
    public boolean removeNote(Notes note) {
        this.note = null;
        return true;
    }

    /**
     * Directly sets a list of Notes into this element.
     *
     * @param notes A non-null <code>List</code> of {@link Notes} objects.
     * @throws IllegalArgumentException if <code>notes</code> is <code>null</code>.
     * @deprecated Use setNote(Note n) from now on - this method will now just set the 1st element of the list.
     */
    public void setNotes(List<Notes> notes) {
        Assert.checkNoNullArgs(notes);
        if (!notes.isEmpty()) {
            this.note = notes.get(0);
        }
    }

    /**
     * Gets a read-only view of the {@link Annotation}s for this element.
     *
     * @return An unmodifiable <code>List</code> of <code>Annotation</code>.
     */
    public Annotation getAnnotation() {
        return annotation;
    }

    /**
     * Adds a <code>Annotation</code>, if this annotation does not already exist in this element's annotations.
     * This is deprecated, use setAnnotation (Annotation) from now on.
     *
     * @param ann A non-null {@link Annotation}
     * @return <code>true</code> if <code>ann</code> added, <code>false</code> otherwise.
     * @deprecated
     */
    public boolean addAnnotation(Annotation ann) {
        setAnnotation(ann);
        return false;
    }

    /**
     * Removes a <code>Annotation</code> from this element's list of {@link Annotation} objects.
     *
     * @param ann A non-null {@link Annotation}
     * @return <code>true</code> if <code>ann</code> removed, <code>false</code> otherwise.
     * @deprecated Usr setAnnotation(null) from now on
     */
    public boolean removeAnnotation(Annotation ann) {
        this.annotation = null;
        return false;
    }

    /**
     * Directly sets a list of Annotations into this element.
     *
     * @param annotations A non-null <code>List</code> of {@link Annotation} objects.
     * @throws IllegalArgumentException if <code>annotations</code> is <code>null</code>.
     * @deprecated Use setAnnotation (annotation) - collections of {@link Annotation} are no longer supported.
     */
    public void setAnnotation(List<Annotation> annotations) {
        if (!annotations.isEmpty()) {
            this.annotation = annotations.get(0);
        }
    }

    /**
     * Sets an an annotation for the given SEDML Element. Can be <code>null</code> if removing an {@link Annotation}.
     *
     * @param annotation
     * @since 2.3
     */
    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Provides a link between the object model and the XML element names
     *
     * @return A non-null <code>String</code> of the XML element name of the object.
     */
    public abstract String getElementName();

    public abstract boolean accept(SEDMLVisitor visitor);

}
