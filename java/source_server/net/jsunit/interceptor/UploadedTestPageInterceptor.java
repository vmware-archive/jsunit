package net.jsunit.interceptor;

import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork.Action;
import net.jsunit.uploaded.UploadedReferencedJsFile;
import net.jsunit.uploaded.UploadedTestPage;
import net.jsunit.uploaded.UploadedTestPageFactory;
import net.jsunit.utility.FileUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class UploadedTestPageInterceptor extends AbstractUploadInterceptor {

    protected void execute(Action targetAction) throws Exception {
        HttpServletRequest request = request();
        if (request instanceof MultiPartRequestWrapper) {
            MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
            File[] uploadedTestPages = wrapper.getFiles("testPageFile");
            if (uploadedTestPages != null && uploadedTestPages.length == 1) {
                File uploadedTestPageFile = uploadedTestPages[0];
                File[] uploadedReferencedJsFiles = wrapper.getFiles("referencedJsFiles");
                String[] uploadedReferencedJsFileNames = wrapper.getFileNames("referencedJsFiles");
                UploadedTestPage page = createUploadedTestPageFrom(
                        uploadedTestPageFile, uploadedReferencedJsFiles, uploadedReferencedJsFileNames);
                page.write();
                uploadedTestPageFile.delete();
                if (uploadedReferencedJsFiles != null)
                    for (File file : uploadedReferencedJsFiles)
                        file.delete();
                setUrlOfTestPageOn(targetAction, page);
            }
        }
    }

    private UploadedTestPage createUploadedTestPageFrom(
            File uploadedTestPageFile, File[] uploadedReferencedJsFiles, String[] uploadedReferencedJsFileNames) {
        String contents = FileUtility.read(uploadedTestPageFile);
        UploadedReferencedJsFile[] referencedJsFiles;
        if (uploadedReferencedJsFiles != null) {
            referencedJsFiles = new UploadedReferencedJsFile[uploadedReferencedJsFiles.length];
            for (int index = 0; index < uploadedReferencedJsFiles.length; index++)
                referencedJsFiles[index] = new UploadedReferencedJsFile(
                        uploadedReferencedJsFileNames[index],
                        FileUtility.read(uploadedReferencedJsFiles[index]),
                        index
                );
        } else
            referencedJsFiles = new UploadedReferencedJsFile[]{};
        return new UploadedTestPageFactory().fromUploaded(contents, referencedJsFiles);
    }

}
