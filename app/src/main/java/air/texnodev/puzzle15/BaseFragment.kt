package air.texnodev.puzzle15

import air.texnodev.puzzle15.room.AppDatabase
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {


    protected val appDatabase: AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())
}