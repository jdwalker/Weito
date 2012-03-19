package pdfbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.ResourceLoader;
import org.apache.pdfbox.util.TextPosition;
import org.apache.pdfbox.util.TextPositionComparator;


public class PDFTextWithFormatExtractor extends PDFTextStripper {
    
	private static final float ENDOFLASTTEXTX_RESET_VALUE = -1;
	private static final float MAXYFORLINE_RESET_VALUE = -Float.MAX_VALUE;
	private static final float EXPECTEDSTARTOFNEXTWORDX_RESET_VALUE = -Float.MAX_VALUE;
	private static final float MAXHEIGHTFORLINE_RESET_VALUE = -1;
	private static final float MINYTOPFORLINE_RESET_VALUE = Float.MAX_VALUE;
	private static final float LASTWORDSPACING_RESET_VALUE = -1;
//    private TextNormalize normalize = null;
	/**
	 * We will later use this to skip reordering
	 */
	@SuppressWarnings("unused")
	private boolean hasRtl = false;
	private List<Format> documentFormatChunks = new ArrayList<Format>();
	public PDFTextWithFormatExtractor() throws IOException {
        super( ResourceLoader.loadProperties(
                "org/apache/pdfbox/resources/PDFTextStripper.properties", true ) );
        this.outputEncoding = null;
//        normalize = new TextNormalize(this.outputEncoding);
	}

	public List<Format> getDocumentFormatChunks() {
		return documentFormatChunks;
	}

