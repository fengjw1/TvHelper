package android.content.pm;

/**
 * Created by maogl on 2017/8/29.
 */

public interface IPackageInstallObserver2 extends android.os.IInterface {

    public abstract static class Stub extends android.os.Binder implements IPackageInstallObserver2 {
        public Stub() {
            throw new RuntimeException("Stub!");
        }

        public static IPackageInstallObserver2 asInterface(android.os.IBinder obj) {
            throw new RuntimeException("Stub!");
        }

        public android.os.IBinder asBinder() {
            throw new RuntimeException("Stub!");
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
                throws android.os.RemoteException {
            throw new RuntimeException("Stub!");
        }
    }

    public abstract void packageInstalled(String packageName, int returnCode)
            throws android.os.RemoteException;
}
