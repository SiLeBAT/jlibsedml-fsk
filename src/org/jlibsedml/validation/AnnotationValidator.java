package org.jlibsedml.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jlibsedml.AddXML;
import org.jlibsedml.Algorithm;
import org.jlibsedml.Annotation;
import org.jlibsedml.ChangeAttribute;
import org.jlibsedml.ChangeXML;
import org.jlibsedml.ComputeChange;
import org.jlibsedml.Curve;
import org.jlibsedml.DataGenerator;
import org.jlibsedml.DataSet;
import org.jlibsedml.FunctionalRange;
import org.jlibsedml.IIdentifiable;
import org.jlibsedml.Model;
import org.jlibsedml.Output;
import org.jlibsedml.Parameter;
import org.jlibsedml.RemoveXML;
import org.jlibsedml.RepeatedTask;
import org.jlibsedml.SEDBase;
import org.jlibsedml.SEDMLVisitor;
import org.jlibsedml.SedML;
import org.jlibsedml.SedMLError;
import org.jlibsedml.SedMLError.ERROR_SEVERITY;
import org.jlibsedml.SetValue;
import org.jlibsedml.Simulation;
import org.jlibsedml.Surface;
import org.jlibsedml.Task;
import org.jlibsedml.UniformRange;
import org.jlibsedml.Variable;
import org.jlibsedml.VectorRange;
import org.jlibsedml.XMLException;
//TODO finish this
public class AnnotationValidator extends AbstractDocumentValidator implements SEDMLVisitor {

 
    private SedML sedml;
    private List<SedMLError> errors = new ArrayList<>();

    public AnnotationValidator(SedML sedml, Document doc) {
        super(doc);
        this.sedml = sedml;
    }

    @Override
    public List<SedMLError> validate() throws XMLException {
       sedml.accept(this);
       return errors;
    }
    
    private void validateAnnotation(SEDBase base) {
        Annotation annotation = base.getAnnotation();
        if (annotation == null) {
            return;
        }
        List<Element> topLevelElements = annotation.getAnnotationElementsList();
        Set<Namespace> seen = new HashSet<>();
        for (Element el: topLevelElements) {
            if(el.getNamespace() == null) {
                addError(base, "No namespace for annotation element");
            } else {
                if(seen.contains(el.getNamespace())) {
                    addError(base, "Namespace " + el.getNamespace()+ " is not unique in this annotation.");
                } else {
                    seen.add(el.getNamespace());
                }
            }
        }
    }

    private void addError(SEDBase base, String msg) {
        if(base instanceof IIdentifiable) {
        errors.add(new SedMLError(getLineNumberOfError(base.getElementName(), (IIdentifiable)base),
                     msg, ERROR_SEVERITY.ERROR));               
        } else {
            errors.add(new SedMLError(1,
                    msg, ERROR_SEVERITY.ERROR));                 
        }
    }

    @Override
    public boolean visit(SedML sedml) {
       validateAnnotation(sedml);
       return true;
    }

    @Override
    public boolean visit(Simulation sim) {
        validateAnnotation(sim);
        return true;
    }

    @Override
    public boolean visit(Model model) {
        validateAnnotation(model);
        return true;
    }

    @Override
    public boolean visit(Task task) {
        validateAnnotation(task);
        return true;
    }

    @Override
    public boolean visit(RepeatedTask repeatedTask) {
        validateAnnotation(repeatedTask);
        return true;
    }

    @Override
    public boolean visit(AddXML change) {
        validateAnnotation(change);
        return true;
    }

    @Override
    public boolean visit(RemoveXML change) {
        validateAnnotation(change);
        return true;
    }

    @Override
    public boolean visit(ChangeXML change) {
        validateAnnotation(change);
        return true;
    }

    @Override
    public boolean visit(ChangeAttribute change) {
        validateAnnotation(change);
        return true;
    }

    @Override
    public boolean visit(ComputeChange change) {
        validateAnnotation(change);
        return true;
    }

    @Override
    public boolean visit(SetValue setValue) {
        validateAnnotation(setValue);
        return true;
    }

    @Override
    public boolean visit(DataGenerator dg) {
        validateAnnotation(dg);
        return true;
    }

    @Override
    public boolean visit(Variable var) {
        validateAnnotation(var);
        return true;
    }

    @Override
    public boolean visit(Parameter param) {
        validateAnnotation(param);
        return true;
    }

    @Override
    public boolean visit(Output output) {
        validateAnnotation(output);
        return true;
    }

    @Override
    public boolean visit(Algorithm algorithm) {
        validateAnnotation(algorithm);
        return true;
    }

    @Override
    public boolean visit(Curve curve) {
        validateAnnotation(curve);
        return true;
    }

    @Override
    public boolean visit(DataSet dataSet) {
        validateAnnotation(dataSet);
        return true;
    }

    @Override
    public boolean visit(Surface surface) {
        validateAnnotation(surface);
        return true;
    }

    @Override
    public boolean visit(UniformRange uniformRange) {
        validateAnnotation(uniformRange);
        return true;
    }

    @Override
    public boolean visit(VectorRange vectorRange) {
        validateAnnotation(vectorRange);
        return true;
    }

    @Override
    public boolean visit(FunctionalRange functionalRange) {
        // TODO Auto-generated method stub
        return false;
    }

}