	@SuppressWarnings("unchecked")
	protected void writePage() throws IOException
	    {
	        float maxYForLine = MAXYFORLINE_RESET_VALUE;
	        float minYTopForLine = MINYTOPFORLINE_RESET_VALUE;
	        float endOfLastTextX = ENDOFLASTTEXTX_RESET_VALUE;
	        float lastWordSpacing = LASTWORDSPACING_RESET_VALUE;
	        float maxHeightForLine = MAXHEIGHTFORLINE_RESET_VALUE;
	        PositionWrapper lastPosition = null;
	        PositionWrapper lastLineStartPosition = null;
	        boolean startOfPage = true;//flag to indicate start of page
	        boolean startOfArticle = true;
	        if(charactersByArticle.size() > 0) { 
	            //writePageStart();
	        }

	        for( int i = 0; i < charactersByArticle.size(); i++)
	        {
	            List<TextPosition> textList = charactersByArticle.get( i );
	            if( getSortByPosition() )
	            {
	                TextPositionComparator comparator = new TextPositionComparator();
	                Collections.sort( textList, comparator );
	            }

	            Iterator<TextPosition> textIter;
	            
	            //defines isRtldominant, and hasRtl
				boolean isRtlDominant = defineDominantRtlOrder(textList);

	            startArticle(!isRtlDominant);
	            startOfArticle = true;
	           
	            /* Now cycle through to print the text.
	             * We queue up a line at a time before we print so that we can convert
	             * the line from presentation form to logical form (if needed). 
	             */
	            List<TextPosition> line = new ArrayList<TextPosition>();

	            textIter = textList.iterator();    // start from the beginning again
	            /* PDF files don't always store spaces. We will need to guess where we should add
	             * spaces based on the distances between TextPositions. Historically, this was done
	             * based on the size of the space character provided by the font. In general, this worked
	             * but there were cases where it did not work. Calculating the average character width
	             * and using that as a metric works better in some cases but fails in some cases where the
	             * spacing worked. So we use both. NOTE: Adobe reader also fails on some of these examples.
	             */
	            //Keeps track of the previous average character width
	            float previousAveCharWidth = -1;
	            while( textIter.hasNext() )
	            {
	                TextPosition position = (TextPosition)textIter.next();
	                PositionWrapper current = new PositionWrapper(position);
	                String characterValue = position.getCharacter();

	                //Resets the average character width when we see a change in font
	                // or a change in the font size
	                if(lastPosition != null && ((position.getFont() != lastPosition.getTextPosition().getFont())
	                        || (position.getFontSize() != lastPosition.getTextPosition().getFontSize())))
	                {
	                    previousAveCharWidth = -1;
	                }

	                float positionX;
	                float positionY;
	                float positionWidth;
	                float positionHeight;

	                /* If we are sorting, then we need to use the text direction
	                 * adjusted coordinates, because they were used in the sorting. */
	                if (getSortByPosition())
	                {
	                    positionX = position.getXDirAdj();
	                    positionY = position.getYDirAdj();
	                    positionWidth = position.getWidthDirAdj();
	                    positionHeight = position.getHeightDir();
	                }
	                else
	                {
	                    positionX = position.getX();
	                    positionY = position.getY();
	                    positionWidth = position.getWidth();
	                    positionHeight = position.getHeight();
	                }

	                //The current amount of characters in a word
	                int wordCharCount = position.getIndividualWidths().length;

	                /* . */
	                float wordSpacing = position.getWidthOfSpace();
	                float deltaSpace = calculateDeltaSpace(lastWordSpacing, wordSpacing);

	                /* Estimate the expected width of the space based on the
	                 * average character width with some margin. This calculation does not
	                 * make a true average (average of averages) but we found that it gave the
	                 * best results after numerous experiments. Based on experiments we also found that
	                 * .3 worked well. */
	                float averageCharWidth = -1;
	                if(previousAveCharWidth < 0)
	                {
	                    averageCharWidth = (positionWidth/wordCharCount);
	                }
	                else
	                {
	                    averageCharWidth = (previousAveCharWidth + (positionWidth/wordCharCount))/2f;
	                }
	                float deltaCharWidth = (averageCharWidth * getAverageCharTolerance());

	                //Compares the values obtained by the average method and the wordSpacing method and picks
	                //the smaller number.
	                float expectedStartOfNextWordX = EXPECTEDSTARTOFNEXTWORDX_RESET_VALUE;
	                if(endOfLastTextX != ENDOFLASTTEXTX_RESET_VALUE)
	                {
	                    if(deltaCharWidth > deltaSpace)
	                    {
	                        expectedStartOfNextWordX = endOfLastTextX + deltaSpace;
	                    }
	                    else
	                    {
	                        expectedStartOfNextWordX = endOfLastTextX + deltaCharWidth;
	                    }
	                }

	                if( lastPosition != null )
	                {
	                    if(startOfArticle){
	                        lastPosition.setArticleStart();
	                        startOfArticle = false;
	                    }
	                    // RDD - Here we determine whether this text object is on the current
	                    // line.  We use the lastBaselineFontSize to handle the superscript
	                    // case, and the size of the current font to handle the subscript case.
	                    // Text must overlap with the last rendered baseline text by at least
	                    // a small amount in order to be considered as being on the same line.

	                    /* XXX BC: In theory, this check should really check if the next char is in full range
	                     * seen in this line. This is what I tried to do with minYTopForLine, but this caused a lot
	                     * of regression test failures.  So, I'm leaving it be for now. */
	                    if(!overlap(positionY, positionHeight, maxYForLine, maxHeightForLine))
	                    {
	                    	List<Format> lineFormatChunks = formattingLine(line);
	                        //writeLine(normalize(line,isRtlDominant,hasRtl),isRtlDominant);
	                        line.clear();

	                        lastLineStartPosition = handleLineSeparation(current, lastPosition, lastLineStartPosition, maxHeightForLine, lineFormatChunks);

	                        endOfLastTextX = ENDOFLASTTEXTX_RESET_VALUE;
	                        expectedStartOfNextWordX = EXPECTEDSTARTOFNEXTWORDX_RESET_VALUE;
	                        maxYForLine = MAXYFORLINE_RESET_VALUE;
	                        maxHeightForLine = MAXHEIGHTFORLINE_RESET_VALUE;
	                        minYTopForLine = MINYTOPFORLINE_RESET_VALUE;
	                    }

	                    //Test if our TextPosition starts after a new word would be expected to start.
	                    if (expectedStartOfNextWordX != EXPECTEDSTARTOFNEXTWORDX_RESET_VALUE && expectedStartOfNextWordX < positionX &&
	                            //only bother adding a space if the last character was not a space
	                             lastPosition.getTextPosition().getCharacter() != null &&
	                            !lastPosition.getTextPosition().getCharacter().endsWith(  getWordSeparator()  ) )
	                    {
	                        line.add(WordSeparator.getSeparator());	                    	
	                    }
	                }

	                if (positionY >= maxYForLine)
	                {
	                    maxYForLine = positionY;
	                }

	                // RDD - endX is what PDF considers to be the x coordinate of the
	                // end position of the text.  We use it in computing our metrics below.
	                endOfLastTextX = positionX + positionWidth;

	                // add it to the list
	                if (characterValue != null)
	                {
	                    if(startOfPage && lastPosition==null){
	                        appendFormatString(getParagraphStart());//not sure this is correct for RTL?
	                    }
	                    line.add(position);
	                }
	                maxHeightForLine = Math.max( maxHeightForLine, positionHeight );
	                minYTopForLine = Math.min(minYTopForLine,positionY - positionHeight);
	                lastPosition = current;
	                if(startOfPage){
	                    lastPosition.setParagraphStart();
	                    lastPosition.setLineStart();
	                    lastLineStartPosition = lastPosition;
	                    startOfPage=false;
	                }
	                lastWordSpacing = wordSpacing;
	                previousAveCharWidth = averageCharWidth;
	            }

	            // print the final line
	            if (line.size() > 0)
	            {
	                //writeLine(normalize(line,isRtlDominant,hasRtl),isRtlDominant);
	                List<Format> lineFormatChunks = formattingLine(line);
	                appendFormatChunks(lineFormatChunks, "");
	                appendFormatString(getParagraphEnd());
	            }

	            endArticle();
	        }
	        appendFormatString(getPageEnd());
	    }


