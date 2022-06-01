package com.example.demo;

import com.example.demo.FtpFile;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FtpServiceNumberTwo {
    private final String server;
    private final int port;
    private final String user;
    private final String password;
    private FTPClient ftp;

    public FtpServiceNumberTwo(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.enterLocalPassiveMode();
        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }
        ftp.login(user, password);
    }

    public void close() throws IOException {
        ftp.disconnect();
    }

    List<FtpFile> list = new ArrayList<>();
    List<FtpFile> listRoot = new ArrayList<>();

    public void getRootItems() throws IOException {
        for (FTPFile ftpFile : ftp.listFiles()) {
            list.add(new FtpFile(ftpFile.getName(), ftpFile.getSize(), null, ftpFile.isDirectory()));
            listRoot.add(new FtpFile(ftpFile.getName(), ftpFile.getSize(), null, ftpFile.isDirectory()));
        }
        for (FtpFile ftpFile : listRoot) {
            if (ftpFile.isDirectory()) {
                getTree(ftpFile);
            }
        }
    }
    public List<FtpFile> getList() {
        return list;
    }

    public void getTree(FtpFile file) throws IOException {
      FTPFile[] list5 = ftp.listFiles("htdocs/123");
        for (FTPFile ftpFile : list5) {
            System.out.println(ftpFile);
        }
        for (FTPFile ftpFile : ftp.listFiles(file.getName())) {
            System.out.println(ftpFile);
            if (ftpFile.isDirectory()) {
                list.add(new FtpFile(ftpFile.getName(), ftpFile.getSize(), file, true));
                getTree(new FtpFile(ftpFile.getName(), ftpFile.getSize(), file, true));
            } else {
                list.add(new FtpFile(ftpFile.getName(), ftpFile.getSize(), file, false));
            }
        }
    }

    public FTPFile[] getListFTPFilesByName(String name) throws IOException {
        return ftp.listFiles(name);
    }


}


