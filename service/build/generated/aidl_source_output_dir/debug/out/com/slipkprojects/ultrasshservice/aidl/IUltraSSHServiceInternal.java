/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.romzkie.ultrasshservice.aidl;
/**
 * Created by arne on 15.11.16.
 */
public interface IUltraSSHServiceInternal extends android.os.IInterface
{
  /** Default implementation for IUltraSSHServiceInternal. */
  public static class Default implements com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal
  {
    /**
         * @param replaceConnection True if the VPN is connected by a new connection.
         * @return true if there was a process that has been send a stop signal
         */
    @Override public void stopVPN() throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal
  {
    private static final java.lang.String DESCRIPTOR = "com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal interface,
     * generating a proxy if needed.
     */
    public static com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal))) {
        return ((com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal)iin);
      }
      return new com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_stopVPN:
        {
          data.enforceInterface(descriptor);
          this.stopVPN();
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      /**
           * @param replaceConnection True if the VPN is connected by a new connection.
           * @return true if there was a process that has been send a stop signal
           */
      @Override public void stopVPN() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopVPN, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().stopVPN();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal sDefaultImpl;
    }
    static final int TRANSACTION_stopVPN = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.romzkie.ultrasshservice.aidl.IUltraSSHServiceInternal getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  /**
       * @param replaceConnection True if the VPN is connected by a new connection.
       * @return true if there was a process that has been send a stop signal
       */
  public void stopVPN() throws android.os.RemoteException;
}
