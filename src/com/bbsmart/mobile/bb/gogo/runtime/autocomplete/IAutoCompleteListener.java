package com.bbsmart.mobile.bb.gogo.runtime.autocomplete;

import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.adapters.IModelAdapter;

public interface IAutoCompleteListener {

    void onAutoCompleteResponse(Vector results, IModelAdapter adapter);

}
