package app.inex.inexfilms.presentation.flow

import app.inex.inexfilms.R
import app.inex.inexfilms.core.common.ext.isTrue
import app.inex.inexfilms.core.navigation.Screens
import app.inex.inexfilms.core.ui.base.BaseFragment
import app.inex.inexfilms.core.ui.ext.viewBinding
import app.inex.inexfilms.databinding.FragmentFlowBinding
import app.inex.inexfilms.presentation.flow.leftmenu.LeftNavItem
import app.inex.inexfilms.presentation.flow.leftmenu.LeftNavView

class FlowFragment: BaseFragment(
    layoutResId = R.layout.fragment_flow
) {

    private val binding by viewBinding(FragmentFlowBinding::bind)

    override fun setupUi() {
        with(binding) {
            navLmv.setOnItemSelectedListener(onItemSelectedListener)
            navLmv.setItems(leftNavItems)
        }
    }

    private val onItemSelectedListener = LeftNavView.OnItemSelectedListener { item ->
        selectTab(item.tag)
    }

    private fun selectTab(tag: String) {
        val fm = childFragmentManager

        val oldFragment = fm.fragments.firstOrNull { it.isVisible }
        val newFragment = fm.findFragmentByTag(tag)

        if (oldFragment != null && newFragment != null && oldFragment == newFragment)
            return

        val transaction = fm.beginTransaction()

        if (newFragment == null) {
            transaction.add(
                R.id.flow_container,
                Screens.tabFragment(tag = tag).createFragment(fm.fragmentFactory),
                tag
            )
        }

        oldFragment?.let { transaction.hide(it) }
        newFragment?.let { transaction.show(it) }

        transaction.commit()
    }

    private fun visibleFragment(): BaseFragment? =
        childFragmentManager.fragments.lastOrNull { it.isVisible } as? BaseFragment

    override fun onBackPressed(): Boolean {
        return visibleFragment()?.onBackPressed().isTrue()
    }

    private val leftNavItems: List<LeftNavItem> by lazy {
        listOf(
            LeftNavItem(
                icon = R.drawable.ic_live_tv,
                text = requireContext().getString(R.string.screen_films_list_name),
                tag = FlowTags.TAG_FILMS_LIST,
            ),
            LeftNavItem(
                icon = R.drawable.ic_settings,
                text = requireContext().getString(R.string.screen_settings_name),
                tag = FlowTags.TAG_SETTINGS,
            )
        )
    }
}