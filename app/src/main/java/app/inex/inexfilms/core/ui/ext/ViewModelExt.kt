package app.inex.inexfilms.core.ui.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlin.reflect.KClass

inline fun <reified VM: ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline viewModelProducer: () -> VM
): ViewModelLazy<VM> = createViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { ownerProducer().viewModelStore },
    viewModelProducer = viewModelProducer
)

inline fun <reified VM: ViewModel> FragmentActivity.viewModels(
    noinline viewModelProducer: () -> VM
): ViewModelLazy<VM> = createViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { viewModelStore },
    viewModelProducer = viewModelProducer
)

@Suppress("UNCHECKED_CAST")
fun <VM: ViewModel> createViewModelLazy (
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    viewModelProducer: () -> VM
): ViewModelLazy<VM> {

    val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModelProducer.invoke() as T
        }
    }

    return ViewModelLazy(viewModelClass, storeProducer) { viewModelFactory }
}