	@Override
	protected void endArticle() throws IOException {
		appendFormatString(getArticleEnd());
	}


	private void appendFormatString(String stringToAppend) {
		if(documentFormatChunks.size() > 0) {
		Format lastChunk = getLastDocFormatChunk();
		lastChunk.appendText(stringToAppend);
		}
	}


	/**
	 * @return Phrase
	 * 
	 */
	private Format getLastDocFormatChunk() {
		int lastDocIndex = documentFormatChunks.size() - 1;
		return documentFormatChunks.get(lastDocIndex);
	}
	
	private void setLastDocFormatChunk(Format lastChunk) {
		int lastDocIndex = documentFormatChunks.size() - 1;
		if(lastDocIndex < 0) {
			lastDocIndex = 0;
			}
		documentFormatChunks.set(lastDocIndex,lastChunk);
	}


	private ArrayList<Format> formattingLine(List<TextPosition> line) {
		String ws = getWordSeparator();
		ArrayList<Format> formatLineChunks = new ArrayList<Format>();
				
		for(TextPosition textPos : line) {
			if(formatLineChunks.size() <= 0) {
				formatLineChunks.add(new FormatLeaf(textPos));
			} else {
				Format buffer = formatLineChunks.get(formatLineChunks.size() - 1);
					Format currentPhrase = new FormatLeaf(textPos);
					if( currentPhrase.formattedAs( buffer.getLastPhrase() ) ) {
						buffer.getLastPhrase().appendText(currentPhrase.getText());			
					} else if(textPos instanceof WordSeparator) {
						buffer.appendText(ws);
					} else {
						formatLineChunks.add(currentPhrase);
					}
						
				}
			}
		return formatLineChunks;
	}
	
//	private Format pullLastWordAsPhrase(Format p) {
//		p = p.getLastPhrase(); //Makes sure very last phrase, and thus must be a leaf node;
//		String t = p.getText();
//		if(t.contains( getWordSeparator() )) {
//			String[] s = t.split( getWordSeparator() );
//			String pullWord = s[s.length - 1];
//			s[s.length - 1] = "";
//			p.setText(joinStrings(s, getWordSeparator() ));
//			return new FormatLeaf(pullWord, p.getPrimaryStyle());
//		} else {
//			Format parentOfp = p.getParent();
//			if(parentOfp != null) {
//				parentOfp.remove(p);
//			}
//			return p;
//		}
//	}
//	
//	private Format pullFirstWordAsPhrase(Format p) {
//		p = p.getLastPhrase(); //Makes sure very last phrase, and thus must be a leaf node;
//		String t = p.getText();
//		if(t.contains( getWordSeparator() )) {
//			String[] s = t.split( getWordSeparator() );
//			String pullWord = s[0];
//			s[0] = "";
//			p.setText(joinStrings(s, getWordSeparator() ));
//			return new FormatLeaf(pullWord, p.getPrimaryStyle());
//		} else {
//			Format parentOfp = p.getParent();
//			if(parentOfp != null) {
//				parentOfp.remove(p);
//			}
//			return p;
//		}
//	}
	
	


//	private String joinStrings(String[] strings, String concatString) {
//		String result = "";
//		for(String s: strings) {
//			if(result != "" && s != "") {
//				result += concatString;
//			}
//			result += s;
//		}
//		
//		return result;
//	}

