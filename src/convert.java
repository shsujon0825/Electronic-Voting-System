
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class convert {
    
    public static String algo = "AES";
    public byte[] keyValue;
    
    public convert(byte key[]){
        keyValue = key;
        
    }
    
    public Key generateKey() throws Exception{
        Key key = new SecretKeySpec(keyValue, algo);
        return key;
    }
    public String encrypt(String Data) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algo);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedvalue = new BASE64Encoder().encode(encVal);
        return encryptedvalue;
    }
    public String decpryt(String Data) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance(algo);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decoderValue = new BASE64Decoder().decodeBuffer(Data);
        byte[] decVal = c.doFinal(decoderValue);
        String decryptedValue = new String(decVal);
        return decryptedValue;
        
    }
            
}
