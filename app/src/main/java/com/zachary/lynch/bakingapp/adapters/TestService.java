package com.zachary.lynch.bakingapp.adapters;

import android.content.Intent;
import android.widget.RemoteViewsService;



public class TestService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TestAdapterForWidget(this, intent);
    }
}
