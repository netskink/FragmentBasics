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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


/* in order to handle fragment to activity communication, the activity must implement
a listener as shown below in the implements clause.


DELIVER A MESSAGE TO A FRAGMENT

The host activity can deliver messages to a fragment by capturing the Fragment
instance with findFragmentById(), then directly call the fragment's public methods.

For instance, imaging that the activity shown above may contain another fragment that's
used to display the item specified by the data returned in the above callback method.
In this case, the activity can pass the information
received in the callback method to the other fragment that will display the item:

<see onArticleSelected() routine>

 */
public class MainActivity extends FragmentActivity 
        implements HeadlinesFragment.OnHeadlineSelectedListener {

    /** Called when the activity is first created. */

    // Inside your activity, call getSupportFragmentManager() to
    // get a FragmentManager using the support Library API's.  Then
    // call beginTransaction() to create a FragmentTransaction and call
    // add() to add a fragment.

    // You can perform multiple fragment transaction for the activity using the same
    // FragmentTransaction.  When you're ready to make the changes, you must call commit().
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // These two lines are all that is necessary to add
        // a fragment to an activity.  Depending upon screen
        // size it adds either the normal or large version
        // of news_articles layout.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            HeadlinesFragment firstFragment = new HeadlinesFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    /*
    In order to receive callbacks from the fragment, the activity that hosts the fragment must
    implement the interface defined in the fragment class.

    For example, the following activity implements the interface from the above example.

    Remember the headlines fragment calls this callback and then it in turn calls down
    to the article fragment to change its content.
     */
    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        ArticleFragment articleFrag = (ArticleFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);

        } else {
            // This is an example of replcing one fragment with another.
            // Its similar procedure, but you call replace() instead of add().
            // Keep in mind that when you perform fragment transactions such as replace
            // or remove one, its often appropriate the allow the user to navigate
            // backwards and undo the change.  To allow the user to navigate backwards
            // through fragment transactions, you must call addToBackStack() before you commit
            // the FragmentTransaction.




            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            // The addToBackStack() method takes an optional string parameter that specifies
            // aunique name for the transaction.  The name isn't needed unless you plan to perform
            // advanced fragment operations using the FragmentManager.BackStackEntry API's.
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}