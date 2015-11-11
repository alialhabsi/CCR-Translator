package my.pphr.toCCRtrans;
/** public class PPHRparser extends PPHRA
 * - methods to parse a PPHR message or entry
 * */
public class PPHRparser extends PPHRA {
	public static String[] params = {"20120101"}; // dummy declarations
    public static String[] values = {"4.5"};
    
	/** PPHRparser() Constructor */
	public PPHRparser() {
		
	} // End PPHRparser Constructor

	/** public void parseOneEntry(String entry)
	 * splits one entry (line) of PPHR into its components
	 * and stores them in the PPHRA variables 
	 * @param String entry - the line or entry to parse */
	public static void parseOneEntry(String entry)
	{
		int fromP = PPHRA.dateTimeLgt;
		String entry2 = new String(entry);
		if (entry2.indexOf(' ') < PPHRA.dateTimeLgt) {
			//process ID etc
			PPHRA.entPid = entry2.substring(0, entry2.indexOf(' '));
			entry2 = entry2.substring(entry2.indexOf(' ')).trim();
		}
		
		{
			
			PPHRA.entDateTime = entry2.substring(0, fromP);
			PPHRA.entLang = entry2.substring(fromP, fromP + PPHRA.langCodeLgt);
			fromP += PPHRA.langCodeLgt;
			PPHRA.entSent = entry2.substring(fromP, fromP + 1);
			fromP += 1;
			PPHRA.entType = entry2.substring(fromP, fromP + 1);
			PPHRA.entContent = entry2.substring(fromP + 1).trim();
		}
		System.out.printf("\nParse: Entry %s %s %s %s\n", PPHRA.entDateTime, PPHRA.entLang,PPHRA.entSent,PPHRA.entType);
		
	} // End parseOneEntry()
	
	/** parseEntryContent()
	 * Input: the content of one entry, in PPHRA.entContent
	 * splits the content into individual parameters and values,
	 *  like the PPHRandroid Chart module
	 *  Params are separated from values by '=' signs
	 *  Param-value pairs are separated by ';' signs
	 * */
	public static void parseEntryContent() {
		
	    int numEquals = 0, nextEqual = 0, iValue = 0;
	    int lastGT = 0, lastSemi = 0, lastEqual = 0;
        //String[] tempSplit;
        //String tempRangeWd = "";
        // count number of = signs in PPHRA.entContent
    	nextEqual = 0;
    	while ((nextEqual = PPHRA.entContent.indexOf('=', nextEqual + 1)) > -1) 
    		numEquals++;
    	params = new String[numEquals];
    	values = new String[numEquals];
    	// remove last ; from PPHRA.entContent
    	iValue = params.length - 1;
    	params[iValue] = PPHRA.entContent.substring(0, PPHRA.entContent.lastIndexOf(';')).trim();
    	// the value comes after the last = sign
    	values[iValue] = params[iValue].substring(params[iValue].lastIndexOf('=') + 1).trim();
    	params[iValue] = params[iValue].substring(0, params[iValue].lastIndexOf('=')).trim();
    	System.out.println("p[" + (iValue) + "]=" + params[iValue] + " val=" + values[iValue]);
    	/* TWO: duplicate each params[] String for number of = signs in it
    	 * 	i.e. because there can be multiple values in one entry,
    	 * 	make number of copies = number of values.
    	 * Copy each into values[]. As we copy, remove the last value from each String.
    	 * Thus we have 1 values[] element for each value in original text
    	 * like  ... param1 = value1
    	 *       ... param1 = value1; param2 = value2
    	 *       ... param1 = value1; param2 = value2; param3 = value3 */
    	for (int i = params.length - 1; i > 0; i--) {
    		lastSemi = params[i].lastIndexOf(';');
    		if (lastSemi > 0) 
    			params[i - 1] = params[i].substring(0, lastSemi).trim();
    		else params[i - 1] = new String(params[i]);
    		lastEqual = params[i - 1].lastIndexOf('=');
    		if (lastEqual > 0) 
    			values[i - 1] = params[i - 1].substring(lastEqual + 1).trim();
    		else values[i - 1] = new String(values[i]);
    		params[i - 1] = params[i - 1].substring(0, params[i - 1].lastIndexOf('=')).trim();
     		if (i < 2) System.out.println("p[" + (i-1) + "]=" + params[i-1] + " val=" + values[i-1]);
    	} // end for i loop to fill params[], values[]
    	// Next remove intermediate param-values from params[] strings. This need to be done now
    	for (int i = 1; i < params.length; i++) {
    		lastGT = params[i].lastIndexOf('>');
    		lastSemi = params[i].lastIndexOf(';');
    		if ((lastGT > 0) && (lastSemi > lastGT) && (lastSemi < params[i].length()-2)) {
    			params[i] = params[i].substring(0, lastGT + 1) + params[i].substring(lastSemi + 1);
    			System.out.println("p[" + (i) + "]=" + params[i] + " val=" + values[i]);
    		}
    	}
	}  // END public static void parseEntryContent()
	