	/**
	 * Estimate the expected width of the space based on the
	 * space character with some margin
	 * @param lastWordSpacing
	 * @param wordSpacing
	 * @return deltaSpace
	 */
	private float calculateDeltaSpace(float lastWordSpacing, float wordSpacing) {
		float deltaSpace;
		if ((wordSpacing == 0) || (wordSpacing == Float.NaN))
		{
		    deltaSpace = Float.MAX_VALUE;
		}
		else
		{
		    if( lastWordSpacing < 0 )
		    {
		        deltaSpace = (wordSpacing * getSpacingTolerance());
		    }
		    else
		    {
		        deltaSpace = (((wordSpacing+lastWordSpacing)/2f)* getSpacingTolerance());
		    }
		}
		return deltaSpace;
	}
	 

	/**
	 * Before we can display the text, we need to do some normalizing.
	 * Arabic and Hebrew text is right to left and is typically stored
	 * in its logical format, which means that the rightmost character is
	 * stored first, followed by the second character from the right etc.
	 * However, PDF stores the text in presentation form, which is left to
	 * right.  We need to do some normalization to convert the PDF data to
	 * the proper logical output format.
	 *
	 * Note that if we did not sort the text, then the output of reversing the
	 * text is undefined and can sometimes produce worse output then not trying
	 * to reverse the order.  Sorting should be done for these languages.
	 * 
	 * sets the internal obj field hasRtl
	 * @param textList
	 * @return isRtldominant
	 */
	private boolean defineDominantRtlOrder(List<TextPosition> textList) {
		Iterator<TextPosition> textIter = textList.iterator();

		/* First step is to determine if we have any right to left text, and
		 * if so, is it dominant. */
		int ltrCnt = 0;
		int rtlCnt = 0;

		while( textIter.hasNext() )
		{
		    TextPosition position = (TextPosition)textIter.next();
		    String stringValue = position.getCharacter();
		    for (int a = 0; a < stringValue.length(); a++)
		    {
		        byte dir = Character.getDirectionality(stringValue.charAt(a));
		        if ((dir == Character.DIRECTIONALITY_LEFT_TO_RIGHT ) ||
		                (dir == Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING) ||
		                (dir == Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE ))
		        {
		            ltrCnt++;
		        }
		        else if ((dir == Character.DIRECTIONALITY_RIGHT_TO_LEFT ) ||
		                (dir == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC) ||
		                (dir == Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING) ||
		                (dir == Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE ))
		        {
		            rtlCnt++;
		        }
		    }
		}
		hasRtl = rtlCnt > 0;
		// choose the dominant direction
        return rtlCnt > ltrCnt;
	}
	 
