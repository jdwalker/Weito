package pdfbox;











import org.apache.pdfbox.util.TextPosition;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/**
 * wrapper of TextPosition that adds flags to track
 * status as linestart and paragraph start positions.
 * <p>
 * This is implemented as a wrapper since the TextPosition
 * class doesn't provide complete access to its
 * state fields to subclasses.  Also, conceptually TextPosition is
 * immutable while these flags need to be set post-creation so
 * it makes sense to put these flags in this separate class.
 * </p>
 * @author m.martinez@ll.mit.edu
 *
 */
public class PositionWrapper{
	
	private boolean isLineStart = false;
	private boolean isParagraphStart = false;
	private boolean isPageBreak = false;
	private boolean isHangingIndent = false;
	private boolean isArticleStart = false;
	
	private TextPosition position = null;
	
	/**
	 * returns the underlying TextPosition object
	 * @return
	 */
	protected TextPosition getTextPosition(){
		return position;
	}
	

    public boolean isLineStart() {
        return isLineStart;
    }


    /**
     * sets the isLineStart() flag to true
     */
    public void setLineStart() {
        this.isLineStart = true;
    }


    public boolean isParagraphStart() {
        return isParagraphStart;
    }


    /**
     * sets the isParagraphStart() flag to true.
     */
    public void setParagraphStart() {
        this.isParagraphStart = true;
    }


	public boolean isArticleStart() {
		return isArticleStart;
	}


	/**
	 * sets the isArticleStart() flag to true.
	 */
	public void setArticleStart() {
		this.isArticleStart = true;
	}


	public boolean isPageBreak() {
		return isPageBreak;
	}

    
    /**
     * sets the isPageBreak() flag to true
     */
    public void setPageBreak() {
        this.isPageBreak = true;
    }


    public boolean isHangingIndent() {
        return isHangingIndent;
    }


    /**
     * sets the isHangingIndent() flag to true
     */
    public void setHangingIndent() {
        this.isHangingIndent = true;
    }


    /**
     * constructs a PositionWrapper around the specified TextPosition object.
     * @param position
     */
    public PositionWrapper(TextPosition position){
        this.position = position;
    }

}
