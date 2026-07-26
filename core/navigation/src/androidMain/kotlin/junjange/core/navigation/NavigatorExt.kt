package junjange.core.navigation

fun Navigator.navigateTo(destination: Destination) {
    navigateTo(destination.route)
}

fun Navigator.navigateTo(destination: Destination, args: Map<String, Any>) {
    navigateTo(destination.route, args)
}
