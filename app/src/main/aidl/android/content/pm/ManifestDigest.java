package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.util.Arrays;
import java.util.jar.Attributes;

/**
 * Represents the manifest digest for a package. This is suitable for comparison
 * of two packages to know whether the manifests are identical.
 *
 * @hide
 */
public class ManifestDigest implements Parcelable {
    /** The digest of the manifest in our preferred order. */
    private final byte[] mDigest;

    /** Digest field names to look for in preferred order. */
    private static final String[] DIGEST_TYPES = {
            "SHA1-Digest", "SHA-Digest", "MD5-Digest",
    };

    /** What we print out first when toString() is called. */
    private static final String TO_STRING_PREFIX = "ManifestDigest {mDigest=";

    ManifestDigest(byte[] digest) {
        mDigest = digest;
    }

    private ManifestDigest(Parcel source) {
        mDigest = source.createByteArray();
    }

    static ManifestDigest fromAttributes(Attributes attributes) {
        if (attributes == null) {
            return null;
        }

        String encodedDigest = null;

        for (int i = 0; i < DIGEST_TYPES.length; i++) {
            final String value = attributes.getValue(DIGEST_TYPES[i]);
            if (value != null) {
                encodedDigest = value;
                break;
            }
        }

        if (encodedDigest == null) {
            return null;
        }

        final byte[] digest = Base64.decode(encodedDigest, Base64.DEFAULT);
        return new ManifestDigest(digest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ManifestDigest)) {
            return false;
        }

        final ManifestDigest other = (ManifestDigest) o;

        return this == other || Arrays.equals(mDigest, other.mDigest);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mDigest);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(TO_STRING_PREFIX.length()
                + (mDigest.length * 3) + 1);

        sb.append(TO_STRING_PREFIX);

        final int N = mDigest.length;
        for (int i = 0; i < N; i++) {
            final byte b = mDigest[i];
            appendByteAsHex(sb, b, false);
        }
        sb.append('}');

        return sb.toString();
    }
    
	//byte 与 int 的相互转换
	public static byte intToByte(int x) {
		return (byte) x;
	}
	
	public static int byteToInt(byte b) {
		//Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
		return b & 0xFF;
	}
	

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(mDigest);
    }

    public static final Creator<ManifestDigest> CREATOR
            = new Creator<ManifestDigest>() {
        public ManifestDigest createFromParcel(Parcel source) {
            return new ManifestDigest(source);
        }

        public ManifestDigest[] newArray(int size) {
            return new ManifestDigest[size];
        }
    };
    
    private static final char[] TENS = {
        '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
        '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
        '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
        '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
        '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
        '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
        '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
        '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
        '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
        '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'
    };
    /** Ones [i] contains the tens digit of the number i, 0 <= i <= 99. */
    private static final char[] ONES = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };
    
    /**
     * The digits for every supported radix.
     */
    private static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static final char[] UPPER_CASE_DIGITS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    public static StringBuilder appendByteAsHex(StringBuilder sb, byte b, boolean upperCase) {
        char[] digits = upperCase ? UPPER_CASE_DIGITS : DIGITS;
        sb.append(digits[(b >> 4) & 0xf]);
        sb.append(digits[b & 0xf]);
        return sb;
    }
  
    public static String bytesToHexString(byte[] bytes, boolean upperCase) {
        char[] digits = upperCase ? UPPER_CASE_DIGITS : DIGITS;
        char[] buf = new char[bytes.length * 2];
        int c = 0;
        for (byte b : bytes) {
            buf[c++] = digits[(b >> 4) & 0xf];
            buf[c++] = digits[b & 0xf];
        }
        return new String(buf);
    }
 
}