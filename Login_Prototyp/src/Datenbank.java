import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 */

/**
 * @author Piel
 *
 */
public class Datenbank {
	private static Connection myCon = null;
	private static Statement mySQL = null;
	private static ResultSet myRS = null;
	private static String url = "";
	private static String user = "";
	private static String pwd = "";
	
	public static boolean testDB(String url, String user, String pwd){
		boolean ret = false;
		try {
			myCon=DriverManager.getConnection(url, user, pwd);
			Datenbank.url = url;
			Datenbank.user = user;
			Datenbank.pwd = pwd;
			ret = true;
			myCon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String[] getLogin(String name) {
		try {
			myCon = DriverManager.getConnection(url, user, pwd);
			mySQL = myCon.createStatement();
			String statment = "SELECT passwort, beschreibung FROM login WHERE name='" + name + "'";
			myRS = mySQL.executeQuery(statment);
			String[] arr = new String[2];
			if(myRS.next()) {
				arr[0] = myRS.getString("passwort");
				arr[1] = myRS.getString("beschreibung");
			}
			mySQL = null;
			myRS = null;
			myCon.close();
			return arr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void changePassword(String name, String pass, String newPass) {
		String[] arr = getLogin(name);
		
		final String strPssword = arr[1];
        AES.setKey(strPssword);
       
        final String strToDecrypt =  arr[0];
        
        AES.decrypt(strToDecrypt.trim());
        
        if(pass.equals(AES.getDecryptedString())) {
        	url = "jdbc:mysql://localhost/mysql";
        	
        	try {
        		myCon = DriverManager.getConnection(url, user, pwd);
				mySQL = myCon.createStatement();
				String statment = "update user set password=PASSWORD('" + newPass + "') where User='root';";
				mySQL.executeUpdate(statment);
				mySQL = null;
				myRS = null;
				myCon.close();
				url = "jdbc:mysql://localhost/loginDB";
				myCon = DriverManager.getConnection(url, user, pwd);
				System.out.println(name);
				System.out.println(newPass);
				AES.encrypt(newPass);
				statment = "update login set passwort='" + AES.getEncryptedString() + "' where name='" + name + "';";
				mySQL.execute(statment);
				mySQL = null;
				myRS = null;
				System.out.println("Passwort geändert!!!!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
        	System.out.println("Falsche Anmeldedaten, du Noob!!!!");
        }
	}
}
