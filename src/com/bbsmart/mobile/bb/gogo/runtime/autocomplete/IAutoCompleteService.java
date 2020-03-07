package com.bbsmart.mobile.bb.gogo.runtime.autocomplete;

public interface IAutoCompleteService {

    void addAutoCompleteListener(IAutoCompleteListener listener);
    void removeAutoCompleteListener(IAutoCompleteListener listener);
    void requestAutoComplete(String request);
    void shutdown();

}
