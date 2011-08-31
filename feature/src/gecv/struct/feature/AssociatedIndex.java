/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.struct.feature;


/**
 * Indexes of two associated features.
 *
 * @author Peter Abeles
 */
public class AssociatedIndex {

	public int src;
	public int dst;
	public double fitScore;

	public void setAssociation(int src , int dst ) {
		this.src = src;
		this.dst = dst;
	}

	public void setAssociation(int src , int dst , double fitScore ) {
		this.src = src;
		this.dst = dst;
		this.fitScore = fitScore;
	}

	public void set( AssociatedIndex a ) {
		src = a.src;
		dst = a.dst;
		fitScore = a.fitScore;
	}
}
