/* GENERATED SOURCE. DO NOT MODIFY. */
package com.android.internal.org.bouncycastle.crypto.engines;

import com.android.internal.org.bouncycastle.crypto.BlockCipher;
import com.android.internal.org.bouncycastle.crypto.CipherParameters;
import com.android.internal.org.bouncycastle.crypto.CryptoServicesRegistrar;
import com.android.internal.org.bouncycastle.crypto.DataLengthException;
import com.android.internal.org.bouncycastle.crypto.OutputLengthException;
import com.android.internal.org.bouncycastle.crypto.constraints.DefaultServiceProperties;
import com.android.internal.org.bouncycastle.crypto.params.KeyParameter;

/**
 * a class that provides a basic DESede (or Triple DES) engine.
 * @hide This class is not part of the Android public SDK API
 */
public class DESedeEngine
    extends DESBase
    implements BlockCipher
{
    protected static final int BLOCK_SIZE = 8;

    private int[] workingKey1 = null;
    private int[] workingKey2 = null;
    private int[] workingKey3 = null;

    private boolean forEncryption;

    /**
     * standard constructor.
     */
    public DESedeEngine()
    {
        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(getAlgorithmName(), bitsOfSecurity()));
    }

    /**
     * initialise a DESede cipher.
     *
     * @param encrypting whether or not we are for encryption.
     * @param params     the parameters required to set up the cipher.
     * @throws IllegalArgumentException if the params argument is
     *                                  inappropriate.
     */
    public void init(
        boolean encrypting,
        CipherParameters params)
    {
        if (!(params instanceof KeyParameter))
        {
            throw new IllegalArgumentException("invalid parameter passed to DESede init - " + params.getClass().getName());
        }

        byte[] keyMaster = ((KeyParameter)params).getKey();

        if (keyMaster.length != 24 && keyMaster.length != 16)
        {
            throw new IllegalArgumentException("key size must be 16 or 24 bytes.");
        }

        this.forEncryption = encrypting;

        byte[] key1 = new byte[8];
        System.arraycopy(keyMaster, 0, key1, 0, key1.length);
        workingKey1 = generateWorkingKey(encrypting, key1);

        byte[] key2 = new byte[8];
        System.arraycopy(keyMaster, 8, key2, 0, key2.length);
        workingKey2 = generateWorkingKey(!encrypting, key2);

        if (keyMaster.length == 24)
        {
            byte[] key3 = new byte[8];
            System.arraycopy(keyMaster, 16, key3, 0, key3.length);
            workingKey3 = generateWorkingKey(encrypting, key3);
        }
        else    // 16 byte key
        {
            workingKey3 = workingKey1;
        }

        CryptoServicesRegistrar.checkConstraints(new DefaultServiceProperties(getAlgorithmName(), bitsOfSecurity(), params, Utils.getPurpose(forEncryption)));
    }

    public String getAlgorithmName()
    {
        return "DESede";
    }

    public int getBlockSize()
    {
        return BLOCK_SIZE;
    }

    public int processBlock(
        byte[] in,
        int inOff,
        byte[] out,
        int outOff)
    {
        if (workingKey1 == null)
        {
            throw new IllegalStateException("DESede engine not initialised");
        }

        if ((inOff + BLOCK_SIZE) > in.length)
        {
            throw new DataLengthException("input buffer too short");
        }

        if ((outOff + BLOCK_SIZE) > out.length)
        {
            throw new OutputLengthException("output buffer too short");
        }

        byte[] temp = new byte[BLOCK_SIZE];

        if (forEncryption)
        {
            desFunc(workingKey1, in, inOff, temp, 0);
            desFunc(workingKey2, temp, 0, temp, 0);
            desFunc(workingKey3, temp, 0, out, outOff);
        }
        else
        {
            desFunc(workingKey3, in, inOff, temp, 0);
            desFunc(workingKey2, temp, 0, temp, 0);
            desFunc(workingKey1, temp, 0, out, outOff);
        }

        return BLOCK_SIZE;
    }

    public void reset()
    {
    }

    // Service Definitions
    private int bitsOfSecurity()
    {
        if (workingKey1 != null && workingKey1 == workingKey3)
        {
            return 80;
        }
        return 112;
    }
}
