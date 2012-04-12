/*
Copyright (C) 2012 Thomas Farr a.k.a tomass1996 [farr.thomas@gmail.com]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
copies of the Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

-The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
-Visible credit is given to the original author.
-The software is distributed in a non-profit way.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.tomass1996.MCPwdRecovery;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 * @author tomass1996
 */
public class Util {
    
    public static String[] readUserPwd(File input, Cipher cipher) throws Exception{
        DataInputStream dis = new DataInputStream(new CipherInputStream(new FileInputStream(input), cipher));
        String usr = dis.readUTF();
        String pwd = dis.readUTF();
        dis.close();
        return new String[]{usr, pwd};
    }
    
    public static Cipher getCipher(int mode, String pwd) throws Exception{
        Random rand = new Random(43287234L);
        byte[] salt = new byte[8];
        rand.nextBytes(salt);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDes").generateSecret(new PBEKeySpec(pwd.toCharArray()));
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDes");
        cipher.init(mode, pbeKey, pbeParamSpec);
        return cipher;
    }
    
    public static File getAppDir(String s)
	{
		String s1 = System.getProperty("user.home", ".");
		File file;
		switch (enumOSMappingArray[getOs().ordinal()])
		{
			case 1:
			case 2:
				file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
				break;
	
			case 3:
				String s2 = System.getenv("APPDATA");
				if (s2 != null)
				{
					file = new File(s2, (new StringBuilder()).append(".").append(s).append('/').toString());
				}
				else
				{
					file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
				}
				break;
	
			case 4:
				file = new File(s1, (new StringBuilder()).append("Library/Application Support/").append(s).toString());
				break;
	
			default:
				file = new File(s1, (new StringBuilder()).append(s).append('/').toString());
				break;
		}
		if (!file.exists() && !file.mkdirs())
		{
			throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
		}
		else
		{
			return file;
		}
	}
	
	private static EnumOS getOs()
	{
		String s = System.getProperty("os.name").toLowerCase();
		if (s.contains("win"))
		{
			return EnumOS.windows;
		}
		if (s.contains("mac"))
		{
			return EnumOS.macos;
		}
		if (s.contains("solaris"))
		{
			return EnumOS.solaris;
		}
		if (s.contains("sunos"))
		{
			return EnumOS.solaris;
		}
		if (s.contains("linux"))
		{
			return EnumOS.linux;
		}
		if (s.contains("unix"))
		{
			return EnumOS.linux;
		}
		else
		{
			return EnumOS.unknown;
		}
	}
	
	public static final int enumOSMappingArray[];

    static
    {
        enumOSMappingArray = new int[EnumOS.values().length];
        try
        {
            enumOSMappingArray[EnumOS.linux.ordinal()] = 1;
        }
        catch (NoSuchFieldError nosuchfielderror) { }
        try
        {
            enumOSMappingArray[EnumOS.solaris.ordinal()] = 2;
        }
        catch (NoSuchFieldError nosuchfielderror1) { }
        try
        {
            enumOSMappingArray[EnumOS.windows.ordinal()] = 3;
        }
        catch (NoSuchFieldError nosuchfielderror2) { }
        try
        {
            enumOSMappingArray[EnumOS.macos.ordinal()] = 4;
        }
        catch (NoSuchFieldError nosuchfielderror3) { }
    }
	
}

enum EnumOS
{
    linux("linux", 0),
    solaris("solaris", 1),
    windows("windows", 2),
    macos("macos", 3),
    unknown("unknown", 4);

    @SuppressWarnings("unused")
	private static final EnumOS allOSes[] = (new EnumOS[] {
        linux, solaris, windows, macos, unknown
    });
    
    private EnumOS(String s, int i)
    {
    }
    
}
