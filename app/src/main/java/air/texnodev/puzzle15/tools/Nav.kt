package air.texnodev.puzzle15.tools

import air.texnodev.puzzle15.R
import androidx.navigation.NavOptions

class Nav {

    public fun optionB(fragment: Int):NavOptions{
        return NavOptions.Builder()
            .setEnterAnim(R.anim.enter_anim)
            .setExitAnim(R.anim.exit_anim)
            .setPopEnterAnim(R.anim.pop_enter_anim)
            .setPopExitAnim(R.anim.pop_exit_anim)
            .setPopUpTo(fragment, true)
            .build()
    }

    public fun optionB():NavOptions{
        return NavOptions.Builder()
            .setEnterAnim(R.anim.enter_anim)
            .setExitAnim(R.anim.exit_anim)
            .setPopEnterAnim(R.anim.pop_enter_anim)
            .setPopExitAnim(R.anim.pop_exit_anim)
            .build()
    }


}