	    /**
	     * Normalize the given list of TextPositions.
	     * @param line list of TextPositions
	     * @param isRtlDominant determines if rtl or ltl is dominant 
	     * @param hasRtl determines if lines contains rtl formatted text(parts)
	     * @return a list of strings, one string for every word
	     */
//	    private List<String> normalize(List<TextPosition> line, boolean isRtlDominant, boolean hasRtl){
//	        LinkedList<String> normalized = new LinkedList<String>();
//	        StringBuilder lineBuilder = new StringBuilder();
//	        for(TextPosition text : line){
//	            if (text instanceof WordSeparator) {
//	                String lineStr = lineBuilder.toString();
//	                if (hasRtl) {
//	                    lineStr = normalize.makeLineLogicalOrder(lineStr,isRtlDominant);
//	                }
//	                lineStr = normalize.normalizePres(lineStr);
//	                normalized.add(lineStr);
//	                lineBuilder = new StringBuilder();
//	            }
//	            else {
//	                lineBuilder.append(text.getCharacter());
//	            }
//	        }
//	        if (lineBuilder.length() > 0) {
//	            String lineStr = lineBuilder.toString();
//	            if (hasRtl) {
//	                lineStr = normalize.makeLineLogicalOrder(lineStr,isRtlDominant);
//	            }
//	            lineStr = normalize.normalizePres(lineStr);
//	            normalized.add(lineStr);
//	        }
//	        return normalized;
//	    }
	 
//	    /**
//	     * Write a list of string containing a whole line of a document.
//	     * @param line a list with the words of the given line
//	     * @param isRtlDominant determines if rtl or ltl is dominant
//	     * @throws IOException if something went wrong
//	     */
//	    private void writeLine(List<String> line, boolean isRtlDominant)throws IOException{
//	        int numberOfStrings = line.size();
//	        if (isRtlDominant) {
//	            for(int i=numberOfStrings-1; i>=0; i--){
//	                if (i < numberOfStrings-1)
//	                    writeWordSeparator();
//	                writeString(line.get(i));
//	            }
//	        }
//	        else {
//	            for(int i=0; i<numberOfStrings; i++){
//	                writeString(line.get(i));
//	                if (!isRtlDominant && i < numberOfStrings-1)
//	                    writeWordSeparator();
//	            }
//	        }
//	    }
	 
	    private boolean overlap( float y1, float height1, float y2, float height2 )
	    {
	        return within( y1, y2, .1f) || (y2 <= y1 && y2 >= y1-height1) ||
	        (y1 <= y2 && y1 >= y2-height2);
	    }
	    
	    private boolean within( float first, float second, float variance )
	    {
	        return second < first + variance && second > first - variance;
	    }
	    
	    /**
	     * handles the line separator for a new line given
	     * the specified current and previous TextPositions.
	     * @param position the current text position
	     * @param lastPosition the previous text position
	     * @param lastLine StartPosition the last text position that followed a line
	     *        separator.
	     * @param maxHeightForLine max height for positions since lastLineStartPosition
	     * @throws IOException
	     */
	    protected PositionWrapper handleLineSeparation(PositionWrapper current,
	            PositionWrapper lastPosition, PositionWrapper lastLineStartPosition, float maxHeightForLine,List<Format> line)
	            throws IOException {
	        current.setLineStart();
	        isParagraphSeparation(current, lastPosition, lastLineStartPosition, maxHeightForLine);
	        lastLineStartPosition = current;
	        addLineTextChunk(current, lastPosition, line);
	        return lastLineStartPosition;
	    }


