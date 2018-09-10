import com.sparetime.common.util.DESedeUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;

/**
 * Created by muye on 17/8/28.
 */
public class Base64Test {

    @Test
    public void test() throws Exception{

        byte[]   bytesEncoded = Base64.encodeBase64("Admin123#" .getBytes());
        System.out.println("ecncoded value is " + new String(bytesEncoded ));

        // Decode data on other side, by processing encoded data
        byte[] valueDecoded= Base64.decodeBase64(bytesEncoded);
        System.out.println("Decoded value is " + new String(valueDecoded));


        System.out.println(DESedeUtil.encrypt("Admin123#", "JI3$JU4%ONH2@JN4@JOI#NO$", "gpg@123#", "/CBC/PKCS5Padding"));
        System.out.println(DESedeUtil.decrypt("JEfoXg0tf8M8ahqlBYpPNA==", "db_password_encrypt_key@#", "test@123", "/CBC/PKCS5Padding"));
    }
}


