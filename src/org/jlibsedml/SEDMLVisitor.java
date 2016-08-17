package org.jlibsedml;

/**
 * Abstract class for any visitor to extend.
 * @author radams
 *
 */
public interface  SEDMLVisitor {
    
    
     boolean visit (SedML sedml);
    
     boolean visit (Simulation sim);
    
     boolean visit (Model model);
    
     boolean visit (Task task);
     boolean visit (RepeatedTask repeatedTask);
    
     boolean visit (AddXML change);
    
     boolean visit (RemoveXML change);
    
     boolean visit (ChangeXML change);
    
     boolean visit (ChangeAttribute change);
    
     boolean visit (ComputeChange change);
    
     boolean visit(SetValue setValue) ;

     boolean visit (DataGenerator dg);
    
     boolean visit (Variable var);
    
     boolean visit (Parameter model);
    
     boolean visit (Output output);

     boolean visit(Algorithm algorithm);

     boolean visit(Curve curve) ;

     boolean visit(DataSet dataSet) ;

     boolean visit(Surface surface) ;

     boolean visit(UniformRange uniformRange) ;
     boolean visit(VectorRange vectorRange) ;
     boolean visit(FunctionalRange functionalRange) ;

}
