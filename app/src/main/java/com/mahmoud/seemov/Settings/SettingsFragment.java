package com.mahmoud.seemov.Settings;


import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;


import com.mahmoud.seemov.R;


public class SettingsFragment extends PreferenceFragmentCompat  implements OnSharedPreferenceChangeListener
{


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        String[] entries = getResources().getStringArray(R.array.pref_genre_types_entries);
        setPreferencesFromResource(R.xml.pref_settings,rootKey);

        final String _KEY = getString(R.string.pref_sort_key);
        ListPreference sortOrderPref = (ListPreference) findPreference(_KEY);

        if(sortOrderPref!=null)
        {

            String selectedEntry = (String) sortOrderPref.getEntry();

            if(selectedEntry==null)
            {
                selectedEntry = getString(R.string.popular_item_entries);
                sortOrderPref.setValueIndex(0);
            }
            sortOrderPref.setSummary(selectedEntry);
        }

        String sort_Key = getString(R.string.sort_key);
        String genre_Key = getString(R.string.genre_key);

        ListPreference sortOrderGenrePref = (ListPreference) findPreference(sort_Key);
        ListPreference genrePref = (ListPreference) findPreference(genre_Key);

        if(genrePref!=null)
        {
            String selectedEntry = (String) genrePref.getEntry();

            if(selectedEntry==null)
            {
                selectedEntry = entries[0];

                genrePref.setValueIndex(0);
            }
            genrePref.setSummary(selectedEntry);



        }

        if(sortOrderGenrePref!=null)
        {


            String selectedEntry = (String) sortOrderGenrePref.getEntry();

            if(selectedEntry==null)
            {
                selectedEntry = getString(R.string.top_rated_item_entries);
                sortOrderGenrePref.setValueIndex(1);
            }
            sortOrderGenrePref.setSummary(selectedEntry);


        }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {



        final String _KEY = getString(R.string.pref_sort_key);
        ListPreference sortOrderPref = (ListPreference) findPreference(_KEY);

        if(sortOrderPref!=null)
        {
           String selectedEntry = (String) sortOrderPref.getEntry();
            sortOrderPref.setSummary(selectedEntry);

            PreferenceCategory preference = (PreferenceCategory) findPreference(getString(R.string.pref_genre_key));
            if(selectedEntry.equals(getString(R.string.pref_genre_title)))
            {
                preference.setEnabled(true);

                String sort_Key = getString(R.string.sort_key);
                String genre_Key = getString(R.string.genre_key);

                ListPreference sortOrderGenrePref = (ListPreference) findPreference(sort_Key);
                ListPreference genrePref = (ListPreference) findPreference(genre_Key);

                if(sortOrderGenrePref!=null){
                    selectedEntry = (String) sortOrderGenrePref.getEntry();
                    sortOrderGenrePref.setSummary(selectedEntry);
                }

                if(genrePref !=null)
                {
                    selectedEntry  = (String) genrePref.getEntry();
                    genrePref.setSummary(selectedEntry);


                }


            }

            else preference.setEnabled(false);


        }



    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        Preference preference = findPreference(getString(R.string.pref_genre_key));

        final String _KEY = getString(R.string.pref_sort_key);
        ListPreference sortOrderPref = (ListPreference) findPreference(_KEY);
        String selectedEntry = (String) sortOrderPref.getEntry();

        if(selectedEntry.equals(getString(R.string.pref_genre_title)))

            preference.setEnabled(true);

        else preference.setEnabled(false);


    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }



}