	/** matchPPHRvarsToCCR()
	 * Takes each variable found by parseEntryContent() - in params[],
	 * searches PPHRA.elemLL[][] table for it,
	 * gets matching CCR element line from PPHRA.CCRelems[][],
	 * inserts variable name into <Description><Text>,
	 * inserts dateTime into <Description><Text dateTime= >,
	 * inserts variable value into <Value>
	 * combines CCR strings into one String
	 * adds CCR String to PPHRA.CCRoutList */
	public static void matchPPHRvarsToCCR() {
		int langIdx = 0, lastGT = 0, dateTimeI = 0;
		int fromI = 0, toI = 20;
		String[] findParts = null;
		String findElem = "", outCCRstr = "";
		// 1: find language of entry, i.e. which column of PPHRA.elemLL[][] to search
		for (int i = 0; i < PPHRA.langs[0].length; i++) {
			if (PPHRA.langs[1][i].equals(PPHRA.entLang)) langIdx = i;
		}
		System.out.println("\nMatch lang=" + PPHRA.langs[1][langIdx] + " idx=" + langIdx);
		for (int i = 0; i < params.length; i++) {
			findParts = params[i].split(">"); //split into Problem ... Pain
			if (findParts == null) continue;
			for (int i2 = 0; i2 < findParts.length; i2++) {
				findElem = findParts[i2].trim();

			//lastGT = params[i].lastIndexOf('>');
			//if (lastGT < params[i].length() - 2) findElem = params[i].substring(lastGT + 1).trim();
			//else findElem = params[i].trim();
			System.out.println("findElem=" + findElem + "  params=" + params[i]);
			
			for (int j = fromI; j <= toI; j++) {
				if (elemLL[j][langIdx].startsWith(findElem)) {  // FOUND
					if (PPHRA.fromSN[j] != -1) { // leads to next list
						fromI = PPHRA.fromSN[j];
						toI = PPHRA.toSN[j];
						break; // exit j search loop and go get next word
					}
					System.out.println("  Found " + findElem + " at elemLL[" + j + "] " + elemLL[j][langIdx]);
					if (CCRelems[j][0] == null) continue; //there is no CCR equivalent
					//if (outCCRstr.length() > 5) continue; //prevent multiples
					outCCRstr = ""; // initialize output string
					for (int k = 0; k < 3; k++) {
						if (CCRelems[j][k] != null) outCCRstr += CCRelems[j][k];
					}
					dateTimeI = outCCRstr.lastIndexOf("dateTime");
					if (dateTimeI > 0) outCCRstr = outCCRstr.substring(0, dateTimeI + 9)
													+ PPHRA.entDateTime 
													+ outCCRstr.substring(dateTimeI + 9);
					outCCRstr += params[i];
					for (int k = 3; k < 11; k++) {
						if (CCRelems[j][k] != null) outCCRstr += CCRelems[j][k];
					}
					outCCRstr += values[i];
					for (int k = 11; k < CCRelems[0].length; k++) {
						if (CCRelems[j][k] != null) outCCRstr += CCRelems[j][k];
					}
					PPHRA.CCRoutList.add(outCCRstr); // ADD to output CCR elems list
					outCCRstr += "\n"; // for printing
					System.out.println("  " + outCCRstr);
				} // end if findElem matches an elemLL[][]
			} // end for j loop through elemLL[][] to find findElem
			} // end for i2 loop through each word in params[i]
		} // end for i loop through params[]
	} // End static void matchPPHRvarsToCCR()
} // END public class PPHRparser extends PPHRA
