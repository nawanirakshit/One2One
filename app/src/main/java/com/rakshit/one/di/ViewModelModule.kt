package android.sleek.construction.di

import com.rakshit.one.ui.dashboard.DashboardViewModel
import com.rakshit.one.ui.prelogin.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

//    viewModel { MainGraphViewModel() }
//    viewModel { LoginGraphViewModel(get()) }
    viewModel { DashboardViewModel() }
    viewModel { AuthViewModel() }


}


