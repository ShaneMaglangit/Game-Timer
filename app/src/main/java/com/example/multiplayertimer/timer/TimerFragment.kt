package com.example.multiplayertimer.timer

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.multiplayertimer.MainActivity
import com.example.multiplayertimer.R
import com.example.multiplayertimer.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var timerViewModel: TimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)
        sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        timerViewModel = ViewModelProvider(this, TimerViewModelFactory(sharedPreferences, activity!!.application)).get(TimerViewModel::class.java)

        timerViewModel.playerToKickPos.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                var gameIsPaused = timerViewModel.isPaused

                if(!gameIsPaused) timerViewModel.toggleGameState(false)

                AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Dialog))
                    .setTitle("Remove player")
                    .setMessage("Are you sure you want to remove this player?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        timerViewModel.continueKick()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        timerViewModel.cancelKick()
                        dialog.dismiss()
                    }
                    .setOnDismissListener {
                        val activity = requireActivity() as MainActivity
                        if(!gameIsPaused) timerViewModel.toggleGameState(false)
                        activity.enableImmersiveMode()
                    }
                    .create()
                    .show()
            }
        })

        binding.buttonCenter.setOnLongClickListener {
            if(timerViewModel.gameStarted) {
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_AppCompat_Dialog))
                    .setTitle("Restart timers")
                    .setMessage("Are you sure you want to reset the timers?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                        timerViewModel.resetGameState()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setOnDismissListener {
                        val activity = requireActivity() as MainActivity
                        activity.enableImmersiveMode()
                    }
                    .create()
                    .show()
            }
            true
        }

        binding.timerViewModel = timerViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
