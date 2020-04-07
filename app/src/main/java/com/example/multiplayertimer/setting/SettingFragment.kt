package com.example.multiplayertimer.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.multiplayertimer.MainActivity
import com.example.multiplayertimer.R
import com.example.multiplayertimer.databinding.FragmentSettingBinding
import com.example.multiplayertimer.databinding.TimeDialogBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        sharedPreferences = activity!!.getPreferences(Context.MODE_PRIVATE)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(sharedPreferences)).get(SettingViewModel::class.java)

        settingViewModel.navigateToTimer.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().navigate(R.id.action_settingFragment_to_timerFragment)
                settingViewModel.navigateToTimerComplete()
            }
        })

        binding.editStartTime.setOnClickListener {
            val dialogBinding = TimeDialogBinding.inflate(inflater)
            dialogBinding.pickerMinutes.value = settingViewModel.getMinutesFromStartTime()
            dialogBinding.pickerSeconds.value = settingViewModel.getSecondsFromStartTime()

            AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme_Dialog))
                .setView(dialogBinding.root)
                .setCancelable(false)
                .setPositiveButton("Done") { dialog, _ ->
                    val minutesMillis = dialogBinding.pickerMinutes.value * 60 * 1000
                    val secondsMillis = dialogBinding.pickerSeconds.value * 1000
                    settingViewModel.setStartTime(minutesMillis.toLong() + secondsMillis.toLong())
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setOnDismissListener {
                    val activity = requireActivity() as MainActivity
                    activity.enableImmersiveMode()
                }
                .create()
                .show()
        }

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

}
