package com.example.multiplayertimer.util

import android.os.Build
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.multiplayertimer.R
import com.example.multiplayertimer.custom.VerticalButton
import com.example.multiplayertimer.data.Player

@BindingAdapter("android:checkedButton")
fun setCheckedButton(view: RadioGroup, value: Int) {
    val buttonId = when (value) {
        2 -> R.id.toggle_player_two
        3 -> R.id.toggle_player_three
        else -> R.id.toggle_player_four
    }

    if (buttonId != view.checkedRadioButtonId) {
        view.check(buttonId)
    }
}

@InverseBindingAdapter(attribute = "android:checkedButton")
fun getCheckedButton(view: RadioGroup): Int =
    when (view.checkedRadioButtonId) {
        R.id.toggle_player_two -> 2
        R.id.toggle_player_three -> 3
        else -> 4
    }

@BindingAdapter("player")
fun setPlayer(view: Button, player: Player) {
    val bg =
        if (player.active) {
            if (Build.VERSION.SDK_INT >= 23) view.resources.getColor(
                if (player.onTurn) R.color.colorRed else R.color.colorGreen,
                view.context.theme
            )
            else view.resources.getColor(if (player.onTurn) R.color.colorRed else R.color.colorGreen)
        } else {
            if (Build.VERSION.SDK_INT >= 23) view.resources.getColor(
                android.R.color.darker_gray,
                view.context.theme
            )
            else view.resources.getColor(android.R.color.darker_gray)
        }
    view.text = millisToTime(player.timeLeft)
    view.isEnabled = player.active
    view.isClickable = player.onTurn
    view.setBackgroundColor(bg)
}

@BindingAdapter("player")
fun setPlayer(view: VerticalButton, player: Player) {
    val bg =
        if (player.active) {
            if (Build.VERSION.SDK_INT >= 23) view.resources.getColor(
                if (player.onTurn) R.color.colorRed else R.color.colorGreen,
                view.context.theme
            )
            else view.resources.getColor(if (player.onTurn) R.color.colorRed else R.color.colorGreen)
        } else {
            if (Build.VERSION.SDK_INT >= 23) view.resources.getColor(
                android.R.color.darker_gray,
                view.context.theme
            )
            else view.resources.getColor(android.R.color.darker_gray)
        }
    view.text = millisToTime(player.timeLeft)
    view.isEnabled = player.active
    view.isClickable = player.onTurn
    view.setBackgroundColor(bg)
}

@BindingAdapter("time")
fun setTime(view: TextView, value: Long) {
    view.text = millisToTime(value)
}

@BindingAdapter("kickPlayer")
fun setKickPlayer(view: ImageButton, player: Player) {
    view.isEnabled = player.active

    val color =
        if (Build.VERSION.SDK_INT >= 23) view.resources.getColor(
            if (player.active) R.color.colorRed
            else android.R.color.darker_gray,
            view.context.theme
        )
        else view.resources.getColor(
            if (player.active) R.color.colorRed
            else android.R.color.darker_gray
        )

    view.setBackgroundColor(color)
}