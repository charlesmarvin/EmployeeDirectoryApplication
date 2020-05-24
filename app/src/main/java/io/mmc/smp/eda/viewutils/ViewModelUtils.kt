package io.mmc.smp.eda.viewutils

import android.telephony.PhoneNumberUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import java.net.URL
import java.util.*

class ViewModelFactory<T>(val supplier: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = supplier() as T
}

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline supplier: (() -> T)? = null): T {
    return if (supplier == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, ViewModelFactory(supplier)).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline supplier: (() -> T)? = null): T {
    return if (supplier == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, ViewModelFactory(supplier)).get(T::class.java)
}

// need to investigate the performance impact of this partial function call
fun ImageView.loadAvatar(url: URL) = Picasso.get()
        .load(url.toString())
        .into(this)

fun String.formatAsPhoneNumber() = PhoneNumberUtils.formatNumber(
    this,
    Locale.getDefault().country
)