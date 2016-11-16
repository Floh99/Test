import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Diese Klasse bietet die Möglichkeit Strings einfach zu verschlüsseln.
 * <strong>WICHTIG:</strong> Apache Commons Codes wird benötigt!!!!
 * @author Piel, Goyer
 */
public class AES {
    
    private static SecretKeySpec secretKey ;
    private static byte[] key ;
    
    private static String decryptedString;
    private static String encryptedString;
    
    /**
     * Mit dieser Methode wird der Verschlüsselungsschlüssel gesetzt
     * und verarbeitet.
     * @param myKey Einen Verschlüsselungsschlüssel angeben
     */
    public static void setKey(String myKey){
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-512");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit;
            secretKey = new SecretKeySpec(key, "AES");
            
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gibt einen entschlüsselten String zurück.
     * @return Der entschlüsselte String
     */
    public static String getDecryptedString() {
        return decryptedString;
    }
    
    /**
     * Setzt einen verschlüsselten String
     * @param decryptedString Einen entschlüsselten String angeben
     */
    public static void setDecryptedString(String decryptedString) {
        AES.decryptedString = decryptedString;
    }
    
    /**
     * Gibt einen verschlüsselten String zurück.
     * @return Ein verschlüsselter String
     */
    public static String getEncryptedString() {
        return encryptedString;
    }
    
    /**
     * Setzt einen verschlüsselten String.
     * @param encryptedString verschlüsselter String
     */
    public static void setEncryptedString(String encryptedString) {
        AES.encryptedString = encryptedString;
    }
    
    /**
     * Diese Methode verschlüsselt einen einfachen String mit dem vorher gesetzten 
     * Schlüssel und dem Verschlüsselungsverfahren.
     * @param strToEncrypt String welcher verschlüsselt werden soll
     * @return Gibt den Verschlüsselten String zurück
     */
    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        
        return null;
    }
    
    /**
     * Diese Methode entschlüsselt einen verschlüsselten String.
     * @param strToDecrypt Einen verschlüsselten String eingeben
     * @return Gibt einen Klartextstring zurück.
     */
    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));
            
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: "+e.toString());
        }
        
        return null;
    }
}