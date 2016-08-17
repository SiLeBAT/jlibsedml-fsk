package org.jlibsedml;

import static org.junit.Assert.assertEquals;

import org.jlibsedml.UniformRange.UniformType;
import org.junit.Test;

public class UniformRangeTest {

    @Test
    public void testEqualsObject() {
        UniformRange range = new UniformRange("any", 0, 100, 101);
        UniformRange range2 = new UniformRange("any", 0, 100, 101);
        assertEquals( range, range2);
        assertEquals(range.hashCode(), range2.hashCode());
    }

    @Test
    public void testGetElementAtLinear() {
        UniformRange range = new UniformRange("any", 0, 100, 101);
        assertEquals (50.0, range.getElementAt(50), 0.001);
        
        range = new UniformRange("any", 100, 200, 101);
        assertEquals (150.0, range.getElementAt(50), 0.001);
        
        range = new UniformRange("any", 100, 500, 201);
        assertEquals (100.0, range.getElementAt(0), 0.001);
        assertEquals (102.0, range.getElementAt(1), 0.001);
        assertEquals (300.0, range.getElementAt(100), 0.001);
        assertEquals (498.0, range.getElementAt(199), 0.001);
        assertEquals (500.0, range.getElementAt(200), 0.001);        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetElementAtValidateIndex() {      
        UniformRange range = createAnyLinearRange();   
        assertEquals (500.0, range.getElementAt(202), 0.001);// too big   
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetElementAtValidateIndexLessThanZero() {      
        UniformRange range = createAnyLinearRange();   
        assertEquals (500.0, range.getElementAt(-1), 0.001);// too big   
    }
    
    @Test
    public void testGetElementAtLog() {
        UniformRange  logRange = new UniformRange("any", 1, 100, 101,UniformType.LOG); 
        assertEquals(100d, logRange.getElementAt(100),0.001);
        assertEquals(10d, logRange.getElementAt(50),0.001);
        assertEquals(1d, logRange.getElementAt(0),0.001);
        // 10e10 range, split over 100 points
        logRange = new UniformRange("any", 0.00001, 100000, 101,UniformType.LOG); 
        assertEquals(100d, logRange.getElementAt(70),0.001);
        assertEquals(10d, logRange.getElementAt(60),0.001);
        assertEquals(1d, logRange.getElementAt(50),0.001);      
    }

    private UniformRange createAnyLinearRange() {
        UniformRange  range = new UniformRange("any", 100, 500, 201);
        return range;
    }
}
