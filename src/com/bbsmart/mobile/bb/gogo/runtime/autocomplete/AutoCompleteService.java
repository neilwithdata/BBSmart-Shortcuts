package com.bbsmart.mobile.bb.gogo.runtime.autocomplete;

import java.util.Enumeration;
import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.adapters.IModelAdapter;
import com.bbsmart.mobile.bb.gogo.adapters.SimpleStringAdapter;
import com.bbsmart.mobile.bb.gogo.runtime.AddressBook;
import com.bbsmart.mobile.bb.gogo.runtime.AddressBookStatusListener;

public abstract class AutoCompleteService implements AddressBookStatusListener, IAutoCompleteService {

    private static class RequestParameter {
        public volatile String value = null;
    }

    private static final int STATE_CANCELLED    = 0;
    private static final int STATE_IDLE         = 1;
    private static final int STATE_RUNNING      = 2;
    private static final int STATE_SHUTDOWN     = 3;
    
    protected AddressBook addressbook = AddressBook.instance;
    protected String request = null;

    private IModelAdapter adapter = new SimpleStringAdapter();
    private RequestParameter requestParameter = new RequestParameter();
    private Thread requestProcessingThread;
    private Vector listeners = new Vector();
    
    private volatile int state = STATE_IDLE;
    
    public AutoCompleteService() {
        requestProcessingThread = new Thread() {
            public void run() {
                while (true) {
                    synchronized (requestParameter) {
                        if (requestParameter.value == null) {
                            try {
                                requestParameter.wait();
                            }
                            catch (InterruptedException ignore) {
                                System.out.println("AutoCompleteService interrupted");
                            }
                        }
                        
                        if (state == STATE_SHUTDOWN) {
                            return;
                        }

                        request = requestParameter.value;
                        
                        requestParameter.value = null;
                    }
                    
                    try {
                        abstractStartAutoCompleteLookup();
                    }
                    catch (RuntimeException e) {
                        System.out.println("AutoCompleteService runtime exception: " + e.toString());
                    }
                }
            }
        };
        
        requestProcessingThread.start();
    }

    public void addAutoCompleteListener(IAutoCompleteListener listener) {
        if (! listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }
    
    public void onAddressBookLoadStarted() {
        // Empty.  Override if desired.
    }
    
    public void onAddressBookLoadCompleted() {
        // Empty.  Override if desired.
    }
    
    public void removeAutoCompleteListener(IAutoCompleteListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }
    
    public void requestAutoComplete(String request) {
        if (isRunning()) {
            abstractCancelAutoCompleteLookup();
        }

        synchronized (requestParameter) {
            requestParameter.value = request;
            requestParameter.notifyAll();
        }
    }
    
    public void shutdown() {
        setState(STATE_SHUTDOWN);
        
        addressbook.removeAddressBookStatusListener(this);
        
        synchronized (requestParameter) {
            requestParameter.notifyAll();
        }
    }
    
    protected abstract void cancelAutoCompleteLookup();
    
    protected abstract void doAutoCompleteLookup();
    
    protected final boolean isCancelled() {
        return state == STATE_CANCELLED;
    }
    
    protected final boolean isIdle() {
        return state == STATE_IDLE;
    }

    protected final boolean isRunning() {
        return state == STATE_RUNNING;
    }
    
    protected void notifyListeners(Vector results) {
        // Make sure listeners get a non-null Vector...
        if (results == null) {
            results = new Vector();
        }

        for (Enumeration en = listeners.elements(); en.hasMoreElements();) {
            IAutoCompleteListener listener = (IAutoCompleteListener) en.nextElement();
            
            listener.onAutoCompleteResponse(results, adapter);
        }
    }

    protected void setModelAdapter(IModelAdapter adapter) {
        this.adapter = adapter;
    }
    
    private void abstractCancelAutoCompleteLookup() {
        setState(STATE_CANCELLED);

        cancelAutoCompleteLookup();
    }

    private void abstractStartAutoCompleteLookup() {
        setState(STATE_RUNNING);
        
        doAutoCompleteLookup();
    }
    
    private void setState(int newState) {
        state = newState;
    }
}
