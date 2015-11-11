package my.pphr.toCCRtrans;

import java.util.ArrayList;

public class CCRoutput extends PPHRA 
{
	public static String CCRhead = "<?xml version=\"1.0\"?>" +
			"<?xml-stylesheet type=\"text/xsl\" href=\"ccr.xsl\"?>" +
			"<ContinuityOfCareRecord " +
			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
			"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xmlns=\"urn:astm-org:CCR\"> " +
			"<CCRDocumentObjectID>1.3.6.1.4.1.22812.1.999.1021362</CCRDocumentObjectID>";
	public static String CCRbodyStart = "<Body>";
	public static String CCRbodyEnd = "</Body";
	public static String CCRtail = "</ContinuityOfCareRecord>";

	/** CCRoutput() Constructor */
	public CCRoutput() {
		// TODO Auto-generated constructor stub
	} // End CCRoutput() Constructor

	/** public String removeEmptyTags()
	 * The CCR elements from the translation are stored in
	 *     ArrayList<String> PPHRA.CCRelemList
	 * Empty tags are like <tag></tag> and so are superfluous.
	 * This loops through PPHRA.CCRelemList, gets each String,
	 *     looks for empty tags and removes them from the element
	 * SHOULD THIS BE FOR EACH ENTRY SEPARATELY, OR FOR WHOLE MESSAGE AT ONE TIME? 
	 * @param String element - the String to process
	 * @return String elemOut - the String with empty tags removed */
	public static void removeEmptyTags() {
		String elemOut = "";
		int[] tagStartEnd = new int[2];
		// Loop thru all elems in PPHRA.CCRoutList
		for (int i = 0; i < PPHRA.CCRoutList.size(); i++) {
			elemOut = PPHRA.CCRoutList.get(i);
			if ((elemOut == null) || (elemOut.length() < 7)) continue;
			do {
				tagStartEnd = hasEmptyTag(elemOut);
				if ((tagStartEnd[0] < 0) || (tagStartEnd[1] <= tagStartEnd[0])) 
					break; // no more empty tags, so no change
				elemOut = elemOut.substring(0, tagStartEnd[0]) 
					+ elemOut.substring(tagStartEnd[1]);
				PPHRA.CCRoutList.set(i, elemOut); // put revision back into list
				//System.out.println("CCRout empty elem: " + elemOut);
			} while (tagStartEnd[0] >= 0);
			System.out.println("CCRout remove empty " + elemOut);
		} // end for i loop thru PPHRA.CCRoutList of CCR elems
	} // End public String removeEmptyTags(String element)
	
	/** public int[] hasEmptyTag(String str)
	 * Searches String str for empty tags like <tag></tag>
	 * @param String str - the String to search
	 * @return int[2] startStop - startStop[0] = index of start of empty tag
	 *                            startStop[1] = index of end + 1 of empty tag
	 */
	public static int[] hasEmptyTag(String str) {
		int[] startStop = {-1, -1};
		int pos = -1, pos1 = -1, pos2 = -1;
		String tag1 = "", tag2 = "";
		if ((str == null) || (str.length() < 7)) return startStop; // no input
		// search str for empty tags, e.g. search for "></"
		pos = str.indexOf("></");
		if (pos <= 0) return startStop;
		// if find one, i.e. pos > 0
		// find tag1 before pos
		pos1 = str.lastIndexOf('<', pos);
		if ((pos1 > 0) && (pos1 < pos)) tag1 = str.substring(pos1+1, pos).trim();
		else tag1 = "";
		// find tag2 after pos
		pos2 = str.indexOf('>', pos + 3);
		if ((pos2 < str.length()) && (pos2 > pos + 3)) 
			tag2 = str.substring(pos+3, pos2).trim();
		else tag2 = "";
		if (tag1.equals(tag2)) {
			startStop[0] = pos1;
			startStop[1] = pos2+1;
			System.out.println("tag1-tag2 " + str.substring(startStop[0],startStop[1]));
		}
		return startStop;
	} // End public int[] hasEmptyTag(String str)
	
	/** public static void combineElems()
	 * Loops through PPHRA.CCRoutList,
	 *   gets Strings i and i+1,
	 *   if end tag in i == start tag in i+1, removes both
	 * SHOULD THIS BE FOR EACH ENTRY SEPARATELY, OR FOR WHOLE MESSAGE AT ONE TIME? */
	public static void combineElems() {
		String sI = "", sIplus1 = "";
		String sIend = "", sIplus1start = "";
		int sIendIdx = 0, sIplus1startIdx = 0;
		boolean foundMatch = false;
		do {
			foundMatch = false;

		for (int i = 0; i < PPHRA.CCRoutList.size() - 1; i++) {
			// if end tag from [i] identical to start tag from [i+1]
			// remove both
			sI = PPHRA.CCRoutList.get(i);
			sIendIdx = sI.lastIndexOf("</"); // look for last </tag>
			if (sIendIdx <= 0) continue;  // not found, skip this entry
			sIplus1 = PPHRA.CCRoutList.get(i+1);
			sIplus1startIdx = sIplus1.indexOf("><"); // look for first <start><next>
			if (sIplus1startIdx <= 0) continue;
			sIend = sI.substring(sIendIdx + 2).trim(); // "tag>"
			sIplus1start = sIplus1.substring(1, sIplus1startIdx + 1); // "tag>"
			System.out.println("Remove end=" + sIend + " start=" + sIplus1start);
			if (sIplus1start.equals(sIend)) {
				// remove end tag from sIend
				sI = sI.substring(0, sIendIdx); 
				// remove start tag from sIplus1
				sIplus1 = sIplus1.substring(sIplus1startIdx + 1);
				PPHRA.CCRoutList.set(i, sI);
				PPHRA.CCRoutList.set(i + 1, sIplus1);
				foundMatch = true;
			}
		} // end for i loop thru PPHRA.CCRoutList
		} while (foundMatch);

	} // END void combineElems()
	
	/** public static String makeCCRoutString()
	 * Loops through PPHRA.CCRoutList
	 * and combines Strings into one
	 */
	public static String makeCCRoutString() {
		String out = "";  // = CCRhead;
		for (int i = 0; i < PPHRA.CCRoutList.size(); i++) {
			out += PPHRA.CCRoutList.get(i) + "\n"; // remove EOF after testing
		}
		out += CCRtail;
		return out;
	}
} // END public class CCRoutput
