package net.jsunit.interceptor;

import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork.Action;
import net.jsunit.upload.ReferencedJsFile;
import net.jsunit.upload.TestPage;
import net.jsunit.upload.TestPageFactory;
import net.jsunit.utility.FileUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class UploadedTestPageInterceptor extends AbstractUploadInterceptor {

    protected void execute(Action targetAction) throws Exception {
        HttpServletRequest request = request();
        if (request instanceof MultiPartRequestWrapper) {
            MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
            File[] uploadedTestPages = wrapper.getFiles("testPageFile");
            if (uploadedTestPages != null && uploadedTestPages.length > 0) {
                File uploadedTestPageFile = uploadedTestPages[0];
                String contents = FileUtility.read(uploadedTestPageFile);
                File[] uploadedReferencedJsFiles = wrapper.getFiles("referencedJsFiles");
                String[] uploadedReferencedJsFileNames = wrapper.getFileNames("referencedJsFiles");
                ReferencedJsFile[] referencedJsFiles;
                if (uploadedReferencedJsFiles != null) {
                    referencedJsFiles = new ReferencedJsFile[uploadedReferencedJsFiles.length];
                    for (int index = 0; index < uploadedReferencedJsFiles.length; index++)
                        referencedJsFiles[index] = new ReferencedJsFile(
                                uploadedReferencedJsFileNames[index],
                                FileUtility.read(uploadedReferencedJsFiles[index]),
                                index
                        );
                } else
                    referencedJsFiles = new ReferencedJsFile[]{};
                TestPage page = new TestPageFactory().fromUploaded(contents, referencedJsFiles);
                page.write();
                uploadedTestPageFile.delete();
                if (uploadedReferencedJsFiles != null)
                    for (File file : uploadedReferencedJsFiles)
                        file.delete();
                setUrlOfTestPageOn(targetAction, page);
            }
        }
    }

}
