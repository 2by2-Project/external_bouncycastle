/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.org.bouncycastle.crypto.generators;

import com.android.org.bouncycastle.crypto.CryptoServicePurpose;
import com.android.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.org.bouncycastle.crypto.KeyGenerationParameters;
import com.android.org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import com.android.org.bouncycastle.crypto.params.DESedeParameters;

/**
 * @hide This class is not part of the Android public SDK API
 */
public class DESedeKeyGenerator
    extends DESKeyGenerator
{
    private static final int MAX_IT = 20;

    /**
     * initialise the key generator - if strength is set to zero
     * the key generated will be 192 bits in size, otherwise
     * strength can be 128 or 192 (or 112 or 168 if you don't count
     * parity bits), depending on whether you wish to do 2-key or 3-key
     * triple DES.
     *
     * @param param the parameters to be used for key generation
     */
    public void init(
        KeyGenerationParameters param)
    {
        this.random = param.getRandom();
        this.strength = (param.getStrength() + 7) / 8;

        if (strength == 0 || strength == (168 / 8))
        {
            strength = DESedeParameters.DES_EDE_KEY_LENGTH;
        }
        else if (strength == (112 / 8))
        {
            strength = 2 * DESedeParameters.DES_KEY_LENGTH;
        }
        else if (strength != DESedeParameters.DES_EDE_KEY_LENGTH
                && strength != (2 * DESedeParameters.DES_KEY_LENGTH))
        {
            throw new IllegalArgumentException("DESede key must be "
                + (DESedeParameters.DES_EDE_KEY_LENGTH * 8) + " or "
                + (2 * 8 * DESedeParameters.DES_KEY_LENGTH)
                + " bits long.");
        }

        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties("DESedeKeyGen", 112, null, CryptoServicePurpose.KEYGEN));
    }

    public byte[] generateKey()
    {
        byte[]  newKey = new byte[strength];
        int     count = 0;

        do
        {
            random.nextBytes(newKey);

            DESedeParameters.setOddParity(newKey);
        }
        while (++count < MAX_IT && (DESedeParameters.isWeakKey(newKey, 0, newKey.length) || !DESedeParameters.isRealEDEKey(newKey, 0)));

        if (DESedeParameters.isWeakKey(newKey, 0, newKey.length) || !DESedeParameters.isRealEDEKey(newKey, 0))
        {
            throw new IllegalStateException("Unable to generate DES-EDE key");
        }

        return newKey;
    }
}