		/**
		 * @param current
		 * @param lastPosition
		 * @throws IOException
		 */
		private void addLineTextChunk(PositionWrapper current,
				PositionWrapper lastPosition,List<Format> line) throws IOException {
	        if (current.isParagraphStart())  {
	            if(lastPosition.isArticleStart()) {
	                getParagraphStart();
	            } else {
	                appendFormatChunks(line,getLineSeparator() + getParagraphEnd() + getParagraphStart());
	            }
	        } else {
	            appendFormatChunks(line, getLineSeparator());
	        }
		}
	    
	    private void appendFormatChunks(List<Format> lineChunks, String seperationString) {
			if(lineChunks != null && lineChunks.size() > 0) {
				if(documentFormatChunks.size() <= 0) {
					documentFormatChunks = lineChunks;
				} else {
					Format firstLineChunkFormat = lineChunks.get(0);
					Format lastDocumentChunkFormat = getLastDocFormatChunk();
					lastDocumentChunkFormat.appendText(seperationString);
					if(lastDocumentChunkFormat.formattedAs(firstLineChunkFormat)) {
						lastDocumentChunkFormat.appendText(firstLineChunkFormat.getText());
						lineChunks.remove(0);
					}
					setLastDocFormatChunk(lastDocumentChunkFormat);
					documentFormatChunks.addAll(lineChunks);
				}
			}
		}


		/**
	     * tests the relationship between the last text position, the current text
	     * position and the last text position that followed a line separator to
	     * decide if the gap represents a paragraph separation. This should
	     * <i>only</i> be called for consecutive text positions that first pass the
	     * line separation test.
	     * <p>
	     * This base implementation tests to see if the lastLineStartPosition is
	     * null OR if the current vertical position has dropped below the last text
	     * vertical position by at least 2.5 times the current text height OR if the
	     * current horizontal position is indented by at least 2 times the current
	     * width of a space character.</p>
	     * <p>
	     * This also attempts to identify text that is indented under a hanging indent.</p>
	     * <p>
	     * This method sets the isParagraphStart and isHangingIndent flags on the current
	     * position object.</p>
	     *
	     * @param position the current text position.  This may have its isParagraphStart
	     * or isHangingIndent flags set upon return.
	     * @param lastPosition the previous text position (should not be null).
	     * @param lastLineStartPosition the last text position that followed a line
	     *            separator. May be null.
	     * @param maxHeightForLine max height for text positions since lasLineStartPosition.
	     */
	    protected void isParagraphSeparation(PositionWrapper position,  
	            PositionWrapper lastPosition, PositionWrapper lastLineStartPosition, float maxHeightForLine){
	        boolean result = false;
	        if(lastLineStartPosition == null) {
	            result = true;
	        }else{
	            float yGap = Math.abs(position.getTextPosition().getYDirAdj()-
	                    lastPosition.getTextPosition().getYDirAdj());
	            float xGap = (position.getTextPosition().getXDirAdj()-
	                    lastLineStartPosition.getTextPosition().getXDirAdj());//do we need to flip this for rtl?
	            if(yGap > (getDropThreshold()*maxHeightForLine)){
	                        result = true;
	            }else if(xGap > (getIndentThreshold()*position.getTextPosition().getWidthOfSpace())){
	                //text is indented, but try to screen for hanging indent
	                if(!lastLineStartPosition.isParagraphStart()){
	                     result = true;
	                }else{
	                     position.setHangingIndent();
	                }
	            }else if(xGap < -position.getTextPosition().getWidthOfSpace()){
	                //text is left of previous line. Was it a hanging indent?
	                if(!lastLineStartPosition.isParagraphStart()){
	                            result = true;
	                }
	            }else if(Math.abs(xGap) < (0.25 * position.getTextPosition().getWidth())){
	                //current horizontal position is within 1/4 a char of the last
	                //linestart.  We'll treat them as lined up.
	                if(lastLineStartPosition.isHangingIndent()){
	                    position.setHangingIndent();
	                }else if(lastLineStartPosition.isParagraphStart()){
	                    //check to see if the previous line looks like
	                    //any of a number of standard list item formats
	                    Pattern liPattern = matchListItemPattern(lastLineStartPosition);
	                    if(liPattern!=null){
	                        Pattern currentPattern = matchListItemPattern(position);
	                        if(liPattern == currentPattern){
	                                    result = true;
	                        }
	                    }
	               }
	           }
	        }
	        if(result){
	            position.setParagraphStart();
	        }
	    }
	    
