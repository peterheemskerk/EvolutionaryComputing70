//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.Vector;

public class ConsertTestSecurity extends SecurityManager {
    private Vector<String> permitted_reads_;
    private String target_;

    public ConsertTestSecurity(String var1) {
        this.target_ = var1;
        this.permitted_reads_ = new Vector();
    }

    public void addPermittedRead(String var1) {
        this.permitted_reads_.add(var1);
    }

    public void checkRead(String var1) {
        if (!this.permitted_reads_.contains(var1)) {
            throw new SecurityException("Attempting to read file! (" + var1 + ")");
        }
    }

    public void checkRead(String var1, Object var2) {
        if (!this.permitted_reads_.contains(var1)) {
            throw new SecurityException("Attempting to read file! (" + var1 + ")");
        }
    }

    public void checkRead(FileDescriptor var1) {
        throw new SecurityException("Attempting to read file! (" + var1.toString() + ")");
    }

    public void checkWrite(String var1) {
        throw new SecurityException("Attempting to write file! (" + var1 + ")");
    }

    public void checkWrite(FileDescriptor var1) {
        throw new SecurityException("Attempting to write file! (" + var1.toString() + ")");
    }

    public void checkDelete(String var1) {
        throw new SecurityException("Attempting to delete file! (" + var1 + ")");
    }

    public void checkAccept(String var1, int var2) {
        throw new SecurityException("Attempting to accept a connection! (" + var1 + ":" + var2 + ")");
    }

    public void checkConnect(String var1, int var2) {
        throw new SecurityException("Attempting to start a connection! (" + var1 + ":" + var2 + ")");
    }

    public void checkConnect(String var1, int var2, Object var3) {
        throw new SecurityException("Attempting to start a connection! (" + var1 + ":" + var2 + ")");
    }

    public void checkListen(int var1) {
        throw new SecurityException("Attempting to listen to a port! (" + var1 + ")");
    }

    public void checkMulticast(InetAddress var1) {
        throw new SecurityException("Attempting to use an IP multicast! (" + var1.getCanonicalHostName() + ")");
    }

    public void checkSetFactory() {
        throw new SecurityException("Attempting to access the socket factory!");
    }

    public void checkExec(String var1) {
        throw new SecurityException("Attempting to create a new process! ('" + var1 + "')");
    }

    public void checkLink(String var1) {
        throw new SecurityException("Attempting to load a binary! ('" + var1 + "')");
    }

    public void checkPropertiesAccess() {
        throw new SecurityException("Attempting to access system properties!");
    }

    public void checkPrintJobAccess() {
        throw new SecurityException("Attempting to request a print job!");
    }

    public void checkCreateClassLoader() {
        throw new SecurityException("Attempting to create a class loader!");
    }

    public void checkPermission(Permission var1) {
        if (var1.getActions().contains("write")) {
            throw new SecurityException("Requesting permission to write system property! (" + var1.getName() + ")");
        }
    }
}
