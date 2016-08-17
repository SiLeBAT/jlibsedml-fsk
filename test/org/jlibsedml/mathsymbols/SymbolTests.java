package org.jlibsedml.mathsymbols;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jlibsedml.Libsedml;
import org.jmathml.ASTCi;
import org.jmathml.ASTNode;
import org.jmathml.ASTNumber;
import org.jmathml.ASTSymbol;
import org.jmathml.ASTSymbolFactory;
import org.jmathml.ASTToXMLElementVisitor;
import org.jmathml.EvaluationContext;
import org.jmathml.FormulaFormatter;
import org.jmathml.IEvaluationContext;
import org.jmathml.SymbolRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbolTests {
    Logger log = LoggerFactory.getLogger(SymbolTests.class);
    final double MIN =-100d;
    final double MAX = 1000d;
    final ASTCi var = new ASTCi("x");
    List<Double> results = Arrays.asList(new Double[]{2d,6d,1d,9.4, MAX, MIN});
    final Double SUM=918.4;
    final Double PRODUCT=-1.128e7;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSumSymbol(){
		ASTSymbol sumSym = new SedMLSymbolFactory().createSymbol("sum");
		sumSym.addChildNode(var);
		
	    EvaluationContext ec = createContext();
	    assertEquals(SUM, sumSym.evaluate(ec).getValue(),0.001);		
		
	}
	
	@Test
	public void testSumSymbolToFromString(){
		SymbolRegistry.getInstance().addSymbolFactory(new SedMLSymbolFactory());
		ASTNode root = Libsedml.parseFormulaString("sum(x)");
		String symbol = new FormulaFormatter().formulaToString(root);
		assertEquals("sum(x)", symbol);
	
	}
	@Test
	public void testMaxSymbol(){
		ASTSymbol sumSym = new SedMLSymbolFactory().createSymbol("max");
		sumSym.addChildNode(var);
		
	    EvaluationContext ec = createContext();
	    assertEquals(MAX, sumSym.evaluate(ec).getValue(),0.01);		
	}
	
	@Test
	public void testMinSymbol(){
		ASTSymbol sumSym = new SedMLSymbolFactory().createSymbol("min");
		sumSym.addChildNode(var);
		
	    EvaluationContext ec = createContext();
	    assertEquals(MIN, sumSym.evaluate(ec).getValue(),0.01);		
	}
	
	class MeanSymbol extends SedMLSymbol {
        public MeanSymbol(String id) {
            super(id);
            setEncoding("text");
            setDefinitionURL("http://someIdentifer.org/#mean");
        }
        // calculates the mean
        @Override
        protected ASTNumber doEvaluate(IEvaluationContext context) {       
            double rc = 0;
            int n = 0;
            for (Double val : context.getValueFor(firstChild().getName())) {
                rc += val;
                n++;
            }
            if(n == 0) {
                throw new IllegalStateException("Can't calculate mean - there are no elements");
            }
            return ASTNumber.createNumber(rc / n);
        }	    
	}
	
	@Test
    public void testExtensionSymbolMean(){
	    // basic evaluation of funciton:
        ASTSymbol meanSym = new MeanSymbol("mean");
        meanSym.addChildNode(var);
        EvaluationContext ec = createContext();
        assertEquals(153.06, meanSym.evaluate(ec).getValue(),0.01);
        // register a symbol factory that knows how to interpret 'mean'
        SymbolRegistry.getInstance().addSymbolFactory( new ASTSymbolFactory("test") {          
            @Override
            protected ASTSymbol createSymbol(String name) {
                if (name.contains("mean")) {
                    return new MeanSymbol("mean");
                } else {
                    return null;
                }              
            }
            
            @Override
            protected boolean canCreateSymbol(String urlEncoding) {
             return createSymbol(urlEncoding) != null;
            }
        });
        
        ASTNode root = Libsedml.parseFormulaString("mean(x)");
        
        assertEquals("mean", root.firstChild().getName());
        String xml = getXmlFromASTNode (root);
        System.err.println(xml);
          
    }
	
	private String getXmlFromASTNode(ASTNode root) {
	    ASTToXMLElementVisitor visitor = new ASTToXMLElementVisitor();
	    root.accept(visitor);
	    Element element = visitor.getElement();
	    return getElementString(element);
    }

    String getElementString(org.jdom.Element node) {
        Document mathDoc = new Document();
        mathDoc.setRootElement(node);
        String xmlString = xmlToString(mathDoc, false);
        return xmlString;
    }

    String xmlToString(Document xmlDoc, boolean bTrimAllWhiteSpace) {
        XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
        return xmlOut.outputString(xmlDoc);
    }
	@Test
	public void testProductSymbol(){
		ASTSymbol sumSym = new SedMLSymbolFactory().createSymbol("product");
		sumSym.addChildNode(var);
		
	    EvaluationContext ec = createContext();
	    assertEquals(PRODUCT, sumSym.evaluate(ec).getValue(),0.1);		
		
	}
	
	@Test
	public void testGetNormalise(){
		ASTNode node = Libsedml.parseFormulaString("(x - min(x)) / (max(x) -min(x))");
		ASTToXMLElementVisitor vis = new ASTToXMLElementVisitor();
		node.accept(vis);
		String normaliseMathml = new XMLOutputter().outputString(vis.getElement());
		log.info("Normalise as Math ML is:\n {}", normaliseMathml);
	}

	private EvaluationContext createContext() {
	    EvaluationContext ec = new EvaluationContext();
	    ec.setValueFor("x", results);
		return ec;
	}
}
