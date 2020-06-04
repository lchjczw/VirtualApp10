package java.io;
import com.lody.virtual.helper.utils.VLog;
public class FileNotFoundException extends IOException {
    private static final long serialVersionUID = -897856973823710492L;
    public FileNotFoundException() {
        VLog.e("===================   FileNotFoundException kook:");
    }
    public FileNotFoundException(String detailMessage) {
        super(detailMessage);
        VLog.e("================  FileNotFoundException detailMessage:"+detailMessage);
    }
}
