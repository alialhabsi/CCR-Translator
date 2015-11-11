package my.pphr.toCCRtrans;

import java.util.Collections;

public class PPHR_CCR_Translate {

	public PPHR_CCR_Translate() {
		// TODO Auto-generated constructor stub
	}
	
	public static String translatePPHR(String inPPHR) {
		String outCCR = " ";
		if ((inPPHR == null) || (inPPHR.length() < 20)) {
			System.err.println("translatePPHR input too short" + inPPHR);
			return "null";
		}
		//System.out.println("\nTranslate input: " + inPPHR.substring(0, 20));
		String[] entries = inPPHR.split("\n");
		System.out.println("Translate numEntries=" + entries.length);
		// clear the output CCR list
		PPHRA.CCRoutList.clear();
		/* This loop gets each entry in the PPHR message,
		 *   parses it,
		 *   splits the Content part into separate variables,
		 *   loops through the List of variables
		 *       finds the matching CCR element for each
		 *       inserts the PPHR var name and value into CCR elem,
		 *       adds CCR String to PPHRA.CCRelemList */
		for (int i1 = 0; i1 < entries.length; i1++) {
			if ((entries[i1] == null) || (entries[i1].length() < 20)) continue;
			//System.out.println("Translate entry num " + i1 + " " + entries[i1]);
			
			PPHRparser.parseOneEntry(entries[i1]); //stores entry parts in PPHRA.
			PPHRparser.parseEntryContent();  //stores entry content variables in PPHRA.
			PPHRparser.matchPPHRvarsToCCR(); //stores CCR output in PPHRA.CCRoutList
		}
		
		// Sort PPHRA.CCRoutList alphabetically
		Collections.sort(PPHRA.CCRoutList);
		// Next remove empty tags from each String in PPHRA.CCRoutList
		CCRoutput.removeEmptyTags();
		/* Next remove matching end-start tags from neighboring elements
		 *     in PPHRA.CCRoutList  */
		CCRoutput.combineElems();
		/* Next combine the elements in PPHRA.CCRelemList into single String*/
		outCCR = CCRoutput.makeCCRoutString();
		System.out.println("OUT: " + outCCR);
		return outCCR;
	}

} // END class PPHR_CCR_Translate
