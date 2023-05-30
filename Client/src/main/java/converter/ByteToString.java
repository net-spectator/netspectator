package converter;

public class ByteToString {
    public static String byteToString(float digit) {
        if (digit < 1000) {
            return String.format("%.0f byte", digit);
        } else if (digit < 1000000) {
            return String.format("%.0f kb", digit / 1000);
        } else if (digit < 1000000000) {
            return String.format("%.2f mb", digit / 1000000);
        } else if (digit < 1000000000000.0) {
            return String.format("%.2f Gb", digit / 1000000000.0);
        }
        return digit + " bytes";
    }
}
