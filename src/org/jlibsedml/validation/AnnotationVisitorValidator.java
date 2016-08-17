package org.jlibsedml.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
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
import org.jlibsedml.Model;
import org.jlibsedml.Output;
import org.jlibsedml.Parameter;
import org.jlibsedml.RemoveXML;
import org.jlibsedml.RepeatedTask;
import org.jlibsedml.SEDBase;
import org.jlibsedml.SEDMLVisitor;
import org.jlibsedml.SedML;
import org.jlibsedml.SedMLError;
import org.jlibsedml.SetValue;
import org.jlibsedml.Simulation;
import org.jlibsedml.Surface;
import org.jlibsedml.Task;
import org.jlibsedml.UniformRange;
import org.jlibsedml.Variable;
import org.jlibsedml.VectorRange;
import org.jdom.Namespace;

public class AnnotationVisitorValidator implements SEDMLVisitor {

    private List<SedMLError> errors = new ArrayList<>();
    public AnnotationVisitorValidator(List<SedMLError> errors) {
       this.errors = errors;
    }

    @Override
    public boolean visit(SedML sedml) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Simulation sim) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Model model) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Task task) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(RepeatedTask repeatedTask) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(AddXML change) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(RemoveXML change) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(ChangeXML change) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(ChangeAttribute change) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(ComputeChange change) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(SetValue setValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(DataGenerator dg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Variable var) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Parameter model) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Output output) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Algorithm algorithm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Curve curve) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(DataSet dataSet) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(Surface surface) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(UniformRange uniformRange) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(VectorRange vectorRange) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean visit(FunctionalRange functionalRange) {
        // TODO Auto-generated method stub
        return false;
    }
    


}
