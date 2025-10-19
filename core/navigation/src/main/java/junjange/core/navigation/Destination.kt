package junjange.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Destination(
    val route: String,
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val inSelectedIconRes: Int,
    @StringRes val labelResId: Int,
) {
    HOME(
        route = ROUTE_HOME,
        selectedIconRes = R.drawable.ic_home,
        inSelectedIconRes = R.drawable.ic_home_outlined,
        labelResId = R.string.navigation_home,
    ),
    MY_NUMBER(
        route = ROUTE_MY_NUMBER,
        selectedIconRes = R.drawable.ic_clover,
        inSelectedIconRes = R.drawable.ic_clover_outlined,
        labelResId = R.string.navigation_my_number,
    ),
    RANDOM_NUMBER(
        route = ROUTE_RANDOM_NUMBER,
        selectedIconRes = R.drawable.ic_plus,
        inSelectedIconRes = R.drawable.ic_plus,
        labelResId = R.string.navigation_random_number,
    ),
    // TODO 서버 로직 제거로 인해 내정보 탭바 주석
//    MY(
//        route = ROUTE_MY,
//        selectedIconRes = R.drawable.ic_my_page,
//        inSelectedIconRes = R.drawable.ic_my_page_outloned,
//        labelResId = R.string.navigation_my,
//    ),
}

private const val ROUTE_HOME = "home"
const val INITIAL_PAGE = "initialPage"
const val ROUTE_MY_NUMBER = "my_number?${INITIAL_PAGE}={$INITIAL_PAGE}"
private const val ROUTE_RANDOM_NUMBER = "random_number"
private const val ROUTE_MY = "my"
