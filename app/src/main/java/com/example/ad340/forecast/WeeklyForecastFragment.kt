package com.example.ad340.forecast

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ad340.*
import com.example.ad340.api.DailyForecast
import com.example.ad340.api.WeeklyForecast

import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class WeeklyForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_weekly_forecast, container, false)

        val zipcode = arguments?.getString(KEY_ZIPCODE) ?: ""

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val locationEntryButton: FloatingActionButton = view.findViewById(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())
        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager){ forecast ->
            showForecastDetails(forecast)
        }
        forecastList.adapter = dailyForecastAdapter

        val weeklyForecastObserver = Observer<WeeklyForecast> { weeklyForecast ->
            //update our list adapter
            dailyForecastAdapter.submitList(weeklyForecast.daily)
        }
        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> {savedLocation ->
            when(savedLocation) {
                is Location.Zipcode -> forecastRepository.loadWeeklyForecast(savedLocation.zipcode)
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return view
    }

    private fun showForecastDetails(forecast: DailyForecast){
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(temp, description)
        findNavController().navigate(action)
    }

    private fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    companion object{
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String) : WeeklyForecastFragment {
            val fragment = WeeklyForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)
            fragment.arguments = args

            return fragment
        }
    }
}
