//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.autovigenere.algorithm;


import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;

/**
 * The CaesarAlgorithm extends the AbstractClassicAlgorithm.
 * @see org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm.
 *
 * @author SLeischnig
 * @version 0.1
 */
public class AutoVigenereAlgorithm extends AbstractClassicAlgorithm {

	public static final AutoVigenereAlgorithmSpecification specification = new AutoVigenereAlgorithmSpecification();
	
	
	/**
	 * Constructor
	 * The specific engine of the algorithm is assigned.
	 *
	 */
	public AutoVigenereAlgorithm() {
		engine = new AutoVigenereEngine();

	}


	/**
	 * This method takes the key data as a char array and
	 * generates from it the algorithm key as int array
	 * @param keyData the key data
	 * @return the generated key as int array
	 */
	protected int[] generateKey(char[] keyData) {

//			int [] tmp = null;
			int[] value = null;
			try {
//				int value = (alphaConv.getAlphaIndex(keyData[0]) + 1) % alphaConv.getAlphaLength();
				value = alphaConv.charArrayToIntArray(keyData);

//				tmp = new int [1];
//
//				tmp[0] = value;
			} catch (Exception e) {
			}

//			return tmp;
			return value;
	}


    @Override
    public String getAlgorithmName() {
        return "Autokey-Vigen\u00E8re"; //$NON-NLS-1$
    }
}