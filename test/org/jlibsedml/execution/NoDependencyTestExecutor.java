package org.jlibsedml.execution;


import org.jlibsedml.AbstractTask;
import org.jlibsedml.Output;
import org.jlibsedml.SedML;
import org.jlibsedml.Simulation;
import org.jlibsedml.UniformTimeCourse;
import org.jlibsedml.modelsupport.KisaoOntology;
import org.jlibsedml.modelsupport.KisaoTerm;

public class NoDependencyTestExecutor extends AbstractSedmlExecutor {

	public NoDependencyTestExecutor(SedML model, Output output) {
		super(model, output);
		// TODO Auto-generated constructor stub
	}
	
	public NoDependencyTestExecutor(SedML model, AbstractTask task1) {
      super(model,task1);
    }

    @Override
	protected boolean canExecuteSimulation(Simulation sim) {
		String kisaoID = sim.getAlgorithm().getKisaoID();
		KisaoTerm wanted = KisaoOntology.getInstance().getTermById(kisaoID);
		KisaoTerm offered = KisaoOntology.getInstance().getTermById("KISAO:0000064");
		return offered.is_a(wanted);
	}

	@Override
	protected IRawSedmlSimulationResults executeSimulation(String model,
			UniformTimeCourse simConfig) {
		return null;
				
	}
	
	

	@Override
	protected boolean supportsLanguage(String language) {
		return language.contains("sbml");
	}
}

