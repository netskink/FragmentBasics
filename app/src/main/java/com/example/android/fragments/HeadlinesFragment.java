/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/*
COMMUNICATING WITH OTHER FRAGMENTS

In order to reuse the Fragment UI components, you should build each as a completely
self-contained, modular component that defines its own layout and behaviour.  once you have
defined these reusable Fragments, you can associate them with the application logic to realize
the overall composite UI.

Often you will want one fragment to communicate with another, for example
to change the content based on a user event.  All fragment-to-fragment communication is done
through the associated activity.  Two fragments should never communicate directly.


DEFINE AND INTERFACE

To allow a Fragment to communicate up to its activity, you can define an interface in the Fragment
class and implement it within the Activity.  The fragment captures the interface implementation during
its onAttach() lifecycle method and can then call the Interface methods in order to communicate with
the Activity.


Here is an example of Fragment to Activity communication:
1. The Fragment implements OnCLASSNAMESelectedListener
2. In onAttach() make sure the container activity has
   implemented the callback interface.

Now, the fragment can deliver messages to the activity by calling
the onArticleSelected() method (or other methods in the interface)
using mCallback instance of the OnHeadlineSelectedListener interface.

For example, see onListItemClick below which is called when a user
clicks on a list item in the fragment.  The fragment uses the callback
interface to deliver the event to the parent activity.

<See the MainActivity for the interface implementation and how to handle
the Fragment to Activity communication in the activity.
 */



public class HeadlinesFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;

    // This is the first part required to implement
    // Fragment to Activity communication.
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onArticleSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines));
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    // This is the second part of the example on how to
    // implement Fragment to Activity communication:
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /*
    This shows how the fragment can deliver the event to the parent activity
    establishing fragment to activity communication.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}