	    protected Pattern matchListItemPattern(PositionWrapper pw) {
	        TextPosition tp = pw.getTextPosition();
	        String txt = tp.getCharacter();
	        Pattern p = matchPattern(txt,getListItemPatterns());
	        return p;
	    }
	    
	    /* (non-Javadoc)
	     * @see org.apache.pdfbox.util.PDFTextStripper#getText(org.apache.pdfbox.pdmodel.PDDocument)
	     */
	    @Override
	    public String getText(PDDocument doc) throws IOException {
	    	String s = super.getText(doc);
	    	//stripOutDoubleWhiteSpace();
	    	//parseDocumentFormatChunks();
			addTextPositions();
	    	return s;
	    }
	    
	    /**
	     * Adds the position of the format to the text
	     * @throws Exception 
	     */
	    private void addTextPositions() {
	    	int i = 0;
	    	for(Format p : documentFormatChunks) {
	    		p.setStartPos(i);
	    		i += p.getText().length();
	    	}
		}

//		@SuppressWarnings("unused")
//		private void parseDocumentFormatChunks() {
//			Iterator<Format> iterator = documentFormatChunks.iterator();
//			List<Format> newdFC = new ArrayList<Format>();
//			Format prevPhrase = null;
//			if(iterator.hasNext()) {
//				prevPhrase = iterator.next();
//			}
//			while(iterator.hasNext()) {
//				Format currentPhrase = iterator.next();
//				String string = prevPhrase.getLastPhrase().getText();
//				if(!string.equals("") && !string.endsWith( getWordSeparator())) {
//					if ( !( prevPhrase instanceof FormatComposite) ) {
//						//make prevPhrase a format composite and push remainder of text to dFC
//						if(prevPhrase.getText().contains( getWordSeparator() )) {
//							Format newComposite = new FormatComposite(pullLastWordAsPhrase(prevPhrase));
//							if(!prevPhrase.getText().equals("")) {
//							newdFC.add(prevPhrase);
//							}
//							prevPhrase = newComposite;
//						} else {
//							prevPhrase = new FormatComposite(prevPhrase);
//						}
//					}
//					if(currentPhrase.getText().contains( getWordSeparator() )) {
//						//push composite to dFC
//						if (!currentPhrase.getText().equals(" ")) {
//							prevPhrase.add(pullFirstWordAsPhrase(currentPhrase));
//						} else {
//							prevPhrase.getLastPhrase().appendText(" ");
//						}
//						
//						newdFC.add(prevPhrase);
//						prevPhrase = currentPhrase;						
//					} else {
//						//add new Phrase
//						prevPhrase.add(currentPhrase);
//					}
//				} else {
//					if(!string.equals("")) {
//					newdFC.add(prevPhrase);
//					}
//					prevPhrase = currentPhrase;
//				}
//			}
//			if(prevPhrase != null) {
//				newdFC.add(prevPhrase);
//			}
//			documentFormatChunks = newdFC;
//	    }

		/**
	     * internal marker class.  Used as a place holder in
	     * a line of TextPositions.
	     * @author ME21969
	     *
	     */
	    private static final class WordSeparator extends TextPosition{
	        private static final WordSeparator separator = new WordSeparator();
	        
	        private WordSeparator(){
	        }

	        public static final WordSeparator getSeparator(){
	            return separator;
	        }

	    }
	 
}
