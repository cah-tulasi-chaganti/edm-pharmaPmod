package com.cardinalhealth.aem.segmentupdate;

import java.io.IOException;

/**
 * A simple service interface
 */
public interface AEMSegmentUpdateService {
    
    /**
     * @return the name of the underlying JCR repository implementation
     * @throws IOException 
     */
    public String updateAEMSegments() throws IOException;
    public String updateContent() throws IOException;
    public String getAEMSegmentsORContent(String aemType, String traitType) throws IOException;


}